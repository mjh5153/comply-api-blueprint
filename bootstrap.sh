#!/usr/bin/env bash
set -euo pipefail

# ==========================================================
# Comply API Bootstrap Script
# Creates a GitHub repo, pushes the code, sets secrets,
# and prints run instructions.
# What it does
#
# Checks for git and gh (GitHub CLI)
#
# (Optionally) unzips the project from ZIP_PATH
#
# Initializes git, creates the repo on GitHub, sets origin, pushes main
#
# Creates GitHub Actions secrets: WEBHOOK_SECRET, API_KEY, RATE_LIMIT_PER_MIN
#
# (Optional) creates a release tag to trigger the Docker release workflow


# ----------------------------------------------------------
# Usage:
#   chmod +x bootstrap.sh
#   ./bootstrap.sh
#
# Optional environment overrides:
#   OWNER=<github-user-or-org>      (default: current gh user)
#   REPO_NAME=comply-api-blueprint
#   VISIBILITY=public|private       (default: public)
#   ZIP_PATH=/path/to/comply-api-blueprint.zip  (optional)
#   WEBHOOK_SECRET=<secret>         (default: random from openssl)
#   API_KEY=<key>                   (default: dev_key_123)
#   RATE_LIMIT_PER_MIN=<int>        (default: 120)
#   DEFAULT_BRANCH=main
# ==========================================================

# --- Helpers ---
#!/usr/bin/env bash
set -euo pipefail

# ==========================================================
# Comply API Bootstrap Script (no GitHub CLI)
# ----------------------------------------------------------
# Usage:
#   chmod +x bootstrap.sh
#   ./bootstrap.sh <github-https-url>
#
# Example:
#   ./bootstrap.sh https://github.com/yourname/comply-api-blueprint.git
#
# Notes:
# 1. You must manually create the repo on GitHub first.
# 2. Pass the repo URL as the first argument.
# ==========================================================

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <github-https-url>"
  exit 1
fi

REPO_URL="$1"
DEFAULT_BRANCH="${DEFAULT_BRANCH:-main}"

log() { printf "\033[1;34m[bootstrap]\033[0m %s\n" "$*"; }

# --- Initialize git if not present ---
if [[ ! -d .git ]]; then
  log "Initializing git repository ..."
  git init
fi

git checkout -B "$DEFAULT_BRANCH" >/dev/null 2>&1 || git checkout -b "$DEFAULT_BRANCH"

# --- Commit everything ---
git add .
if git diff --cached --quiet; then
  log "No changes to commit"
else
  git commit -m "Comply API MVP scaffold"
fi

# --- Add remote ---
if git remote get-url origin >/dev/null 2>&1; then
  log "Remote 'origin' already set: $(git remote get-url origin)"
else
  log "Adding remote origin: $REPO_URL"
  git remote add origin "$REPO_URL"
fi

# --- Push ---
log "Pushing branch $DEFAULT_BRANCH to origin ..."
git push -u origin "$DEFAULT_BRANCH"

log "Done! Repo is at: $REPO_URL"
