#!/bin/bash

# Build the Dokka docs.
./assemble_docs.sh

# Deploy to Github pages.
mkdocs gh-deploy

# Clean up.
rm -r docs/api
rm -r site

echo ""
echo "==========================================="
echo "✅ Docs published!"
echo "==========================================="
echo ""
