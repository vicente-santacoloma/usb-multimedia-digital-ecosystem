# usb-multimedia-digital-ecosystem
Simón Bolívar University - CI4822 - Operating Systems III - Multimedia Digital Ecosystem

## Structure

| File                            | Description                                                                         |
|---------------------------------|-------------------------------------------------------------------------------------|
| FileChooser.java                | GUI to select a file in the directory tree                                          |
| MultimediaRequestGUI.java       | GUI to select the desired multimedia content                                        |
| MultimediaSharerAgent.java      | Multimedia Digital Ecosystem agent                                                  |
| MultimediaSharerGUI.java        | GUI to share, remove and search for multimedia content by a search pattern          |
| OfferMultimediaBehaviour.java   | Agent behavior to offer multimedia content                                          |
| RequestMultimediaBehaviour.java | Agent behavior to request multimedia content to an agent                            |
| SearchMultimediaBehaviour.java  | Agent behavior to search multimedia content associated with a pattern on all agents |
| ShareMultimediaBehaviour.java   | Agent behavior to share multimedia content                                          |

## Configuration Instructions

#### With Netbeans

Add the file `jade.jar` to the IDE

#### Without Netbeans

Copy the folder `JADE-all-4.2.0` in the user's home directory.

## Build Instructions

#### With Netbeans

```bash
build MDE [project_name]
```

#### Without Netbeans

```bash
cd [project_name]/src/mde
../../../build-JADE.bash
```

## Run Instructions

#### With Netbeans:

```bash
cd [project_name]
./run-JADE-Main-Container.bash
```

config Netbeans run properties with:
* **Main Class:** jade.Boot
* **Arguments:** -container -local-host localhost -local-port 3000 -host localhost Agent1:mde.MultimediaSharerAgent;Agent2:mde.MultimediaSharerAgent

```bash
run MDE project
```

#### Without Netbeans:

```bash
cd [project_name]
./run-JADE-Main-Container.bash
./run-JADE-Agents.bash -a [agents] -h [host] -l [localhost] -p [port]
```

* **agents:** number of agents to run. `2` by default
* **host:** host main container. `localhost` by default.
* **localhost:** application host. `localhost` by default.
* **port:** application port. `3000` by default.
