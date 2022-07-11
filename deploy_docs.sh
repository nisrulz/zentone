#!/bin/bash

# Build the Dokka docs.
./assemble_docs.sh

# Copy outside files into the docs folder.
cp Changelog.md docs/changelog.md

# Deploy to Github pages.
mkdocs gh-deploy

# Clean up.
rm docs/changelog.md
rm -r docs/api
rm -r site

echo ""
echo "==========================================="
echo "✅ Docs published!"
echo "==========================================="
echo ""
