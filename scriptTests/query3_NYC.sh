#!/bin/bash
#
#

BASE_PATH="../client/src/main/java/ar/edu/itba/pod/tpe2/client"
ADDRESS='localhost:50058'
CITY='NYC'
IN_PATH='../example/data'
OUT_PATH='../output'
N='4'

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"
if ! pushd "${SCRIPT_DIR}" &> /dev/null; then
        >&2 echo "Script directory not found (??): '$SCRIPT_DIR'"
        exit 1
fi

bash query3.sh -Daddresses="$ADDRESS" -DinPath="$IN_PATH" -DoutPath="$OUT_PATH" -Dcity="$CITY" -Dn="$N"