//package io.levysworks.tests;
//
//import io.levysworks.Subcommands.TestCommand;
//import org.junit.Test;
//import picocli.CommandLine;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//import static org.junit.Assert.assertEquals;
//
//public class TestCommandTest {
//    TestCommand testCommand = new TestCommand();
//    CommandLine cmd =  new CommandLine(testCommand);
//
//    @Test
//    public void testTestHTTPCommandWithRequiredArguments() throws Exception {
//        StringWriter sw = new StringWriter();
//        cmd.setOut(new PrintWriter(sw));
//
//        int exitCode = cmd.execute("HTTP", "-a", "192.168.1.100", "-p", "8080");
//        assertEquals(0, exitCode);
//    }
//}
