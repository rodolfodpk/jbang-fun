///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.7.6

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

@Command(name = "WordCounterScript2", mixinStandardHelpOptions = true, version = "WordCounterScript2 0.1",
        description = "WordCounterScript2")
class WordCounterScript2 implements Callable<Integer> {

    @CommandLine.Option(names = {"-f", "--file"}, description = "Target file name", defaultValue = "data-file.txt")
    private String targetFile;

    @CommandLine.Option(names = {"-w", "--word"}, description = "Target word", defaultValue = "PEACE")
    private String targetWord;

    public static void main(String... args) {
        int exitCode = new CommandLine(new WordCounterScript2()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        var startTime = System.nanoTime();
        System.out.println("Target file " + targetFile + " target word " + targetWord);
        var reader = new WordCounterImplBuffered();
        var count = reader.countWordOnFile(targetWord, targetFile);
        var endTime = System.nanoTime();
        var elapsed = endTime - startTime;
        System.out.println("The word '" + targetWord + "' occurs " + count + " times.");
        System.out.println("Elapsed time in milliseconds: " + elapsed / 1000000);
        return 0;
    }


    public static class WordCounterImplBuffered {

        public Long countWordOnFile(String targetWord, String targetFile) {
            long wordCount = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line);
                    while (st.hasMoreTokens()) {
                        if (targetWord.equals(st.nextToken())) {
                            wordCount++;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return wordCount;
        }
    }
}
