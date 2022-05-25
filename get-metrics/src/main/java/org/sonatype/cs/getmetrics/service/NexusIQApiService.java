package org.sonatype.cs.getmetrics.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.json.Json;
import javax.json.JsonReader;

@Service
public class NexusIQApiService {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(NexusIQApiService.class);

    private NexusIqApiConnectionService nexusIqApiConnectionService;
    private FileIoService fileIoService;
    private String iqUrl;
    private String iqUser;
    private String iqPasswd;

    public NexusIQApiService(
            NexusIqApiConnectionService nexusIqApiConnectionService,
            FileIoService fileIoService,
            @Value("${iq.url}") String iqUrl,
            @Value("${iq.user}") String iqUser,
            @Value("${iq.passwd}") String iqPasswd) {
        this.nexusIqApiConnectionService = nexusIqApiConnectionService;
        this.fileIoService = fileIoService;
        this.iqUrl = iqUrl;
        this.iqUser = iqUser;
        this.iqPasswd = iqPasswd;
    }

    public void makeReport(CsvFileService cfs, String endPoint) throws IOException {
        HttpURLConnection urlConnection =
                nexusIqApiConnectionService.createAuthorisedUrlConnection(
                        iqUser, iqPasswd, iqUrl, endPoint);
        InputStream is;
        try {
            is = urlConnection.getInputStream();
        } catch (IOException e) {
            logger.info(
                    "IOException raised when trying to reach iqUrl [{}], iqApi [{}], endpoint [{}]"
                            + " (return code [{} - {}]) - no CSV produced",
                    iqUrl,
                    endPoint,
                    urlConnection.getResponseCode(),
                    urlConnection.getResponseMessage());
            return;
        }
        try (JsonReader reader = Json.createReader(is)) {
            cfs.makeCsvFile(fileIoService, reader);
        }
    }
}
