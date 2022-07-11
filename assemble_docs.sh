#!/bin/bash

# Clean any previous Dokka docs.
rm -rf docs/api

# Build the docs.
./gradlew dokkaHtml

echo ""
echo "==========================================="
echo "✅ API Docs generated!"
echo "==========================================="
echo ""