package io.levysworks.Reports;


public class HTTPTestReportManager {
    private static HTTPTestReport report;

    public static void startReport(String host, int port, int threads, int duration, int frequency) {
        report = new HTTPTestReport(host, port, threads, duration, frequency, 0, 0, 0);
    }

    public static void addData(Integer requests, Integer successful_request, Integer failed_request) {
        report.requests += requests;
        report.successful_requests += successful_request;
        report.failed_requests += failed_request;
    }

    public static void print() {
        System.out.printf("Target: http://%s:%s\n", report.target, report.port);
        System.out.printf("Duration: %ss  | Threads: %s  | Frequency: %s req/sec\n", report.duration, report.threads, report.frequency);
        System.out.printf("Requests sent: %d\n", report.requests);
        System.out.printf("Successful:    %d\n", report.successful_requests);
        System.out.printf("Failed:        %d\n", report.failed_requests);
    }

    public static class HTTPTestReport {
        private String target;
        private int port;
        private int threads;
        private int duration;
        private int frequency;
        private int requests;
        private int successful_requests;
        private int failed_requests;

        private HTTPTestReport(String target, int port, int threads, int duration, int frequency, int requests, int successful_requests, int failed_requests) {
            this.target = target;
            this.port = port;
            this.threads = threads;
            this.duration = duration;
            this.frequency = frequency;
            this.requests = requests;
            this.successful_requests = successful_requests;
            this.failed_requests = failed_requests;
        }
    }
}
