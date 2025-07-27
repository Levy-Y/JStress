package io.levysworks.Subcommands;

import java.util.concurrent.Callable;

public class StatsCommand implements Callable<Integer> {
    public Integer call() throws Exception {
        System.out.println("Stats");
        return 0;
    }
}
