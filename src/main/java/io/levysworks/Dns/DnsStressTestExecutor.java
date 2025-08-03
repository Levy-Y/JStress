package io.levysworks.Dns;

import org.xbill.DNS.Address;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DnsStressTestExecutor {
    private final DnsStressTestConfiguration configuration;
    private final Map<String, Thread> threadMap = Collections.synchronizedMap(new HashMap<>());
    private Long startTime;

    public DnsStressTestExecutor(DnsStressTestConfiguration configuration) {
        this.configuration = configuration;
    }

    Runnable TesterThread = new Runnable() {
        @Override
        public void run() {
            String fqdn = configuration.getFQDN();
            String address = configuration.getAddress();

            long interval = 1000 / configuration.getFrequency();

            String id = Thread.currentThread().getName();

            if (configuration.getVerbose()) {
                System.out.printf("Thread: %s; Starting DNS stress test\n", id);
            }

            while (System.currentTimeMillis() - startTime <= configuration.getDuration() * 1000) {
                if (fqdn != null) {
                    try {
                        InetAddress addr = Address.getByName(fqdn);

                        System.out.println(fqdn + " - " + addr.getHostAddress());

                    } catch (UnknownHostException ex) {
                        System.err.println("Could not resolve address " + fqdn);
                    }
                } else if (address != null) {

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
            String id = "DnsThread_" + i;

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

    }
}
