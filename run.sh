#!/bin/bash

set -e

./mvnw clean package

java --enable-preview -jar ./target/graphino-0.1.0-SNAPSHOT.jar