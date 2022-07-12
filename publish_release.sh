#!/usr/bin/env bash

# We manually run assemble & dokka before uploadArchives. If we only run uploadArchives,
# the assemble and dokka tasks are run interleaved on each module, which can cause
# connection timeouts to Sonatype (since we need to wait for assemble+dokka to finish).
# By front-loading the assemble+dokka tasks, the upload is much quicker.
./gradlew assembleRelease dokkaHtml publish closeAndReleaseRepository --no-daemon --no-parallel

