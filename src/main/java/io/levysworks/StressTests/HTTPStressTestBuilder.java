package io.levysworks.StressTests;

import io.levysworks.Enums.HTTPRequestTypes;
import io.levysworks.Interfaces.BaseTest;
import io.levysworks.Reports.HTTPTestReportManager;
import io.levysworks.Utils.FileGenerator;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HTTPStressTestBuilder {
    public static class HTTPStressTest implements BaseTest {
        private int durationMili;
        private int frequency;
        private Integer expected_status;
        private HttpRequest request;
        private boolean verbose;

        private long startTime;

        private HTTPStressTest(int duration, int frequency, Integer expected_status, HttpRequest request, boolean verbose) {
            this.durationMili = duration * 1000;
            this.frequency = frequency;
            this.expected_status = expected_status;
            this.request = request;
            this.verbose = verbose;
        }

        private int successful_requests;
        private int failed_requests;

        @Override
        public void run() {
            long interval = 1000 / frequency;
            startTime = System.currentTimeMillis();

            String id = Thread.currentThread().getName();

            if (verbose) {
                System.out.printf("Thread: %s; Starting HTTP stress test on %s\n", id, request.uri().toString());
            }

            while (System.currentTimeMillis() - startTime <= durationMili) {
                try (HttpClient client = HttpClient.newHttpClient()) {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (expected_status != null && response.statusCode() != expected_status && verbose) {
                        System.err.printf("Expected HTTP status code %d, got %d\n", expected_status, response.statusCode());
                    }
                    successful_requests++;

                } catch (IOException | InterruptedException e) {
                    if ((e.getCause() instanceof IOException) && verbose) {
                        System.err.printf("Thread: %s; Interrupted. Request bounced off %s\n", id, request.uri().toString());
                    }
                    failed_requests++;
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            HTTPTestReportManager.addData(successful_requests + failed_requests, successful_requests, failed_requests);
        }
    }

    private String host = "0.0.0.0";
    private int port = 0;
    private int frequency = 0;
    private int duration = 0;
    private HTTPRequestTypes method = HTTPRequestTypes.GET;
    private Map<String, String> headers = new HashMap<>();
    private File bodyFile = null;
    private Integer bodySize = 0;
    private Integer expected_status;
    private boolean verbose = false;

    public HTTPStressTestBuilder host(String host) {
        this.host = host;
        return this;
    }

    public HTTPStressTestBuilder port(int port) {
        this.port = port;
        return this;
    }

    public HTTPStressTestBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public HTTPStressTestBuilder frequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    public HTTPStressTestBuilder method(HTTPRequestTypes method) {
        this.method = method;
        return this;
    }

    public HTTPStressTestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HTTPStressTestBuilder bodyFile(File bodyFile) {
        this.bodyFile = bodyFile;
        return this;
    }

    public HTTPStressTestBuilder bodySize(Integer bodySize) {
        this.bodySize = bodySize;
        return this;
    }

    public HTTPStressTestBuilder expected_status(Integer expected_status) {
        this.expected_status = expected_status;
        return this;
    }

    public HTTPStressTestBuilder verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public HTTPStressTest build() throws IOException {
        String url = "http://" + host + ":" + port;

        Builder builder = HttpRequest.newBuilder().uri(URI.create(url));

        switch (method) {
            case HEAD:
                builder.HEAD();
                break;
            case POST:
                if (bodyFile != null) {
                    builder.POST(HttpRequest.BodyPublishers.ofFile(bodyFile.toPath()));

                    break;
                } else if (bodySize > 0) {
                    File tempFile = FileGenerator.generateBodyFile(bodySize);

                    assert tempFile != null;
                    tempFile.deleteOnExit();
                    builder.POST(HttpRequest.BodyPublishers.ofByteArray(Files.readAllBytes(tempFile.toPath())));
                    break;
                }

                builder.POST(HttpRequest.BodyPublishers.noBody());
                break;
            case PUT:
                if (bodyFile != null) {
                    builder.PUT(HttpRequest.BodyPublishers.ofFile(bodyFile.toPath()));
                    break;
                } else if (bodySize > 0) {
                    File tempFile = FileGenerator.generateBodyFile(bodySize);

                    assert tempFile != null;
                    tempFile.deleteOnExit();
                    builder.PUT(HttpRequest.BodyPublishers.ofFile(tempFile.toPath()));
                    break;
                }
                builder.PUT(HttpRequest.BodyPublishers.noBody());
                break;
            case DELETE:
                builder.DELETE();
                break;
            default:
                builder.GET();
                break;
        }

        if (headers != null) {
            headers.forEach(builder::setHeader);
        }

        HttpRequest request = builder.build();

        return new HTTPStressTest(duration, frequency, expected_status, request, verbose);
    }
}
