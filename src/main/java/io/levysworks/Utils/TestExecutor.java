package io.levysworks.Utils;

import io.levysworks.Interfaces.BaseTest;
import io.levysworks.Reports.HTTPTestReportManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestExecutor {
    private static final Map<String, Thread> threadMap = new ConcurrentHashMap<>();

    public static void stressTest(BaseTest test, String host, Integer port, Integer threads, Integer duration, Integer frequency, boolean verbose) {
        for (int i = 0; i < threads; i++) {
            String id = "TesterThread_" + i;

            Thread t = new Thread(test);
            t.setName(id);
            t.start();

            if (verbose) {
                System.out.println("Created thread " + id);
            }

            threadMap.put(id, t);
        }

        System.out.println("Started stress test");
        HTTPTestReportManager.startReport(host, port, threads, duration, frequency);

        threadMap.values().forEach(t -> {
            try {
                t.join();

                if (verbose) {
                    System.out.println("Finished thread " + t.getName());
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for thread " + t.getName());
            }
        });

        HTTPTestReportManager.HTTPTestReport report = HTTPTestReportManager.getReport();
        System.out.printf("Target: http://%s:%s\n", report.getTarget(), report.getPort());
        System.out.printf("Duration: %ss  | Threads: %s  | Frequency: %s req/sec\n", report.getDuration(), report.getThreads(), report.getFrequency());
        System.out.printf("Requests sent: %d\n", report.getRequests());
        System.out.printf("Successful:    %d\n", report.getSuccessfulRequests());
        System.out.printf("Failed:        %d\n", report.getFailedRequests());
    }
}
