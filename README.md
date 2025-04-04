# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)


my sequence diagram https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5kvDrco67F8H5LCBswAucBaymqKDlAgB48rC+6Hqi6KxNiCaGC6YZumSFIGrS5ajiaRLhhaHLcryBqCsKMCvmIrrSkml5weUkRgD4tysrR2iOs6bFOvKMAAGqUEgABmYRSQxJGjDA5wwGB7yspsYAgKkAmJgSeEsh6uqZLG-rTiGzHmpGHLRjAxnxnKOHJtBJaoTy2a5pg-4grBJRXAM8ljhOXzTrOzbjq2fTfleXmFNkPYwP2g69H5I6jAuk59MFjahYFS6cKu3h+IEXgoOge4Hr4zDHukmSYLFF5FNQ17SAAoruzX1M1zQtA+qhPt0mVzu2v5nECJbGdASAAF7xIkpQDegHlOcN2EIeVvooWtYDoRiWH2RqemkjA5JgMZAZBiFaBkUybqUVyPIxkGdFhPNaChuRN2sQ1wnwZx3FoKytn8dh+3vfpRgoNwRlBrCgPyFdZoRoUlrSBDFJqDZj1A3tn2eeUqEVW5CB5ktnY-qUPSRY10VdjkYClAlQ65SungFRukK2ru0IwAA4qOrJVaetXnsw3nXtz7VdfYo79edWWvT+bK4xjfoTdNBCzS9i2jct9kIdCKHQttmHaThumg4dx2nS98MUZZd28rDwBCs9suDeZEbeeCv08crwZY3BIPXWDyCxLzowaO7t3URwrJhygDqR59K3plxPtx4KPKKTAIAQMqKDgE+JudrjnOxITxPa6TUVXFMUvh+MlT9HXKAAJLSA3ACMvYAMwACxPCemQGhWExfDoCCgA2w-AaPTzNwAcqOo97DAjSU8cn11XT8UDt0pa13zDcVE3o5t53Pf91Mg-6v5KxjxPIBT7fs8H6Mi+jMvq9M-l66BNg3HYG4PAQyhg44pGqmeWmbIyaVFqA0SW0tgiu3QEOBeo514dkctrco40oBTRmmgOayDnxJFLH0NBqVPiYigpXT2IlPR6lhHAEBRssRF1wubd0R0KRW2ITbD6SMqL3V9k9RSxC3pBwsnQn6FI-oA0xvIdhZtJFcIYZkOOsIKEoH4SxQR5RmFekyKyZuLR9C9XsP9I6o4YBIBXInaRHFZE+2bjYlcwMcZLX0SwtQ7lPJVypuTV+rd26lC7n3GAGDqZb3HAzXoQSz7lDCb3CJZg8os1-gESwENELJBgAAKQgJnMBARx6TyFlA0WaZqiUjvC0ZuMs6xyyHIA4AWSoBwFztAWYzc26RMVp432qsCFEMaXOPeZCWltI6YhKAKwADqLAW4dRaAAIV3AoOAABpGY5DT4hKSdQrWqZoG6xgAAK0KWgWEBTXIoDRDtJRMgDpcMttDa2EiEZR2EY7Z2YjRkLXsUJL2TjLGO0ee7Uox0NE9OkDoiyejo481HKImFHzbYOO9pY9O-tvqBwRqUPwWh1GjlhJMyg0yukwrhYjdkjiWnqj9IYTS0oXGxDwQ4dhWDUzlBuWgcuRyYKVPJn0zewt6a716N-dJhUAheFaV2L0sBgDYEAYQAh4DBbRKFZUFqbUOpdWMENfp2C0AQEUlAewPhRgYFZJAZSZiWgUwFZgoF8oQDcDwLCXaAcOEqI9B6uE1LbqIEVbxDoLRjItDAXZH1XLgSlBDXgflfik7V1LCK04W8+wSqdcuTAQA

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
