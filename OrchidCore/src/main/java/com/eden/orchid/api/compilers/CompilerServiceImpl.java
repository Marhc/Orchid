package com.eden.orchid.api.compilers;

import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @since v1.0.0
 * @orchidApi services
 */
@Singleton
public final class CompilerServiceImpl implements CompilerService {

    private String[] binaryExtensions = new String[] {
            "jpg",
            "jpeg",
            "png",
            "pdf",
            "gif",
            "svg",

            // Webfont Formats
            "otf",
            "eot",
            "ttf",
            "woff",
            "woff2",
    };

    @Option("binaryExtensions")
    @Description("Add additional file extensions to recognize as binary, so these assets can be copied directly " +
            "without further processing."
    )
    public String[] customBinaryExtensions;

    @Option("compilerExtensions")
    @Description("Convert unrecognized file extensions into known file types for the compilers. The should be a " +
            "mapping with keys of the unrecognized extension and values of the known extension. These take " +
            "precedence over the normally recognized extensions."
    )
    public JSONObject customCompilerExtensions;

    private String[] ignoredOutputExtensions = new String[] {
            "min",
            "index",
    };

    @Option("ignoredOutputExtensions")
    @Description("Add additional file extensions to exclude from counting as an 'output extension' An example would " +
            "be 'min' in a filename like 'index.min.js', which is commonly used to denote a minified asset and is " +
            "not intended to make a file named 'index.min'."
    )
    public String[] customIgnoredOutputExtensions;

    @Getter @Setter
    @Option @StringDefault("peb")
    @Description("Convert unrecognized file extensions into known file types for the compilers. The should be a " +
            "mapping with keys of the unrecognized extension and values of the known extension. These take " +
            "precedence over the normally recognized extensions."
    )
    public String defaultPrecompilerExtension;

    private OrchidContext context;

    private Set<OrchidCompiler> compilers;
    private Set<OrchidParser> parsers;
    private OrchidPrecompiler precompiler;

    private Set<String> compilerExtensions;
    private Set<String> parserExtensions;

    private Map<String, OrchidCompiler> compilerMap;
    private Map<String, OrchidParser> parserMap;

    private Map<String, OrchidCompiler> customCompilerMap;

    @Inject
    public CompilerServiceImpl(Set<OrchidCompiler> compilers, Set<OrchidParser> parsers, OrchidPrecompiler precompiler) {
        this.precompiler = precompiler;
        this.compilers = new TreeSet<>(compilers);
        this.parsers = new TreeSet<>(parsers);
        buildCompilerIndex();
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void onPostExtraction() {
        buildCustomCompilerIndex();
    }

    private void buildCompilerIndex() {
        this.compilerExtensions = new HashSet<>();
        this.parserExtensions = new HashSet<>();
        this.compilerMap = new HashMap<>();
        this.parserMap = new HashMap<>();

        for (OrchidCompiler compiler : this.compilers) {
            if (!EdenUtils.isEmpty(compiler.getSourceExtensions())) {
                for (String ext : compiler.getSourceExtensions()) {
                    compilerExtensions.add(ext);
                    compilerMap.put(ext, compiler);
                }
            }
        }

        for (OrchidParser parser : this.parsers) {
            if (!EdenUtils.isEmpty(parser.getSourceExtensions())) {
                for (String ext : parser.getSourceExtensions()) {
                    parserExtensions.add(ext);
                    parserMap.put(ext, parser);
                }
            }
        }
    }

    private void buildCustomCompilerIndex() {
        this.customCompilerMap = new HashMap<>();

        if(customCompilerExtensions != null) {
            for(String ext : customCompilerExtensions.keySet()) {
                if(compilerMap.containsKey(customCompilerExtensions.getString(ext))) {
                    customCompilerMap.put(ext, compilerMap.get(customCompilerExtensions.getString(ext)));
                }
            }
        }
    }

    public Set<String> getCompilerExtensions() {
        Set<String> allExtensions = new HashSet<>();

        if(customCompilerExtensions != null) {
            allExtensions.addAll(customCompilerExtensions.keySet());
        }
        allExtensions.addAll(compilerExtensions);

        return allExtensions;
    }

    public Set<String> getParserExtensions() {
        return parserExtensions;
    }

    public OrchidCompiler compilerFor(String extension) {
        if(customCompilerMap != null && customCompilerMap.containsKey(extension)) {
            return customCompilerMap.get(extension);
        }
        return compilerMap.getOrDefault(extension, null);
    }

    public OrchidParser parserFor(String extension) {
        return parserMap.getOrDefault(extension, null);
    }

    public String compile(String extension, String input) {
        return this.compile(extension, input, null);
    }

    public String compile(String extension, String input, Object data) {
        OrchidCompiler compiler = compilerFor(extension);

        if (compiler != null) {
            synchronized(compiler) {
                return compiler.compile(extension, input, context.getSiteData(data));
            }
        }
        else {
            return input;
        }
    }

    public Map<String, Object> parse(String extension, String input) {
        OrchidParser parser = parserFor(extension);
        return (parser != null) ? parser.parse(extension, input) : null;
    }

    public String serialize(String extension, Object input) {
        OrchidParser parser = parserFor(extension);
        return (parser != null) ? parser.serialize(extension, input) : null;
    }

    public EdenPair<String, Map<String, Object>> getEmbeddedData(String input) {
        return precompiler.getEmbeddedData(input);
    }

    public String getOutputExtension(String extension) {
        OrchidCompiler compiler = compilerFor(extension);
        return (compiler != null) ? compiler.getOutputExtension() : extension;
    }

    public List<String> getBinaryExtensions() {
        return getExtensionList(binaryExtensions, customBinaryExtensions);
    }

    public boolean isBinaryExtension(String extension) {
        return isExtensionInList(extension, binaryExtensions, customBinaryExtensions);
    }

    public List<String> getIgnoredOutputExtensions() {
        return getExtensionList(ignoredOutputExtensions, customIgnoredOutputExtensions);
    }

    public boolean isIgnoredOutputExtension(String extension) {
        return isExtensionInList(extension, ignoredOutputExtensions, customIgnoredOutputExtensions);
    }

    private List<String> getExtensionList(String[]... arrays) {
        List<String> allExtensions = new ArrayList<>();

        for(String[] array : arrays) {
            if (!EdenUtils.isEmpty(array)) {
                Collections.addAll(allExtensions, array);
            }
        }

        return allExtensions;
    }

    private boolean isExtensionInList(String extension, String[]... arrays) {
        for (String ignoredExtension : getExtensionList(arrays)) {
            if (extension.equalsIgnoreCase(ignoredExtension)) {
                return true;
            }
        }

        return false;
    }

}
