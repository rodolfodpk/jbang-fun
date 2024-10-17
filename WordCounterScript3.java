///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.7.6
//DEPS io.vertx:vertx-core:4.5.10

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.parsetools.RecordParser;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

@Command(name = "WordCounterScript3", mixinStandardHelpOptions = true, version = "WordCounterScript3 0.1",
        description = "WordCounterScript3")
class WordCounterScript3 implements Callable<Integer> {

    @CommandLine.Option(names = {"-f", "--file"}, description = "Target file name", defaultValue = "data-file.txt")
    private String targetFile;

    @CommandLine.Option(names = {"-w", "--word"}, description = "Target word", defaultValue = "PEACE")
    private String targetWord;

    public static void main(String... args) {
        int exitCode = new CommandLine(new WordCounterScript3()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws InterruptedException {
        var startTime = System.nanoTime();
        System.out.println("Target file " + targetFile + " target word " + targetWord);
        var vertx = Vertx.vertx();
        var reader = new WordCounterImplVertx(vertx);
        var latch = new CountDownLatch(1);
        reader.countWordOnFile(targetWord, targetFile)
                .onSuccess(count -> {
                    System.out.println("The word '" + targetWord + "' occurs " + count + " times.");
                    latch.countDown(); // release the latch when the count is available
                })
                .onFailure(err -> {
                    err.printStackTrace();
                    latch.countDown(); // release the latch even in case of an error
                });
        latch.await();
        var endTime = System.nanoTime();
        var elapsed = endTime - startTime;
        System.out.println("Elapsed time in milliseconds: " + elapsed / 1000000);
        return 0;
    }

    public static class WordCounterImplVertx {

        private final Vertx vertx;
        private long wordCount = 0;

        public WordCounterImplVertx(Vertx vertx) {
            this.vertx = vertx;
        }

        public Future<Long> countWordOnFile(String targetWord, String targetFile) {
            Promise<Long> promise = Promise.promise();
            vertx.fileSystem().open(targetFile, new OpenOptions().setRead(true), ar -> {
                if (ar.succeeded()) {
                    var file = ar.result();
                    RecordParser parser = RecordParser.newFixed(1024 * 1024, file); // 1 MB chunk size
                    StringBuilder leftover = new StringBuilder();
                    parser.handler(buffer -> {
                        String chunk = leftover + buffer.toString();
                        wordCount += countInChunk(chunk, targetWord);
                        leftover.setLength(0);
                        leftover.append(chunk.substring(Math.max(0, chunk.length() - targetWord.length() + 1)));
                    });
                    parser.endHandler(v -> {
                        if (leftover.length() > 0) {
                            wordCount += countInChunk(leftover.toString(), targetWord);
                        }
                        promise.complete(wordCount); // Complete the Promise
                        file.close();
                    });
                    parser.exceptionHandler(promise::fail);
                } else {
                    promise.fail(ar.cause()); // Fail the Promise on failure
                }
            });
            return promise.future();
        }

        private long countInChunk(String chunk, String word) {
            long count = 0;
            int index = 0;

            while ((index = chunk.indexOf(word, index)) != -1) {
                count++;
                index += word.length();
            }

            return count;
        }
    }
}
