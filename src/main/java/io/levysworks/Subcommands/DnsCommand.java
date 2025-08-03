package io.levysworks.Subcommands;

import io.levysworks.Dns.DnsStressTestConfiguration;
import io.levysworks.Dns.DnsStressTestExecutor;
import io.levysworks.Dns.Enums.DnsQueryTypes;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "dns", mixinStandardHelpOptions = true, description = "Executes customizable DNS queries")
public class DnsCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-r", "--resolver"}, description = "The DNS server to send the requests to")
    private String resolver;

    @CommandLine.Option(names = "--port", description = "A port other than the default one (used in edge cases)", defaultValue = "53")
    private Integer port;

    @CommandLine.ArgGroup(multiplicity = "1")
    DnsExclusiveMode mode;

    @CommandLine.Option(names = {"-D", "--duration"}, description = "The duration of the stress test in seconds", defaultValue = "5")
    private int duration;

    @CommandLine.Option(names = {"-F", "--frequency"}, description = "How frequently send the request per second", defaultValue = "2")
    private int frequency;

    @CommandLine.Option(names = {"-T", "--threads"}, description = "The amount of parallel threads to run this test on (CAUTION! This can cause high CPU usage)", defaultValue = "1")
    private int threadCount;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Whether or not to show more detailed debug messages")
    boolean[] verbose;

    @Override
    public Integer call() {
        DnsStressTestConfiguration configuration = new DnsStressTestConfiguration(resolver, port,
                mode.domain != null ? mode.domain : null,
                mode.dnsQueryNonDomain != null ? mode.dnsQueryNonDomain.queryType : null,
                mode.dnsQueryNonDomain != null ? mode.dnsQueryNonDomain.address : null,
                duration, frequency, threadCount,
                verbose != null);

        DnsStressTestExecutor test = new DnsStressTestExecutor(configuration);

        test.execute();

        return 0;
    }

    private static class DnsExclusiveMode {
        @CommandLine.ArgGroup(exclusive = false, multiplicity = "1")
        DnsQueryNonDomain dnsQueryNonDomain;

        @CommandLine.Option(names = {"-d", "--domain", "--fqdn"}, description = "The domain name to query the IP of")
        private String domain;
    }

    private static class DnsQueryNonDomain {
        @CommandLine.Option(names = {"-t", "--type"}, description = "The type of DNS query to send", required = true)
        private DnsQueryTypes queryType;

        @CommandLine.Option(names = {"-a", "--address"}, description = "The address to query", required = true)
        private String address;
    }
}

