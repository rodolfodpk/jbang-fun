# Java word count

## Requirement

* Docker

## Build

This will take few minutes to generate a 1GB file.

`docker build -t my-jbang-java21-image .`

## Running

* Counting with plain Linux tools.

`docker run --cpus=1 -m 256m --rm my-jbang-java21-image time grep -o 'PEACE' data-file.txt | wc -l`

* Counting using java Scanner class.

`docker run --cpus=1 -m 256m --rm my-jbang-java21-image java -jar WordCounterScript1-fatjar.jar`

* Counting using BufferedReader using a buffer of 1MB.

`docker run --cpus=1 -m 256m --rm my-jbang-java21-image java -jar WordCounterScript2-fatjar.jar -b 1`

* Counting using BufferedReader using a buffer of 2MB.

`docker run --cpus=1 -m 256m --rm my-jbang-java21-image java -jar WordCounterScript2-fatjar.jar -b 2`

* Counting using Vertx.

`docker run --cpus=1 -m 256m --rm my-jbang-java21-image java -jar WordCounterScript3-fatjar.jar`

## Notes

* The requirements: 4GB file, 4GB of RAM and 40GB of disk.
* In order to accelerate my manual tests, I'm running the container with a 1GB file, 256MB of RAM and just 1 cpu.
* WordCounterScript2 (BufferedReader) is the fastest implementation.
* WordCounterScript3 (Vertx) is the second.
* WordCounterScript1 (Scanner) is the third.
* All implementations will show the expected result: 100 occurrences of the word 'PEACE', as a token.
* I made use of IntelliJ AI bot.
