package com.swissre.gfp.agentextractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class Extractor implements ApplicationListener<ApplicationStartedEvent> {
    public static final Logger LOGGER = LoggerFactory.getLogger(Extractor.class);
    @Value("${extraction.path}")
    private Path path;
    @Value("${instrumentation.key}")
    private String key;
    @Value("${role.name:#{null}}")
    private String rolename;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        String jarName = "applicationinsights-agent-3.0.0-PREVIEW.4.jar";
        LOGGER.info("Extracting {} to {}", jarName, path);
        InputStream resourceAsStream = getClass().getResourceAsStream("/" + jarName);
        try {
            Files.copy(resourceAsStream, path.resolve(jarName), StandardCopyOption.REPLACE_EXISTING);

            Path jsonPath = path.resolve("ApplicationInsights.json");
            Files.deleteIfExists(jsonPath);

            LOGGER.info("Writing agent json: {}", jsonPath);
            String specificRoleName = rolename == null ? "" : "      \"roleName\": \"" + rolename + "\"\n";
            Files.writeString(jsonPath,
                              "{ \"instrumentationSettings\": {\n" +
                                      "    " +
                                      "  \"connectionString\": \"InstrumentationKey=" + key + "\",\n" +
                                      "    \"preview\": {\n" +
                                      specificRoleName +
                                      "    }\n" +
                                      "  " +
                                      "  }\n" +
                                      "}");
            LOGGER.info("Extraction complete");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
