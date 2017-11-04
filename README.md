# usb-multimedia-digital-ecosystem
Simón Bolívar University - CI4822 - Operating Systems III - Multimedia Digital Ecosystem

## Members

* María Andreina Loriente
* Vicente Santacoloma
* Rosannys Ruíz

## Structure

| File                            | Description                                                                         |
|---------------------------------|-------------------------------------------------------------------------------------|
| MultimediaSharerAgent.java      | Multimedia Digital Ecosystem agent                                                  |
| OfferMultimediaBehaviour.java   | Agent behavior to offer multimedia content                                          |
| RequestMultimediaBehaviour.java | Agent behavior to request multimedia content to an agent                            |
| SearchMultimediaBehaviour.java  | Agent behavior to search multimedia content associated with a pattern on all agents |
| ShareMultimediaBehaviour.java   | Agent behavior to share multimedia content                                          |
| MultimediaSharerGUI.java        | GUI to share, remove and search for multimedia content by a search pattern          |
| MultimediaRequestGUI.java       | GUI to select the desired multimedia content                                        |
| FileChooser.java                | GUI to select a file in the directory tree                                          |

## Configuration

### With Netbeans

Add the file `jade.jar` to the IDE

### Without Netbeans

Copy the folder `JADE-all-4.2.0` in the user's home directory.

## Build

### With Netbeans

```bash
build MDE project
```

### Without Netbeans

```bash
cd [Project Name]/src/mde
../../../build-JADE.bash
```

## Execution Instructions

### With Netbeans:

```bash
cd [Project Name]
./run-JADE-Main-Container.bash

config Netbeans run properties with:
* **Main Class:** jade.Boot
* **Arguments:** -container -local-host localhost -local-port 3000 -host localhost Agent1:mde.MultimediaSharerAgent;Agent2:mde.MultimediaSharerAgent

run MDE project
```

### Without Netbeans:

```bash
cd [Project Name]
./run-JADE-Main-Container.bash
./run-JADE-Agents.bash -a [agents] -h [host] -l [localhost] -p [port]
```

* **agents:** number of agents to run. `2` by default
* **host:** host main container. `localhost` by default.
* **localhost:** application host. `localhost` by default.
* **port:** application port. `3000` by default.
