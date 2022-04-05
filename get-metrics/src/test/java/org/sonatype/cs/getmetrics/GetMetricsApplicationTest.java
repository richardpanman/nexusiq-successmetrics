package org.sonatype.cs.getmetrics;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sonatype.cs.getmetrics.reports.ApplicationEvaluations;
import org.sonatype.cs.getmetrics.reports.AutoReleasedFromQuarantineComponents;
import org.sonatype.cs.getmetrics.reports.AutoReleasedFromQuarantineConfig;
import org.sonatype.cs.getmetrics.reports.AutoReleasedFromQuarantineSummary;
import org.sonatype.cs.getmetrics.reports.QuarantinedComponents;
import org.sonatype.cs.getmetrics.reports.QuarantinedComponentsSummary;
import org.sonatype.cs.getmetrics.reports.Waivers;
import org.sonatype.cs.getmetrics.service.FileIoService;
import org.sonatype.cs.getmetrics.service.NexusIQAPIPagingService;
import org.sonatype.cs.getmetrics.service.NexusIQApiService;
import org.sonatype.cs.getmetrics.service.NexusIQSuccessMetrics;
import org.sonatype.cs.getmetrics.service.PolicyIdsService;

import java.io.IOException;

@DisplayName("GetMetricsApplication should")
public class GetMetricsApplicationTest {
    PolicyIdsService policyIdsService;
    NexusIQAPIPagingService nexusIQAPIPagingService;
    NexusIQApiService nexusIQApiService;
    FileIoService fileIoService;
    NexusIQSuccessMetrics nexusIQSuccessMetrics;

    @BeforeEach
    void setUp() {
        policyIdsService = mock(PolicyIdsService.class);
        nexusIQAPIPagingService = mock(NexusIQAPIPagingService.class);
        nexusIQApiService = mock(NexusIQApiService.class);
        fileIoService = mock(FileIoService.class);
        nexusIQSuccessMetrics = mock(NexusIQSuccessMetrics.class);
    }

    @DisplayName("make reports for all selected metrics")
    @Test
    void testRun() {
        GetMetricsApplication getMetricsApplication =
                new GetMetricsApplication(
                        policyIdsService,
                        nexusIQAPIPagingService,
                        nexusIQApiService,
                        fileIoService,
                        nexusIQSuccessMetrics,
                        true,
                        true,
                        true,
                        true,
                        true);

        try {
            getMetricsApplication.run();
            verify(nexusIQSuccessMetrics, times(1)).createSuccessMetricsCsvFile();
            verify(nexusIQApiService, times(1))
                    .makeReport(any(ApplicationEvaluations.class), eq("/reports/applications"));
            verify(nexusIQApiService, times(1))
                    .makeReport(any(Waivers.class), eq("/reports/components/waivers"));
            verify(nexusIQApiService, times(1))
                    .makeReport(
                            any(AutoReleasedFromQuarantineConfig.class),
                            eq("/firewall/releaseQuarantine/configuration"));
            verify(nexusIQApiService, times(1))
                    .makeReport(
                            any(AutoReleasedFromQuarantineSummary.class),
                            eq("/firewall/releaseQuarantine/summary"));
            verify(nexusIQApiService, times(1))
                    .makeReport(
                            any(QuarantinedComponentsSummary.class),
                            eq("/firewall/quarantine/summary"));
            verify(nexusIQAPIPagingService, times(1))
                    .makeReport(
                            any(QuarantinedComponents.class),
                            eq("/firewall/components/quarantined"));
            verify(nexusIQAPIPagingService, times(1))
                    .makeReport(
                            any(AutoReleasedFromQuarantineComponents.class),
                            eq("/firewall/components/autoReleasedFromQuarantine"));

        } catch (JSONException | IOException | HttpException e) {
            Assertions.fail();
        }
    }

    @DisplayName("not make reports for all unselected metrics")
    @Test
    void testRunNoReports() {
        GetMetricsApplication getMetricsApplication =
                new GetMetricsApplication(
                        policyIdsService,
                        nexusIQAPIPagingService,
                        nexusIQApiService,
                        fileIoService,
                        nexusIQSuccessMetrics,
                        false,
                        false,
                        false,
                        false,
                        false);

        try {
            getMetricsApplication.run();
            verify(nexusIQSuccessMetrics, times(0)).createSuccessMetricsCsvFile();
            verify(nexusIQApiService, times(0))
                    .makeReport(any(ApplicationEvaluations.class), eq("/reports/applications"));
            verify(nexusIQApiService, times(0))
                    .makeReport(any(Waivers.class), eq("/reports/components/waivers"));
            verify(nexusIQApiService, times(0))
                    .makeReport(any(AutoReleasedFromQuarantineConfig.class), anyString());
            verify(nexusIQApiService, times(0))
                    .makeReport(any(AutoReleasedFromQuarantineSummary.class), anyString());
            verify(nexusIQApiService, times(0))
                    .makeReport(any(QuarantinedComponentsSummary.class), anyString());
            verify(nexusIQAPIPagingService, times(0))
                    .makeReport(any(QuarantinedComponents.class), anyString());
            verify(nexusIQAPIPagingService, times(0))
                    .makeReport(any(AutoReleasedFromQuarantineComponents.class), anyString());
        } catch (JSONException | IOException | HttpException e) {
            Assertions.fail();
        }
    }
}
