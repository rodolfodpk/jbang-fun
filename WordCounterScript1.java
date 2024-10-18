///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.7.6

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;

@Command(name = "WordCounterScript1", mixinStandardHelpOptions = true, version = "WordCounterScript1 0.1",
        description = "WordCounterScript1")
class WordCounterScript1 implements Callable<Integer> {

    @CommandLine.Option(names = {"-f", "--file"}, description = "Target file name", defaultValue = "data-file.txt")
    private String targetFile;

    @CommandLine.Option(names = {"-w", "--word"}, description = "Target word", defaultValue = "PEACE")
    private String targetWord;

    public static void main(String... args) {
        int exitCode = new CommandLine(new WordCounterScript1()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        var startTime = System.nanoTime();
        System.out.println("Target file " + targetFile + " target word " + targetWord);
        var reader = new ScannerWordCounter();
        var count = reader.countWordOnFile(targetWord, targetFile);
        var endTime = System.nanoTime();
        var elapsed = endTime - startTime;
        System.out.println("The word '" + targetWord + "' occurs " + count + " times.");
        System.out.println("Elapsed time in milliseconds: " + elapsed / 1000000);
        return 0;
    }

    private static class ScannerWordCounter {

        public Long countWordOnFile(String targetWord, String targetFile) {
            long wordCount = 0;

            try (Scanner scanner = new Scanner(new File(targetFile))) {

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    wordCount += countOccurrenceInLine(line, targetWord);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return wordCount;
        }

        private int countOccurrenceInLine(String line, String targetWord) {
            if (line == null || line.isEmpty()) {
                return 0;
            }

            String[] words = line.split("\\s+");
            int count = 0;

            for (String word : words) {
                if (word.equalsIgnoreCase(targetWord)) {
                    count++;
                }
            }

            return count;
        }
    }
}
