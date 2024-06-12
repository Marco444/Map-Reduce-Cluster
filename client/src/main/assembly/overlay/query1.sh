#!/bin/bash

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"
if ! pushd "${SCRIPT_DIR}" &> /dev/null; then
        >&2 echo "Script directory not found (??): '$SCRIPT_DIR'"
        exit 1
fi

PATH_TO_CODE_BASE="$(pwd)"
JAVA_OPTS="-Djava.rmi.server.codebase=file://$PATH_TO_CODE_BASE/lib/jars/hazelcast-com-client-2024.1Q.jar"
MAIN_CLASS="ar.edu.itba.pod.tpe2.client.query1.TotalFinesByInfractions"

java $JAVA_OPTS "$@" -cp 'lib/jars/*' "$MAIN_CLASS"

# Exit script directory
popd &> /dev/null || exit 0