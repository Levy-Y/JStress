package io.levysworks.Subcommands;

import picocli.CommandLine;

@CommandLine.Command(name = "start", mixinStandardHelpOptions = true, description = "Runs different kinds of stress tests against network devices", subcommands = { HttpTestCommand.class }, synopsisSubcommandLabel = "TYPE")
public class StartCommand {}