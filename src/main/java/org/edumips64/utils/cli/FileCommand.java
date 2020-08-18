package org.edumips64.utils.cli;

import org.edumips64.core.CPU;
import org.edumips64.core.parser.ParserMultiException;
import org.edumips64.utils.CurrentLocale;
import org.edumips64.utils.io.ReadException;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;

import java.io.File;


@Command(name = "file", resourceBundle = "CliMessages")
public class FileCommand implements Runnable {

    @ParentCommand
    private Cli cli;

    @Option(names={"-h","--help"}, usageHelp=true, descriptionKey = ".help.usage.description")
    private boolean help;

    @Parameters(arity = "1", paramLabel = "file_to_run", descriptionKey = "file.param")
    private File file;

    @Override
    public void run() {
        String absoluteFilename = file.getAbsolutePath();
        try {
            if (canLoadFile()) {
                cli.getParser().parse(absoluteFilename);
                cli.getDinero().setDataOffset(cli.getMemory().getInstructionsNumber() * 4);
                cli.getCPU().setStatus(CPU.CPUStatus.RUNNING);
                printLoadedFileMsg(absoluteFilename);
            } else {
                printCannotLoad();
            }
        } catch (ParserMultiException | ReadException e) {
            printLoadFailedMsg(absoluteFilename);
        }
    }

    private boolean canLoadFile() {
        return cli.getCPU().getStatus() == CPU.CPUStatus.HALTED || cli.getCPU().getStatus() == CPU.CPUStatus.READY;
    }

    private void printCannotLoad() {
        System.out.println(CurrentLocale.getString("CLI.FILE.CANT.LOAD"));
    }

    private void printLoadedFileMsg(String fileName) {
        System.out.printf(CurrentLocale.getString("CLI.FILE.LOADED"), fileName);
        System.out.println();
    }

    private void printLoadFailedMsg(String fileName) {
        System.out.printf(CurrentLocale.getString("CLI.FILE.NOT.LOADED"), fileName);
        System.out.println();
    }
}
