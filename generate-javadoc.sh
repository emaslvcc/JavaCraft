#!/bin/bash

# Define directory to generate documentation for
SCRIPT_DIR="$(dirname -- "$(readlink -f -- "$0")")"

# Use javadoc to generate documentation
javadoc -private --show-members private --show-types private -d "$SCRIPT_DIR"/um-submission/docs/html -sourcepath "$SCRIPT_DIR" JavaCraft.java
