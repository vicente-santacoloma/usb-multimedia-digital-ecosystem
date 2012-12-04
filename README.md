MDE
===

###############################################################################
#                        Multimedia Digital Ecosystem                         #
###############################################################################

###############################################################################
# Members                                                                     #
###############################################################################

Maria Andreina Loriente
Vicente Santacoloma
Rosannys Ruiz

###############################################################################
# Structure                                                                   #
###############################################################################

MultimediaSharerAgent.java: 
	Multimedia Digital Ecosystem agent.

OfferMultimediaBehaviour.java: 
	Agent behavior to offer multimedia content.

RequestMultimediaBehaviour.java:
	Agent behavior to request multimedia content to an agent.

SearchMultimediaBehaviour.java:
	Agent behavior to search multimedia content associated with 
	a pattern on all agents registered in the main container.

ShareMultimediaBehaviour.java:
	Agent behavior to share multimedia content.

MultimediaSharerGUI.java:
	GUI to share, remove and search for multimedia content by a 
	search pattern.

MultimediaRequestGUI.java:
	GUI to select the desired multimedia content.

FileChooser.java:
	GUI to select a file in the directory tree.

###############################################################################
# Configuration Instructions                                                  #
###############################################################################

With Netbeans:
Add the file "jade.jar" to the IDE

Without Netbeans:
Copy the folder "JADE-all-4.2.0" in the user's home directory.

###############################################################################
# Build Instructions                                                          #
###############################################################################

With Netbeans:
build MDE project

Without Netbeans:
$ cd MDE/src/mde
$ ../../../build-JADE.bash

###############################################################################
# Execution Instructions                                                      #
###############################################################################

With Netbeans:
$ cd MDE
$ ./run-JADE-Main-Container.bash
config Netbeans run properties with:
	Main Class: jade.Boot
	Arguments: -container -local-host localhost -local-port 3000 -host localhost
			   Agent1:mde.MultimediaSharerAgent;Agent2:mde.MultimediaSharerAgent
run MDE project

Without Netbeans:
$ cd MDE
$ ./run-JADE-Main-Container.bash
$ ./run-JADE-Agents.bash -a [agents] -h [host] -l [localhost] -p [port]

agents: number of agents to run. 2 by default
host: host main container. "localhost" by default.
localhost: application host. "localhost" by default.
port: application port. 3000 by default.
