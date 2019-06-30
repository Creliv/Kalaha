FROM bigtruedata/sbt:latest

COPY . /game

WORKDIR /game

RUN sbt compile

EXPOSE 9000

ENTRYPOINT sbt run