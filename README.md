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

