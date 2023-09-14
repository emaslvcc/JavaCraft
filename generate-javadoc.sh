#!/bin/bash

# Define directory to generate documentation for
SCRIPT_DIR="$(dirname -- "$(readlink -f -- "$0")")"

# Use javadoc to generate documentation
javadoc -private --show-members private --show-types private -d "$SCRIPT_DIR"/docs -sourcepath "$SCRIPT_DIR" *.java
