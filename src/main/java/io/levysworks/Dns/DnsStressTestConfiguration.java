package io.levysworks.Dns;

import io.levysworks.Dns.Enums.DnsQueryTypes;

public class DnsStressTestConfiguration {
    private String resolver;
    private Integer port;
    private String fqdn;
    private DnsQueryTypes queryType;
    private String address;
    private Integer duration;
    private Integer frequency;
    private Integer threadCount;
    private Boolean verbose;

    public DnsStressTestConfiguration(String resolver, Integer port, String fqdn, DnsQueryTypes queryType, String address, Integer duration, Integer frequency, Integer threadCount, Boolean verbose) {
        this.resolver = resolver;
        this.port = port;
        this.fqdn = fqdn;
        this.queryType = queryType;
        this.address = address;
        this.duration = duration;
        this.frequency = frequency;
        this.threadCount = threadCount;
        this.verbose = verbose;
    }

    public String getResolver() {
        return resolver;
    }

    public Integer getPort() {
        return port;
    }

    public String getFQDN() {
        return fqdn;
    }

    public DnsQueryTypes getQueryType() {
        return queryType;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getVerbose() {
        return verbose;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public Integer getThreadCount() {
        return threadCount;
    }
}
