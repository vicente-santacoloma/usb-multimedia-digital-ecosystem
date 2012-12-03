#!/bin/bash

INPUT_PARAMETERS=(${@})
LENGTH=${#}

agents=4
host=localhost
local_host=localhost
port=3000

for (( i = 0; i < ${LENGTH}; ++i )); do
	case ${INPUT_PARAMETERS[${i}]} in
	"-a")
		agents=${INPUT_PARAMETERS[$(expr $i + 1)]}
		echo ${INPUT_PARAMETERS[$(expr $i + 1)]};;
	"-h")
		host=${INPUT_PARAMETERS[$(expr $i + 1)]}
		echo ${INPUT_PARAMETERS[$(expr $i + 1)]};;
	"-l")
		local_host=${INPUT_PARAMETERS[$(expr $i + 1)]}
		echo ${INPUT_PARAMETERS[$(expr $i + 1)]};;
	"-p")
		port=${INPUT_PARAMETERS[$(expr $i + 1)]}
		echo ${INPUT_PARAMETERS[$(expr $i + 1)]};;
	*)
		echo "Invalid input parameter"
		exit -1;;
	esac
	i=$(expr $i + 1)
done

for (( i = 0; i < $agents; ++i )); do
	sharer_agents+="Agent"$i":mde.MultimediaSharerAgent;"
done

java -cp $HOME/JADE-all-4.2.0/JADE-bin-4.2.0/lib/jade.jar jade.Boot -container -local-host $local_host -local-port $port -host $host $sharer_agents