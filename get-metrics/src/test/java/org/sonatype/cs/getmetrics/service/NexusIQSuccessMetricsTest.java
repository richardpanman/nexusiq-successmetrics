package org.sonatype.cs.getmetrics.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

// TODO - syntax checking on inputs in NexusIQSuccessMetrics, currently essentially none

@DisplayName("NexusIQSuccessMetrics should")
public class NexusIQSuccessMetricsTest {
    private NexusIqApiConnectionService nexusIqApiConnectionService;
    private FileIoService fileIoService;

    @BeforeEach
    public void setUp() {
        nexusIqApiConnectionService = mock(NexusIqApiConnectionService.class);
        fileIoService = mock(FileIoService.class);
    }

    @Test
    @DisplayName("write a CSV file when NexusIQ API returns a successful response")
    void testCreateSuccessMetricsCsvFile() {
        NexusIQSuccessMetrics nexusIQSuccessMetrics =
                new NexusIQSuccessMetrics(
                        nexusIqApiConnectionService,
                        fileIoService,
                        "iqUrl",
                        "iqUser",
                        "iqPwd",
                        "iqSmPeriod",
                        "iqApiFirstTimePeriod",
                        "iqApiLastTimePeriod",
                        "",
                        "",
                        "iqApi",
                        "iqReportsEndpoint");
        String apiPayload =
                new String(
                        "{\"timePeriod\":\"IQSMPERIOD\",\"lastTimePeriod\":\"iqApiLastTimePeriod\",\"firstTimePeriod\":\"iqApiFirstTimePeriod\"}");
        try {
            when(nexusIqApiConnectionService.retrieveCsvBasedOnPayload(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "iqReportsEndpoint", apiPayload))
                    .thenReturn("String");
            nexusIQSuccessMetrics.createSuccessMetricsCsvFile();
            verify(nexusIqApiConnectionService)
                    .retrieveCsvBasedOnPayload(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "iqReportsEndpoint", apiPayload);
            verify(nexusIqApiConnectionService, times(1))
                    .retrieveCsvBasedOnPayload(
                            anyString(),
                            anyString(),
                            anyString(),
                            anyString(),
                            anyString(),
                            anyString());
            verify(fileIoService, times(1)).writeSuccessMetricsFile(any(InputStream.class));
        } catch (JSONException | IOException | HttpException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("write a CSV file when NexusIQ API returns a successful response (no end period)")
    void testCreateSuccessMetricsCsvFileNoEndPeriod() {

        NexusIQSuccessMetrics nexusIQSuccessMetrics =
                new NexusIQSuccessMetrics(
                        nexusIqApiConnectionService,
                        fileIoService,
                        "iqUrl",
                        "iqUser",
                        "iqPwd",
                        "iqSmPeriod",
                        "iqApiFirstTimePeriod",
                        "",
                        "",
                        "",
                        "iqApi",
                        "iqReportsEndpoint");
        String apiPayload =
                new String(
                        "{\"timePeriod\":\"IQSMPERIOD\",\"firstTimePeriod\":\"iqApiFirstTimePeriod\"}");
        try {
            when(nexusIqApiConnectionService.retrieveCsvBasedOnPayload(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "iqReportsEndpoint", apiPayload))
                    .thenReturn("String");
            nexusIQSuccessMetrics.createSuccessMetricsCsvFile();
            verify(nexusIqApiConnectionService)
                    .retrieveCsvBasedOnPayload(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "iqReportsEndpoint", apiPayload);
            verify(nexusIqApiConnectionService, times(1))
                    .retrieveCsvBasedOnPayload(
                            anyString(),
                            anyString(),
                            anyString(),
                            anyString(),
                            anyString(),
                            anyString());
            verify(fileIoService, times(1)).writeSuccessMetricsFile(any(InputStream.class));
        } catch (JSONException | IOException | HttpException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("raise an exception if the firstTimePeriod is not set")
    void testCreateSuccessMetricsCsvFileNoApplicationsNoFirstTimePeriod() {
        NexusIQSuccessMetrics nexusIQSuccessMetrics =
                new NexusIQSuccessMetrics(
                        nexusIqApiConnectionService,
                        fileIoService,
                        "iqUrl",
                        "iqUser",
                        "iqPwd",
                        "iqSmPeriod",
                        null,
                        "iqApiLastTimePeriod",
                        "",
                        "",
                        "iqApi",
                        "iqReportsEndpoint");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> nexusIQSuccessMetrics.createSuccessMetricsCsvFile());
    }

    @Test
    @DisplayName(
            "write a CSV file when NexusIQ API returns a successful response (single organization)")
    void testCreateSuccessMetricsCsvFileSingleOrganization() {
        try {
            // given
            NexusIQSuccessMetrics nexusIQSuccessMetrics =
                    new NexusIQSuccessMetrics(
                            nexusIqApiConnectionService,
                            fileIoService,
                            "iqUrl",
                            "iqUser",
                            "iqPwd",
                            "iqSmPeriod",
                            "iqApiFirstTimePeriod",
                            "iqApiLastTimePeriod",
                            "",
                            "singleOrg",
                            "iqApi",
                            "iqReportsEndpoint");
            String apiPayload =
                    new String(
                            "{\"organizationIds\":[\"4b6b92347d194781823102a6851ff474\"],\"timePeriod\":\"IQSMPERIOD\",\"lastTimePeriod\":\"iqApiLastTimePeriod\",\"firstTimePeriod\":\"iqApiFirstTimePeriod\"}");

            when(nexusIqApiConnectionService.retrieveJsonFromIq(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "/organizations"))
                    .thenReturn(
                            "{\"organizations\": [{\"id\":"
                                    + " \"4b6b92347d194781823102a6851ff474\",\"name\":"
                                    + " \"singleOrg\",\"tags\": []}]}");
            when(nexusIqApiConnectionService.retrieveCsvBasedOnPayload(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "iqReportsEndpoint", apiPayload))
                    .thenReturn("String");

            // when
            nexusIQSuccessMetrics.createSuccessMetricsCsvFile();

            // then
            verify(nexusIqApiConnectionService, times(1))
                    .retrieveJsonFromIq("iqUser", "iqPwd", "iqUrl", "iqApi", "/organizations");
            verify(nexusIqApiConnectionService, times(1))
                    .retrieveCsvBasedOnPayload(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "iqReportsEndpoint", apiPayload);
            verify(fileIoService, times(1)).writeSuccessMetricsFile(any(InputStream.class));
        } catch (JSONException | IOException | HttpException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName(
            "write a CSV file when NexusIQ API returns a successful response (single application)")
    void testCreateSuccessMetricsCsvFileSingleApplication() {
        try {
            // given
            NexusIQSuccessMetrics nexusIQSuccessMetrics =
                    new NexusIQSuccessMetrics(
                            nexusIqApiConnectionService,
                            fileIoService,
                            "iqUrl",
                            "iqUser",
                            "iqPwd",
                            "iqSmPeriod",
                            "iqApiFirstTimePeriod",
                            "iqApiLastTimePeriod",
                            "singleApplication",
                            "",
                            "iqApi",
                            "iqReportsEndpoint");
            String apiPayload =
                    new String(
                            "{\"timePeriod\":\"IQSMPERIOD\",\"lastTimePeriod\":\"iqApiLastTimePeriod\",\"firstTimePeriod\":\"iqApiFirstTimePeriod\",\"applicationIds\":[\"4b6b92347d194781823102a6851ff474\"]}");

            when(nexusIqApiConnectionService.retrieveJsonFromIq(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "/applications"))
                    .thenReturn(
                            "{\"applications\": [{\"id\":"
                                    + " \"4b6b92347d194781823102a6851ff474\",\"name\":"
                                    + " \"singleApplication\",\"tags\": []}]}");
            when(nexusIqApiConnectionService.retrieveCsvBasedOnPayload(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "iqReportsEndpoint", apiPayload))
                    .thenReturn("String");

            // when
            nexusIQSuccessMetrics.createSuccessMetricsCsvFile();

            // then
            verify(nexusIqApiConnectionService, times(1))
                    .retrieveJsonFromIq("iqUser", "iqPwd", "iqUrl", "iqApi", "/applications");
            verify(nexusIqApiConnectionService, times(1))
                    .retrieveCsvBasedOnPayload(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "iqReportsEndpoint", apiPayload);
            verify(fileIoService, times(1)).writeSuccessMetricsFile(any(InputStream.class));
        } catch (JSONException | IOException | HttpException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("throw an exception if the application or organization is not known in IQ")
    void testCreateSuccessMetricsCsvFileUnknownSingleApplication() {
        try {
            // given
            NexusIQSuccessMetrics nexusIQSuccessMetrics =
                    new NexusIQSuccessMetrics(
                            nexusIqApiConnectionService,
                            fileIoService,
                            "iqUrl",
                            "iqUser",
                            "iqPwd",
                            "iqSmPeriod",
                            "iqApiFirstTimePeriod",
                            "iqApiLastTimePeriod",
                            "unknownApplication",
                            "",
                            "iqApi",
                            "iqReportsEndpoint");

            when(nexusIqApiConnectionService.retrieveJsonFromIq(
                            "iqUser", "iqPwd", "iqUrl", "iqApi", "/applications"))
                    .thenReturn(
                            "{\"applications\": [{\"id\":"
                                    + " \"4b6b92347d194781823102a6851ff474\",\"name\":"
                                    + " \"singleApplication\",\"tags\": []}]}");

            // when
            final IllegalArgumentException thrown =
                    Assertions.assertThrows(
                            IllegalArgumentException.class,
                            () -> nexusIQSuccessMetrics.createSuccessMetricsCsvFile());
            Assertions.assertEquals(
                    "unknownApplication is unknown to IQ when listing applications",
                    thrown.getMessage());

            // then
            verify(nexusIqApiConnectionService, times(1))
                    .retrieveJsonFromIq("iqUser", "iqPwd", "iqUrl", "iqApi", "/applications");
            verify(nexusIqApiConnectionService, times(0))
                    .retrieveCsvBasedOnPayload(
                            anyString(),
                            anyString(),
                            anyString(),
                            anyString(),
                            anyString(),
                            anyString());
            verify(fileIoService, times(0)).writeSuccessMetricsFile(any(InputStream.class));
        } catch (JSONException | IOException | HttpException e) {
            Assertions.fail();
        }
    }
}
