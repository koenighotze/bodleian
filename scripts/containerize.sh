#!/usr/bin/env bash

set -Eeuo pipefail

: "${IMAGE_NAME?'Expected env var IMAGE_NAME not set'}"
: "${GITHUB_SHA?'Expected env var GITHUB_SHA not set'}"
: "${GITHUB_REF?'Expected env var GITHUB_REF not set'}"
: "${DOCKER_REGISTRY_USERNAME?}'Expected env var DOCKER_REGISTRY_USERNAME not set'}"
: "${DOCKER_REGISTRY_TOKEN?}'Expected env var DOCKER_REGISTRY_TOKEN not set'}"
: "${CONTAINER_PORTS:=8080}"

NOW=$(date -u +%Y-%m-%dT%T%z)
CONTAINER_LABELS="org.opencontainers.image.revision=${GITHUB_SHA},org.opencontainers.image.created=${NOW}"

if [[ "$GITHUB_REF" = refs/tags/* ]]; then
    GIT_TAG=${GITHUB_REF/refs\/tags\/}
fi

echo "::group:: Building image ${IMAGE_NAME}"

JIB_OPTIONS="-Djib.container.labels=${CONTAINER_LABELS}
    -Djib.to.image=${IMAGE_NAME}
    -Djib.container.ports=${CONTAINER_PORTS}
    -Djib.to.auth.username=${DOCKER_REGISTRY_USERNAME}
    -Djib.to.auth.password=${DOCKER_REGISTRY_TOKEN}"

if [[ -n "${GIT_TAG:=}" ]]; then
    JIB_OPTIONS="${JIB_OPTIONS} -Djib.to.tags=${GIT_TAG}"
fi

if [[ "$GITHUB_REF" = "refs/heads/main" ]]; then
  # shellcheck disable=SC2086
  mvn jib $JIB_OPTIONS
else
  echo "Not running on main branch, only building a tar and not pushing"

  # shellcheck disable=SC2086
  mvn jib:buildTar $JIB_OPTIONS
fi

echo "::endgroup::"