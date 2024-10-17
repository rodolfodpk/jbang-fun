# Java word count

## Requirement
* Docker

## Build

`docker build -t my-jbang-java21-image .`

## Running 

`docker run -m 512m --rm my-jbang-java21-image grep -o 'PEACE' data-file.txt | wc -l`

`docker run -m 512m --rm my-jbang-java21-image java -jar WordCounterScript1-fatjar.jar`

`docker run -m 512m --rm my-jbang-java21-image java -jar WordCounterScript2-fatjar.jar`

`docker run -m 512m --rm my-jbang-java21-image java -jar WordCounterScript3-fatjar.jar`

## Notes

* The requirements: 4GB file, 4GB of RAM and 40GB of disk.
* In order to accelerate my manual tests, I'm running a 1GB file with 512MG of memory.
* WordCounterScript2 seems to be the fastest implementation
* I made use of IntelliJ AI boot 
