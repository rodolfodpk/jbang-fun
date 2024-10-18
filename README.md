# Java word count

## Requirement
* Docker

## Build

`docker build -t my-jbang-java21-image .`

## Running 

`docker run --cpus=1 -m 128m --rm my-jbang-java21-image time grep -o 'PEACE' data-file.txt | wc -l`

`docker run --cpus=1 -m 128m --rm my-jbang-java21-image java -jar WordCounterScript1-fatjar.jar`

`docker run --cpus=1 -m 128m --rm my-jbang-java21-image java -jar WordCounterScript2-fatjar.jar`

`docker run --cpus=1 -m 128m --rm my-jbang-java21-image java -jar WordCounterScript3-fatjar.jar`

## Notes

* The requirements: 4GB file, 4GB of RAM and 40GB of disk.
* In order to accelerate my manual tests, I'm running the container with a 1GB file, 128MB of RAM and just 1 cpu.
* WordCounterScript2 seems to be the fastest implementation.
* I made use of IntelliJ AI bot.
