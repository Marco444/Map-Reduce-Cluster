#!/bin/bash

# JAVA_OPTS="-Djava.security.debug=access -Djava.security.manager -Djava.security.policy=/$PATH_TO_CODE_BASE/java.policy -Djava.rmi.server.useCodebaseOnly=false"

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"
if ! pushd "${SCRIPT_DIR}" &> /dev/null; then
        >&2 echo "Script directory not found (??): '$SCRIPT_DIR'"
        exit 1
fi

PATH_TO_CODE_BASE="$(pwd)"
MAIN_CLASS="ar.edu.itba.pod.tpe2.server.Server"

java $JAVA_OPTS "$@" -cp 'lib/jars/*' "$MAIN_CLASS"

# Exit script directory
popd &> /dev/null || exit 0

