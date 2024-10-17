# Java word count

## Requirement
* Docker

## Build

`docker build -t my-jbang-java21-image .`

## Running 

`time grep -o 'PEACE' data-file.txt | wc -l`

`time docker run -m 512m --rm my-jbang-java21-image java -jar WordCounterScript1-fatjar.jar`

`time docker run -m 512m --rm my-jbang-java21-image java -jar WordCounterScript2-fatjar.jar`

`time docker run -m 512m --rm my-jbang-java21-image java -jar WordCounterScript3-fatjar.jar`

