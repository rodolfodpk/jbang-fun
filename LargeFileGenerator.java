///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.7.0

import picocli.CommandLine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "generate-file", mixinStandardHelpOptions = true, description = "Generates a large file with random data and a specified word.")
public class LargeFileGenerator implements Callable<Integer> {

    private static final int CHUNK_SIZE = 1024 * 1024; // 1 MB chunk size for efficiency
    private static final Random RANDOM = new SecureRandom(); // SecureRandom for randomness
    private static final double PROBABILITY_OF_SPACE = 0.01;
    @CommandLine.Option(names = {"-o", "--output"}, description = "Output file name", defaultValue = "data-file.txt")
    private String outputFile;
    @CommandLine.Option(names = {"-s", "--size"}, description = "Size of the file in GB", defaultValue = "1")
    private long fileSizeGB;
    @CommandLine.Option(names = {"-c", "--count"}, description = "Number of occurrences of the word", defaultValue = "100")
    private int wordCount;
    @CommandLine.Option(names = {"-w", "--word"}, description = "Word to insert", defaultValue = "PEACE")
    private String word;
    @CommandLine.Option(names = {"-d", "--delimiter"}, description = "Record delimiter", defaultValue = "\n")
    private String recordDelimiter;

    public static void main(String... args) {
        int exitCode = new CommandLine(new LargeFileGenerator()).execute(args);
        System.exit(exitCode);
    }

    private static char generateRandomChar() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return chars.charAt(RANDOM.nextInt(chars.length()));
    }

    @Override
    public Integer call() throws Exception {

        word = " " + word + " ";
        long fileSizeInBytes = fileSizeGB * 1024L * 1024L * 1024L; // Convert to bytes

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8))) {
            long remainingSize = fileSizeInBytes;
            int wordInserted = 0;

            while (remainingSize > 0) {
                // Calculate the current chunk size to write
                int currentChunkSize = (int) Math.min(CHUNK_SIZE, remainingSize);

                // Determine if the word should be inserted in this chunk
                boolean insertWord = wordInserted < wordCount && RANDOM.nextDouble() < (double) (wordCount - wordInserted) / (fileSizeInBytes / CHUNK_SIZE);

                // Generate random chunk with or without the word
                String chunk = generateRandomChunk(currentChunkSize, insertWord);

                // Write the chunk to file
                writer.write(chunk);
                writer.write(recordDelimiter);

                // Update remaining size and word insertion count
                remainingSize -= currentChunkSize;
                if (insertWord) {
                    wordInserted++;
                }
            }

            // Ensure the remaining word insertions if any are left
            while (wordInserted < wordCount) {
                String chunk = generateRandomChunk(CHUNK_SIZE, true);
                writer.write(chunk);
                writer.write(recordDelimiter);
                wordInserted++;
            }

            System.out.println("File generated successfully: " + outputFile + " with " + wordCount + " occurrences of the word '" + word + "'.");
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

    private String generateRandomChunk(int chunkSize, boolean insertWord) {
        StringBuilder chunk = new StringBuilder(chunkSize);
        for (int i = 0; i < chunkSize; i++) {
            // randomly insert a space to form words
            if (RANDOM.nextDouble() < PROBABILITY_OF_SPACE) {
                chunk.append(' ');
            } else {
                chunk.append(generateRandomChar());
            }
        }
        // Your code to insert the word into the chunk
        if (insertWord) {
            int randomIndex = RANDOM.nextInt(chunkSize);
            chunk.replace(randomIndex, randomIndex + word.length(), word);
        }
        return chunk.toString();
    }
}
