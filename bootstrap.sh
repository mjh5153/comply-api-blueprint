#!/usr/bin/env bash
set -euo pipefail

# Usage:
#   ./bootstrap.sh <github-https-or-ssh-url> [path/to/comply-api-blueprint.zip]

if [ $# -lt 1 ]; then
  echo "Usage: $0 <github-https-or-ssh-url> [zip]"; exit 1
fi

REPO_URL="$1"; ZIP_PATH="${2:-}"; DEFAULT_BRANCH="${DEFAULT_BRANCH:-main}"
log(){ printf "\033[1;34m[bootstrap]\033[0m %s\n" "$*"; }
err(){ printf "\033[1;31m[error]\033[0m %s\n" "$*" >&2; }
need(){ command -v "$1" >/dev/null 2>&1; }

EXTRACTOR=""; need unzip && EXTRACTOR="unzip"; [ -z "$EXTRACTOR" ] && need tar && EXTRACTOR="tar" || true

is_root(){ [ -f docker-compose.yml ] || [ -d fastapi_app ] || [ -d express_app ] || [ -f openapi/openapi.yaml ]; }

find_root(){
  base="$1"
  p="$(find "$base" -type f -name openapi.yaml -path '*/openapi/openapi.yaml' -not -path '*/__MACOSX/*' -print -quit 2>/dev/null || true)"
  [ -n "$p" ] && { echo "${p%/openapi/openapi.yaml}"; return 0; }
  p="$(find "$base" -type f -name docker-compose.yml -not -path '*/__MACOSX/*' -print -quit 2>/dev/null || true)"
  [ -n "$p" ] && { dirname "$p"; return 0; }
  p="$(find "$base" -type d \( -name fastapi_app -o -name express_app \) -not -path '*/__MACOSX/*' -print -quit 2>/dev/null || true)"
  [ -n "$p" ] && { dirname "$p"; return 0; }
  return 1
}

if is_root; then
  log "Using current directory as project root: $(pwd)"
else
  [ -z "${ZIP_PATH:-}" ] && [ -f comply-api-blueprint.zip ] && ZIP_PATH="comply-api-blueprint.zip"
  [ -z "${ZIP_PATH:-}" ] && { err "No project files and no ZIP provided."; exit 1; }
  [ -f "$ZIP_PATH" ] || { err "ZIP not found: $ZIP_PATH"; exit 1; }
  [ -n "$EXTRACTOR" ] || { err "Need unzip or tar to extract."; exit 1; }

  TMPDIR="$(mktemp -d)"; log "Extracting $ZIP_PATH to $TMPDIR"
  case "$EXTRACTOR" in
    unzip) unzip -q "$ZIP_PATH" -d "$TMPDIR" ;;
    tar)   tar -xf "$ZIP_PATH" -C "$TMPDIR" ;;
  esac
  ROOT_DIR="$(find_root "$TMPDIR" || true)"
  [ -z "${ROOT_DIR:-}" ] && { err "Could not locate project root inside ZIP."; exit 1; }
  cd "$ROOT_DIR"; log "Project root: $(pwd)"
fi

if [ ! -d .git ]; then git init; fi
git checkout -B "$DEFAULT_BRANCH" >/dev/null 2>&1 || git checkout -b "$DEFAULT_BRANCH"
git add .
git diff --cached --quiet || git commit -m "Comply API MVP scaffold"
git remote get-url origin >/dev/null 2>&1 || git remote add origin "$REPO_URL"
git push -u origin "$DEFAULT_BRANCH"
log "Done! $REPO_URL"