#!/bin/bash

rm -rf docs/api/*

javadoc \
  --source-path src/main/java \
  -d docs/api \
  -protected \
  --module org.klojang.check \
  -link https://docs.oracle.com/en/java/javase/18/docs/api/index.html \
  -windowtitle 'Klojang Check' \
  org.klojang.check \
  org.klojang.check.aux \
  org.klojang.check.fallible \
  org.klojang.check.relation
