package org.sonatype.cs.metrics;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

// https://spring.io/guides/gs/testing-web/
@SpringBootTest(
        properties = {"data.dir=src/test/resources", "spring.profiles.active=insights"},
        webEnvironment = WebEnvironment.RANDOM_PORT)
public class SuccessMetricsIsightsApplicationTest {

    @Autowired private SuccessMetricsApplication controller;

    @Test
    public void checkCSVFileIsGenerated() throws Exception {
        SpringApplication app = new SpringApplication(SuccessMetricsApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run();
        assertNotNull(controller.gettimestamp());
        File outputFile =
                new File(
                        "src/test/resources/output/insights-" + controller.gettimestamp() + ".csv");
        assertTrue(outputFile.exists());
        String fileContents =
                new String(Files.readAllBytes(Paths.get(outputFile.getAbsolutePath())));
        Approvals.verify(fileContents);
    }
}
