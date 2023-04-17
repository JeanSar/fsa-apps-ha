#!/usr/bin/env bash
cd $(dirname $0)
test -z "$TAG" && TAG=harbor.fsa.os.univ-lyon1.fr/fsa/worker:latest
MIRROR_SWITCH=""
test -n "$MVN_MIRROR" && MIRROR_SWITCH="--build-arg MVN_MIRROR"
docker build $MIRROR_SWITCH -t "$TAG" . && docker push "$TAG"
