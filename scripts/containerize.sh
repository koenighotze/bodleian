#!/usr/bin/env bash

set -Eeuo pipefail

: "${GITHUB_SHA?'Expected env var GITHUB_SHA not set'}"
: "${GITHUB_REF?'Expected env var GITHUB_REF not set'}"
: "${TARGET_REGISTRY?'Expected env var TARGET_REGISTRY not set'}"
: "${CONTAINER_PORTS:=8080}"

IMAGE_NAME="$TARGET_REGISTRY/$GITHUB_REPOSITORY:$GITHUB_SHA"

gcloud auth configure-docker eu.gcr.io

NOW=$(date -u +%Y-%m-%dT%T%z)
CONTAINER_LABELS="org.opencontainers.image.revision=${GITHUB_SHA},org.opencontainers.image.created=${NOW}"

if [[ "$GITHUB_REF" = refs/tags/* ]]; then
    GIT_TAG=${GITHUB_REF/refs\/tags\/}
fi

echo "::group:: Building image ${IMAGE_NAME}"

JIB_OPTIONS="-Djib.container.labels=$CONTAINER_LABELS
    -Djib.to.image=$IMAGE_NAME
    -Djib.container.ports=$CONTAINER_PORTS"

if [[ -n "${GIT_TAG:=}" ]]; then
    JIB_OPTIONS="$JIB_OPTIONS -Djib.to.tags=$GIT_TAG"
fi

if [[ "$GITHUB_REF" = refs/tags/* ]]; then
  # shellcheck disable=SC2086
  mvn jib:build $JIB_OPTIONS
  echo "::set-output name=image-name::$TARGET_REGISTRY/$GITHUB_REPOSITORY:$GIT_TAG}"
else
  echo "Not running on tag, only building a tar and not pushing"

  # shellcheck disable=SC2086
  mvn jib:buildTar $JIB_OPTIONS
fi

echo "::endgroup::"