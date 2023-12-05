#!/usr/bin/env bash

# We manually run assemble & dokka before publishAllPublicationsToMavenCentral. If we only
# run publishAllPublicationsToMavenCentral,
# the assemble and dokka tasks are run interleaved on each module, which can cause
# connection timeouts to Sonatype (since we need to wait for assemble+dokka to finish).
# By front-loading the assemble+dokka tasks, the upload is much quicker.
./gradlew assembleRelease dokkaHtml publishAllPublicationsToMavenCentral --no-configuration-cache -PRELEASE_SIGNING_ENABLED=true
