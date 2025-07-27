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

    public static HTTPTestReport getReport() {
        return report;
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

        public String getTarget() {
            return target;
        }

        public int getPort() {
            return port;
        }

        public int getThreads() {
            return threads;
        }

        public int getDuration() {
            return duration;
        }

        public int getFrequency() {
            return frequency;
        }

        public int getRequests() {
            return requests;
        }

        public int getSuccessfulRequests() {
            return successful_requests;
        }

        public int getFailedRequests() {
            return failed_requests;
        }
    }
}
