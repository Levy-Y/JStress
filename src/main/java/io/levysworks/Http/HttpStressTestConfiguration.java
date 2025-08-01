package io.levysworks.Http;

import io.levysworks.Http.Enums.HTTPRequestMethods;

import java.io.File;
import java.util.Map;

public class HttpStressTestConfiguration {
    private String host;
    private Integer port;
    private HTTPRequestMethods method;
    private Map<String, String> headers;
    private File bodyFile;
    private Integer bodySize;
    private Integer expectedStatusCode;
    private Integer frequency;
    private Integer duration;
    private Integer threadCount;
    private Boolean verbose;
    
    public HttpStressTestConfiguration(String host, Integer port, HTTPRequestMethods method,
                                       Map<String, String> headers, File bodyFile, Integer bodySize,
                                       Integer expectedStatusCode, Integer frequency, Integer duration,
                                       Integer threadCount, Boolean verbose) {
        this.host = host;
        this.port = port;
        this.method = method;
        this.headers = headers;
        this.bodyFile = bodyFile;
        this.bodySize = bodySize;
        this.expectedStatusCode = expectedStatusCode;
        this.frequency = frequency;
        this.duration = duration;
        this.threadCount = threadCount;
        this.verbose = verbose;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public HTTPRequestMethods getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public File getBodyFile() {
        return bodyFile;
    }

    public Integer getBodySize() {
        return bodySize;
    }

    public Integer getExpectedStatusCode() {
        return expectedStatusCode;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public Boolean getVerbose() {
        return verbose;
    }
}
