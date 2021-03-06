package com.eden.orchid.impl.compilers.pebble.tag;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.compilers.pebble.PebbleWrapperTemplateTag;
import com.google.inject.Provider;
import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.utils.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentTagParser extends BaseTagParser {

    private final Class<? extends TemplateTag> tagClass;
    private final String[] parameters;

    private Map<String, Expression<?>> paramExpressionMap;
    private Expression<?> tagBodyExpression;

    public ContentTagParser(
            Provider<OrchidContext> contextProvider,
            String name,
            String[] parameters,
            Class<? extends TemplateTag> tagClass) {
        super(contextProvider, name);
        this.parameters = parameters;
        this.tagClass = tagClass;
    }

    public RenderableNode parse(Token token, Parser parser) throws ParserException {
        TokenStream stream = parser.getStream();
        int lineNumber = token.getLineNumber();

        // skip over the tag name token
        stream.next();

        // parameter expressions will be added here
        paramExpressionMap = parseParams(parameters, tagClass, stream, parser);
        List<Expression<?>> bodyFilters = parseBodyFilters(stream, parser);
        stream.expect(Token.Type.EXECUTE_END);
        tagBodyExpression = parseBody(bodyFilters, name, stream, parser);

        return new PebbleWrapperTemplateTag.TemplateTagNode(lineNumber, this);
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContextImpl context) throws IOException {
        TemplateTag freshTag = contextProvider.get().getInjector().getInstance(tagClass);
        Map<String, Object> evaluatedParamExpressionMap = evaluateParams(paramExpressionMap, self, context);

        Object pageVar = context.getVariable("page");
        if(pageVar instanceof OrchidPage) {
            freshTag.setPage((OrchidPage) pageVar);
        }

        freshTag.extractOptions(contextProvider.get(), evaluatedParamExpressionMap);

        String bodyContent = StringUtils.toString(tagBodyExpression.evaluate(self, context)).trim();
        TemplateTag.Tab tab = new TemplateTag.SimpleTab(null, bodyContent);
        freshTag.setContent(tab);

        freshTag.onRender();
        if (freshTag.rendersContent()) {
            Map<String, Object> templateArgs = new HashMap<>();
            templateArgs.put("tag", freshTag);

            renderTagContent(self, writer, context, templateArgs, unchecked(e ->  {
                if (!EdenUtils.isEmpty(bodyContent)) {
                    writer.append(bodyContent.trim());
                }
            }));
        }
    }

}
