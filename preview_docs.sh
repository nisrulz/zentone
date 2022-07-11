#!/bin/bash

# Build the Dokka docs.
./assemble_docs.sh

# Copy outside files into the docs folder.
cp Changelog.md docs/changelog.md

# Build the main website
mkdocs build

cd site

echo "==========================================="
echo "Open http://localhost:8000/ in your browser"
echo "==========================================="

python3 -m http.server 8000