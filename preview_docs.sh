#!/bin/bash

# Build the Dokka docs.
./assemble_docs.sh

# Build the main website
mkdocs build

cd site || return

echo "==========================================="
echo "Open http://localhost:8000/ in your browser"
echo "==========================================="

python3 -m http.server 8000