package io.levysworks.Subcommands;

import io.levysworks.Enums.HTTPRequestTypes;
import io.levysworks.Interfaces.BaseTest;
import io.levysworks.StressTests.HTTPStressTestBuilder;
import io.levysworks.Utils.AddressValidator;
import io.levysworks.Utils.TestExecutor;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "http", mixinStandardHelpOptions = true, description = "Runs customizable HTTP tests against a host")
public class HttpTestCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-a", "--address", "--host"}, description = "The host to run the stress stress test against", required = true)
    private String host;

    @CommandLine.Option(names = {"-p", "--port"}, description = "The port of the host to stress test", required = true)
    private Integer port;

    @CommandLine.Option(names = {"-m", "--method"}, description = "The HTTP request method to use in the test", defaultValue = "GET")
    private HTTPRequestTypes method;

    @CommandLine.Option(names = "--header", description = "Header to use in the stress tests (Pass --header for each header key-value pair)")
    private Map<String, String> headers;

    @CommandLine.Option(names = "--body-size", description = "Size of generated file to try to send in each request (in bytes)")
    private Integer bodySize;

    @CommandLine.Option(names = "--body-file", description = "File to try to send in each request")
    private File bodyFile;

    @CommandLine.Option(names = "--expect-status", description = "Status code to expect the server to respond with")
    private Integer expectStatus;

    @CommandLine.Option(names = {"-d", "--duration"}, description = "The duration of the stress test in seconds", defaultValue = "5")
    private int duration;

    @CommandLine.Option(names = {"-f", "--frequency"}, description = "How frequently send the request per second", defaultValue = "2")
    private int frequency;

    @CommandLine.Option(names = {"-t", "--threads"}, description = "The amount of parallel threads to run this test on (CAUTION! This can cause high CPU usage)", defaultValue = "1")
    private int parallel;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Whether or not to show more detailed debug messages")
    boolean[] verbose;

    @Override
    public Integer call() throws IOException {
        if (!AddressValidator.isAddressValid(host)) {
            System.err.println("Invalid host address: " + host);
            return 1;
        }

        if (!AddressValidator.isPrivateAddress(host)) {
            System.err.println("Host address can only be in the private range, to avoid misuse of the tool.");
            return 1;
        }

        if (bodySize != null && bodyFile != null && verbose != null) {
            System.out.println("Both --body-size and --body-file were provided, ignoring body size..");
        }

        if (!(method.equals(HTTPRequestTypes.POST) || method.equals(HTTPRequestTypes.PUT)) && (bodySize != null || bodyFile != null) && verbose != null) {
            System.out.println("Selected request method cannot have a file body, aborting..");
            return 1;
        }

        BaseTest test = new HTTPStressTestBuilder()
                        .host(host)
                        .port(port)
                        .method(method)
                        .headers(headers)
                        .bodyFile(bodyFile)
                        .bodySize(bodySize)
                        .expected_status(expectStatus)
                        .frequency(frequency)
                        .duration(duration)
                        .verbose(verbose != null && verbose[0])
                        .build();

        TestExecutor.stressTest(test, host, port, parallel, duration, frequency, (verbose != null && verbose.length > 0));
        return 0;
    }
}
