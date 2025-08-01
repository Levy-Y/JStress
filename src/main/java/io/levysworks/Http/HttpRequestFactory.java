package io.levysworks.Http;

import io.levysworks.Http.Enums.HTTPRequestMethods;
import io.levysworks.Utils.FileGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Map;
import java.util.Objects;

public class HttpRequestFactory {
    private HttpStressTestConfiguration configuration;

    public HttpRequestFactory(HttpStressTestConfiguration configuration) {
        this.configuration = configuration;
    }

    private HttpRequest.BodyPublisher createBody(File file) {
        try {
            return HttpRequest.BodyPublishers.ofFile(file.toPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest createHttpRequest() {
        String url = "http://" + configuration.getHost() + ":" + configuration.getPort();

        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url));

        File bodyFile = configuration.getBodyFile();
        Integer bodySize = configuration.getBodySize();
        HttpRequest.BodyPublisher body = null;

        if (configuration.getMethod() == HTTPRequestMethods.POST || configuration.getMethod() == HTTPRequestMethods.PUT) {
            body = createBody(configuration.getBodyFile() != null
                    ? configuration.getBodyFile()
                    : Objects.requireNonNull(FileGenerator.generateBodyFile(configuration.getBodySize()))
            );
        }


        switch (configuration.getMethod()) {
            case HEAD:
                builder.HEAD();
                break;
            case POST:
                if (bodyFile != null) {
                    builder.POST(body);
                    break;
                } else if (bodySize > 0) {

                    builder.POST(body);
                    break;
                }

                builder.POST(HttpRequest.BodyPublishers.noBody());
                break;
            case PUT:
                if (bodyFile != null) {
                    builder.PUT(body);
                    break;
                } else if (bodySize > 0) {
                    File tempFile = FileGenerator.generateBodyFile(bodySize);

                    assert tempFile != null;
                    tempFile.deleteOnExit();
                    builder.PUT(body);
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

        Map<String, String> headers = configuration.getHeaders();
        if (headers != null) {
            headers.forEach(builder::setHeader);
        }

        return builder.build();
    }

}
