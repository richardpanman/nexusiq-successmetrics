package org.sonatype.cs.getmetrics;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.getmetrics.reports.*;
import org.sonatype.cs.getmetrics.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GetMetricsApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(GetMetricsApplication.class);

    private PolicyIdsService policyIdsService;
    private NexusIQAPIPagingService nexusIQAPIPagingService;
    private NexusIQApiService nexusIQApiService;
    private FileIoService fileIoService;
    private NexusIQSuccessMetrics nexusIQSuccessMetrics;
    private boolean successmetrics;
    private boolean applicationsevaluations;
    private boolean waivers;
    private boolean policyviolations;
    private boolean firewall;

    public GetMetricsApplication(
            PolicyIdsService policyIdsService,
            NexusIQAPIPagingService nexusIQAPIPagingService,
            NexusIQApiService nexusIQApiService,
            FileIoService fileIoService,
            NexusIQSuccessMetrics nexusIQSuccessMetrics,
            @Value("${metrics.successmetrics}") boolean successmetrics,
            @Value("${metrics.applicationsevaluations}") boolean applicationsevaluations,
            @Value("${metrics.waivers}") boolean waivers,
            @Value("${metrics.policyviolations}") boolean policyviolations,
            @Value("${metrics.firewall}") boolean firewall) {
        this.policyIdsService = policyIdsService;
        this.nexusIQAPIPagingService = nexusIQAPIPagingService;
        this.nexusIQApiService = nexusIQApiService;
        this.fileIoService = fileIoService;
        this.nexusIQSuccessMetrics = nexusIQSuccessMetrics;
        this.successmetrics = successmetrics;
        this.applicationsevaluations = applicationsevaluations;
        this.waivers = waivers;
        this.policyviolations = policyviolations;
        this.firewall = firewall;
    }

    public static void main(String[] args) {
        SpringApplication.run(GetMetricsApplication.class, args);
    }

    @Override
    public void run(String... args) throws JSONException, IOException, HttpException {
        log.info("Starting GetMetrics Application");

        fileIoService.initMetricsDir();

        if (successmetrics) {
            nexusIQSuccessMetrics.createSuccessMetricsCsvFile();
        }

        if (applicationsevaluations) {
            nexusIQApiService.makeReport(new ApplicationEvaluations(), "/reports/applications");
        }

        if (waivers) {
            nexusIQApiService.makeReport(new Waivers(), "/reports/components/waivers");
        }

        if (policyviolations) {
            nexusIQApiService.makeReport(
                    new PolicyViolations(policyIdsService),
                    policyIdsService.getPolicyIdsEndpoint());
        }

        if (firewall) {
            nexusIQApiService.makeReport(
                    new AutoReleasedFromQuarantineConfig(),
                    "/firewall/releaseQuarantine/configuration");

            nexusIQApiService.makeReport(
                    new AutoReleasedFromQuarantineSummary(), "/firewall/releaseQuarantine/summary");
            nexusIQApiService.makeReport(
                    new QuarantinedComponentsSummary(), "/firewall/quarantine/summary");

            nexusIQAPIPagingService.makeReport(
                    new QuarantinedComponents(), "/firewall/components/quarantined");
            nexusIQAPIPagingService.makeReport(
                    new AutoReleasedFromQuarantineComponents(),
                    "/firewall/components/autoReleasedFromQuarantine");
        }
    }
}
