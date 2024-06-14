#!/bin/bash
#
#

ADDRESS='localhost:5701'
CITY='CHI'
IN_PATH="$(pwd)/../example/data"
OUT_PATH="$(pwd)/../output"
QUERIES_PATH="$(pwd)/../client/target/hazelcast-com-client-2024.1Q/"

SCRIPT_DIR="$(dirname "${BASH_SOURCE[0]}")"
if ! pushd "${SCRIPT_DIR}" &> /dev/null; then
        >&2 echo "Script directory not found (??): '$SCRIPT_DIR'"
        exit 1
fi

bash "$QUERIES_PATH"/query5.sh -Daddresses="$ADDRESS" -DinPath="$IN_PATH" -DoutPath="$OUT_PATH" -Dcity="$CITY"