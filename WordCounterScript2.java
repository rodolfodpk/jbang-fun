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

    @CommandLine.Option(names = {"-b", "--buffer"}, description = "Characters buffer in MB", defaultValue = "1")
    private int charBuffersSizeMB;

    public static void main(String... args) {
        int exitCode = new CommandLine(new WordCounterScript2()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        var startTime = System.nanoTime();
        System.out.println("Target file " + targetFile + " target word " + targetWord + " buffer size " + charBuffersSizeMB);
        try {
            var bufferSize = charBuffersSizeMB * 1024 * 1024 / 2;
            var reader = new BufferedWordCounter();
            var count = reader.countWordOnFile(targetWord, targetFile, bufferSize);
            System.out.println("The word '" + targetWord + "' occurs " + count + " times.");
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            var endTime = System.nanoTime();
            var elapsed = endTime - startTime;
            System.out.println("Elapsed time in milliseconds: " + elapsed / 1000000);
        }
    }


    private static class BufferedWordCounter {

        public Long countWordOnFile(String targetWord, String targetFile, int bufferSize) {
            long wordCount = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(targetFile), bufferSize)) {
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
                throw new RuntimeException(e);
            }
            return wordCount;
        }
    }
}
