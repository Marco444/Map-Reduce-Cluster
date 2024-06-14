#!/bin/bash
#
#

BASE_PATH="../client/src/main/java/ar/edu/itba/pod/tpe2/client"
ADDRESS='localhost:5701'
CITY='CHI'
IN_PATH='../example/data'
OUT_PATH='../output'

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"
if ! pushd "${SCRIPT_DIR}" &> /dev/null; then
        >&2 echo "Script directory not found (??): '$SCRIPT_DIR'"
        exit 1
fi

bash query5.sh -Daddresses="$ADDRESS" -DinPath="$IN_PATH" -DoutPath="$OUT_PATH" -Dcity="$CITY"