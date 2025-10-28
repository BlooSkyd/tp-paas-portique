#!/bin/bash

TOPIC=""
SERVER="localhost:9092"

parse_arg() {
    local ARG="$1"
    case "$ARG" in
        --topic=*)
            TOPIC="${ARG#*=}"
            ;;
        --server=*)
            SERVER="${ARG#*=}"
            ;;
        *)
            ;;
    esac
}

[ ! -z "$1" ] && parse_arg "$1"
[ ! -z "$2" ] && parse_arg "$2"

if [ -z "$TOPIC" ] || [ -z "$SERVER" ]; then
    echo "Missing argument --topic or --server if no --server then its set to localhost:9092"
    exit 1
fi

/opt/kafka/bin/kafka-console-consumer.sh --topic "$TOPIC" --from-beginning --bootstrap-server "$SERVER"
