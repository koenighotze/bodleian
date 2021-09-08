#!/usr/bin/env bash

set -Eeuo pipefail

: "${GITHUB_REPOSITORY?'Expected env var GITHUB_REPOSITORY not set'}"
: "${GITHUB_SHA?'Expected env var GITHUB_SHA not set'}"
: "${GITHUB_REF?'Expected env var GITHUB_REF not set'}"
: "${GCP_PROJECT_ID?'Expected env var GCP_PROJECT_ID not set'}"

echo "::group:: Pushing image ${IMAGE_NAME} to GCR"

if [[ "$GITHUB_REF" = "refs/tags/" ]]; then
  GIT_TAG=${GITHUB_REF/refs\/tags\/}
  IMAGE_NAME="$GITHUB_REPOSITORY:$GITHUB_SHA"
  GCR_IMAGE_NAME="eu.gcr.io/${GCP_PROJECT_ID}/$GITHUB_REPOSITORY:${GIT_TAG}"

  docker tag "$IMAGE_NAME" "$GCR_IMAGE_NAME"
  docker push "$GCR_IMAGE_NAME"

  echo "::set-output name=image_name::$GCR_IMAGE_NAME"
else
  echo "Not a tagged version, will not push to GCR"
fi

echo "::endgroup::"