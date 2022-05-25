package org.sonatype.cs.getmetrics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

@Service
public class NexusIQApiDataService {
    private NexusIqApiConnectionService nexusIqApiConnectionService;
    private String iqUrl;
    private String iqUser;
    private String iqPasswd;

    public NexusIQApiDataService(
            NexusIqApiConnectionService nexusIqApiConnectionService,
            @Value("${iq.url}") String iqUrl,
            @Value("${iq.user}") String iqUser,
            @Value("${iq.passwd}") String iqPasswd) {
        this.nexusIqApiConnectionService = nexusIqApiConnectionService;
        this.iqUrl = iqUrl;
        this.iqUser = iqUser;
        this.iqPasswd = iqPasswd;
    }

    public JsonObject getData(String endPoint) throws IOException {
        HttpURLConnection urlConnection =
                nexusIqApiConnectionService.createAuthorisedUrlConnection(
                        iqUser, iqPasswd, iqUrl, endPoint);

        return getJsonReaderFromURLConnection(urlConnection);
    }

    static JsonObject getJsonReaderFromURLConnection(HttpURLConnection urlConnection)
            throws IOException {
        try (InputStream is = urlConnection.getInputStream()) {
            try (JsonReader reader = Json.createReader(is)) {
                return reader.readObject();
            }
        }
    }
}
