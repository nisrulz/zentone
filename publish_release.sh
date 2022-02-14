#!/usr/bin/env bash

./gradlew publish --no-daemon --no-parallel
./gradlew closeAndReleaseRepository

