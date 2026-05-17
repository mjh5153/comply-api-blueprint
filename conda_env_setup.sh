#!/usr/bin/env bash
# Create a conda environment for this project with M1/M2 Mac support
# Requires: conda or mamba installed
set -euo pipefail

CONDA_ENV_NAME="${CONDA_ENV_NAME:-demo-env}"
PYTHON_VERSION="${PYTHON_VERSION:-3.10}"

# Check if conda is available
if ! command -v conda >/dev/null 2>&1; then
  echo "conda not found. Please install Anaconda or Miniconda and retry." >&2
  echo "Download from: https://www.anaconda.com/download" >&2
  exit 1
fi

echo "Creating conda environment '${CONDA_ENV_NAME}' with Python ${PYTHON_VERSION}..."

# For M1/M2 Macs, use mamba if available (faster resolver)
if command -v mamba >/dev/null 2>&1; then
  echo "Using mamba (faster resolver)..."
  mamba create -y -n "${CONDA_ENV_NAME}" python="${PYTHON_VERSION}"
  mamba activate "${CONDA_ENV_NAME}"
  mamba install -y -c conda-forge pandas matplotlib scikit-learn
  pip install mlxtend jupyter
else
  conda create -y -n "${CONDA_ENV_NAME}" python="${PYTHON_VERSION}"
  # shellcheck disable=SC1091
  eval "$(conda shell.bash hook)"
  conda activate "${CONDA_ENV_NAME}"
  conda install -y -c conda-forge pandas matplotlib scikit-learn
  pip install mlxtend jupyter
fi

echo ""
echo "Setup complete! Conda environment '${CONDA_ENV_NAME}' is ready."
echo ""
echo "To activate the environment, run:"
echo "  conda activate ${CONDA_ENV_NAME}"
echo ""
echo "To launch Jupyter Lab with the notebook:"
echo "  jupyter lab market_basket_analysis_example.ipynb"
echo ""
echo "To run the import test:"
echo "  python import_test.py"
echo ""
echo "To deactivate:"
echo "  conda deactivate"

