package io.levysworks.Http;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpStressTestExecutor {
    private final HttpStressTestConfiguration configuration;
    private final HttpRequest request;

    private final Map<String, Thread> threadMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Integer> statuses = new ConcurrentHashMap<>();
    private AtomicInteger successful_requests = new AtomicInteger(0);
    private AtomicInteger failed_requests = new AtomicInteger(0);
    private Long startTime;

    public HttpStressTestExecutor(HttpStressTestConfiguration configuration, HttpRequest request) {
        this.configuration = configuration;
        this.request = request;
    }

    Runnable TesterThread = new Runnable() {
        @Override
        public void run() {
            Integer expected_status = configuration.getExpectedStatusCode();
            long interval = 1000 / configuration.getFrequency();

            String id = Thread.currentThread().getName();

            if (configuration.getVerbose()) {
                System.out.printf("Thread: %s; Starting HTTP stress test on %s:%d\n", id, configuration.getHost(), configuration.getPort());
            }

            while (System.currentTimeMillis() - startTime <= configuration.getDuration() * 1000) {
                try (HttpClient client = HttpClient.newHttpClient()) {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (expected_status != null && response.statusCode() != expected_status && configuration.getVerbose()) {
                        System.err.printf("Expected HTTP status code %d, got %d\n", expected_status, response.statusCode());
                    }
                    successful_requests.addAndGet(1);

                    if (statuses.containsKey(response.statusCode())) {
                        statuses.get(response.statusCode());
                    }

                } catch (IOException | InterruptedException e) {
                    if ((e.getCause() instanceof IOException) && configuration.getVerbose()) {
                        System.err.printf("Thread: %s; Interrupted. Request bounced off %s\n", id, request.uri().toString());
                    }
                    failed_requests.addAndGet(1);
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    public void execute() {
        startTime = System.currentTimeMillis();

        for (int i = 0; i < configuration.getThreadCount(); i++) {
            String id = "TesterThread_" + i;

            Thread t = new Thread(TesterThread);
            t.setName(id);
            t.start();

            if (configuration.getVerbose()) {
                System.out.println("Created thread " + id);
            }

            threadMap.put(id, t);
        }

        System.out.println("Started stress test");

        threadMap.values().forEach(t -> {
            try {
                t.join();

                if (configuration.getVerbose()) {
                    System.out.println("Finished thread " + t.getName());
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for thread " + t.getName());
            }
        });

        System.out.println("Finished stress test");
        System.out.printf("Target: http://%s:%s\n", configuration.getHost(), configuration.getPort());
        System.out.printf("Duration: %ss  | Threads: %s  | Frequency: %s req/sec\n", configuration.getDuration(), configuration.getThreadCount(), configuration.getFrequency());
        System.out.printf("Requests sent: %d\n", successful_requests.get() + failed_requests.get());
        System.out.printf("Successful:    %d\n", successful_requests.get());
        System.out.printf("Failed:        %d\n", failed_requests.get());
    }
}
