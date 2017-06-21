package com.eden.orchid.server.impl.controllers.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.utilities.OrchidUtils;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IndexFileResponse {

    private OrchidContext context;
    private OrchidResources resources;

    private Map<String, String> iconMap;


    @Inject
    public IndexFileResponse(OrchidContext context, OrchidResources resources) {
        this.context = context;
        this.resources = resources;

        this.iconMap = new HashMap<>();

        iconMap.put("css", "/assets/svg/css.svg");
        iconMap.put("csv", "/assets/svg/csv.svg");
        iconMap.put("html", "/assets/svg/html.svg");
        iconMap.put("jpg", "/assets/svg/jpg.svg");
        iconMap.put("js", "/assets/svg/js.svg");
        iconMap.put("json", "/assets/svg/json.svg");
        iconMap.put("png", "/assets/svg/png.svg");
        iconMap.put("xml", "/assets/svg/xml.svg");

        iconMap.put("file", "/assets/svg/folder.svg");
        iconMap.put("folder", "/assets/svg/folder.svg");
    }

    public NanoHTTPD.Response getResponse(File targetFile, String targetPath) {
        String content = "";

        if (targetFile.isDirectory()) {
            File[] files = targetFile.listFiles();

            if (files != null) {
                JSONArray jsonDirs = new JSONArray();
                JSONArray jsonFiles = new JSONArray();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-hh:mm:ss z", Locale.getDefault());

                for (File file : files) {
                    JSONObject newFile = new JSONObject();
                    newFile.put("url", OrchidUtils.applyBaseUrl(context, StringUtils.strip(targetPath, "/") + "/" + file.getName()));
                    newFile.put("name", file.getName());
                    newFile.put("size", file.length());
                    newFile.put("date", formatter.format(new Date(file.lastModified())));

                    if (file.isDirectory()) {
                        newFile.put("icon", iconMap.get("folder"));
                        jsonDirs.put(newFile);
                    }
                    else {
                        newFile.put("icon",
                                iconMap.containsKey(FilenameUtils.getExtension(file.getName()))
                                        ? iconMap.get(FilenameUtils.getExtension(file.getName()))
                                        : iconMap.get("file")
                        );
                        jsonFiles.put(newFile);
                    }
                }

                JSONObject page = new JSONObject();
                page.put("title", "List of files/dirs under " + targetPath);
                page.put("path", targetPath);
                page.put("dirs", jsonDirs);
                page.put("files", jsonFiles);

                JSONObject object = new JSONObject(context.getRoot().toMap());
                object.put("page", page);

                OrchidResource resource = resources.getResourceEntry("templates/server/directoryListing.twig");

                if (resource != null) {
                    content = context.compile(resource.getReference().getExtension(), resource.getContent(), object.toString(2));
                }
                else {
                    content = object.toString(2);
                }
            }
        }

        Clog.i("Rendering Index: #{$1}", new Object[]{targetPath});
        return NanoHTTPD.newFixedLengthResponse(content);
    }
}