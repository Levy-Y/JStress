package io.levysworks;

import io.levysworks.Subcommands.HttpTestCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "jstress", mixinStandardHelpOptions = true, version = "v1.0-ALPHA", description = "Main command of jstress", subcommands = { HttpTestCommand.class }, synopsisSubcommandLabel = "COMMAND")
public class MainCommand {
    public static void main(String[] args) throws Exception {
        CommandLine cmd = new CommandLine(new MainCommand()).setCaseInsensitiveEnumValuesAllowed(true);

        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }
}
