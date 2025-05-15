#!/bin/bash

read -p "  ❓  Specify version name: " version
echo "  🚀 Publishing documentation for version $version"

# Build the Dokka docs.
./assemble_docs.sh

# Deploy to Github pages.
mike deploy --push --update-aliases "$version" latest

# Clean up.
rm -r docs/api
rm -r site

echo ""
echo "==========================================="
echo "✅ Docs published!"
echo "==========================================="
echo ""
