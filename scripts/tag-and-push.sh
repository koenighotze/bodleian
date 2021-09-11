#!/usr/bin/env bash

set -Eeuo pipefail

: "${GITHUB_SHA?'Expected env var GITHUB_SHA not set'}"
: "${GITHUB_REF?'Expected env var GITHUB_REF not set'}"
: "${TARGET_REGISTRY?'Expected env var TARGET_REGISTRY not set'}"
: "${CONTAINER_PORTS:=8080}"

gcloud auth configure-docker "${TARGET_REGISTRY}"

GIT_TAG=${GITHUB_REF/refs\/tags\/}
IMAGE_NAME="$TARGET_REGISTRY/$GITHUB_REPOSITORY:$GITHUB_SHA"
TARGET_IMAGE_NAME="$TARGET_REGISTRY/$GITHUB_REPOSITORY:$GIT_TAG"

echo "::group:: Tagging image $IMAGE_NAME as $TARGET_IMAGE_NAME"

docker pull "$IMAGE_NAME"
docker tag "$IMAGE_NAME" "$TARGET_IMAGE_NAME"
docker push "$TARGET_IMAGE_NAME"

echo "::set-output name=image-name::$TARGET_IMAGE_NAME}"

echo "::endgroup::"