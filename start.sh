#!/bin/bash

cd "$(dirname "$0")";
./gradlew composeUp && ./gradlew bootRun

