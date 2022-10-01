#!/bin/bash

javadoc \
  --source-path src/main/java \
  -d docs/api \
  -protected \
  --module nl.naturalis.common \
  -link https://docs.oracle.com/en/java/javase/18/docs/api/index.html \
  -windowtitle 'naturalis-common' \
  nl.naturalis.check \
  nl.naturalis.common \
  nl.naturalis.common.collection \
  nl.naturalis.common.exception \
  nl.naturalis.common.function \
  nl.naturalis.common.invoke \
  nl.naturalis.common.util
