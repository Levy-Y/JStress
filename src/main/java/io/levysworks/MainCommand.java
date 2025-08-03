package io.levysworks;

import io.levysworks.Subcommands.DnsCommand;
import io.levysworks.Subcommands.HttpCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "jstress", mixinStandardHelpOptions = true, version = "v1.0-ALPHA", description = "Main command of jstress", subcommands = { HttpCommand.class, DnsCommand.class }, synopsisSubcommandLabel = "COMMAND")
public class MainCommand {
    public static void main(String[] args) throws Exception {
        CommandLine cmd = new CommandLine(new MainCommand()).setCaseInsensitiveEnumValuesAllowed(true);

        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }
}
