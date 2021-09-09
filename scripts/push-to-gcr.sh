#!/usr/bin/env bash

set -Eeuo pipefail

: "${GITHUB_REPOSITORY?'Expected env var GITHUB_REPOSITORY not set'}"
: "${GITHUB_SHA?'Expected env var GITHUB_SHA not set'}"
: "${GITHUB_REF?'Expected env var GITHUB_REF not set'}"
: "${GCP_PROJECT_ID?'Expected env var GCP_PROJECT_ID not set'}"

gcloud auth configure-docker

if [[ "$GITHUB_REF" = refs/tags/* ]]; then
  GIT_TAG=${GITHUB_REF/refs\/tags\/}
  IMAGE_NAME="$GITHUB_REPOSITORY:$GIT_TAG"
  echo "::group:: Pushing image ${IMAGE_NAME} to GCR"

  GCR_IMAGE_NAME="eu.gcr.io/${GCP_PROJECT_ID}/${IMAGE_NAME}"

  docker pull "$IMAGE_NAME"
  docker tag "$IMAGE_NAME" "$GCR_IMAGE_NAME"
  docker push "$GCR_IMAGE_NAME"

  echo "::set-output name=image_name::$GCR_IMAGE_NAME"
else
  echo "Not a tagged version, will not push to GCR"
fi

echo "::endgroup::"