#!/usr/bin/env bash
cd $(dirname $0)
test -z "$TAG" && TAG=harbor.fsa.os.univ-lyon1.fr/fsa/api:latest
docker build -t "$TAG" . && docker push "$TAG"
