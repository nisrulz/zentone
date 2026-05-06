#!/bin/bash

# Clean any previous Dokka docs.
rm -rf docs/api

# Build the docs.
./gradlew dokkaGeneratePublicationHtml

echo ""
echo "==========================================="
echo "✅ API Docs generated!"
echo "==========================================="
echo ""
