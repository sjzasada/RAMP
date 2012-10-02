#! /bin/env bash

export CLASSPATH=$PATH/lib/commons-codec/commons-codec-1.3.jar:$PATH/lib/djep-1.0.0.jar:$PATH/lib/jade.jar:$PATH/lib/jep-2.3.0.jar:$PATH/src/

java jade.Boot -gui -host localhost -port 9001  seller:uk.ac.ucl.chem.ccs.ramp.resource.ResourceAgent("10")