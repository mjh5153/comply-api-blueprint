#!/usr/bin/env bash
# Create a Python virtual environment in .venv and install requirements
set -euo pipefail
PYTHON=${PYTHON:-python3}
VENV_DIR=".venv"

if ! command -v "$PYTHON" >/dev/null 2>&1; then
  echo "${PYTHON} not found. Please install Python 3 and retry." >&2
  exit 1
fi

echo "Creating virtual environment in ${VENV_DIR}..."
$PYTHON -m venv "$VENV_DIR"

# Activate and upgrade pip
# shellcheck disable=SC1091
source "$VENV_DIR/bin/activate"
python -m pip install --upgrade pip setuptools wheel

if [ -f requirements.txt ]; then
  echo "Installing packages from requirements.txt..."
  # If PIP_INDEX_URL is set in the environment, use it; otherwise pip will use its default index
  if [ -n "${PIP_INDEX_URL:-}" ]; then
    echo "Using PIP_INDEX_URL=${PIP_INDEX_URL}"
    pip install --index-url "${PIP_INDEX_URL}" --trusted-host "$(echo ${PIP_INDEX_URL} | awk -F/ '{print $3}')" -r requirements.txt
  else
    pip install -r requirements.txt
  fi
else
  echo "requirements.txt not found in $(pwd). Create one with required packages." >&2
  exit 1
fi

echo "Setup complete. Activate with: source ${VENV_DIR}/bin/activate"


