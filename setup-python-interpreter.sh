#!/bin/bash

# Python Interpreter Setup Script for IntelliJ IDEA

echo "Setting up Python interpreter for IntelliJ IDEA..."

# Check if Python 3 is installed
if ! command -v python3 &> /dev/null; then
    echo "❌ Python 3 is not installed. Please install Python 3.8 or higher."
    exit 1
fi

PYTHON_PATH=$(which python3)
PYTHON_VERSION=$($PYTHON_PATH --version 2>&1)

echo "✓ Found Python: $PYTHON_PATH"
echo "✓ Version: $PYTHON_VERSION"

# Create or activate virtual environment
if [ ! -d ".venv" ]; then
    echo "Creating virtual environment in .venv..."
    $PYTHON_PATH -m venv .venv
    echo "✓ Virtual environment created"
fi

# Activate virtual environment
source .venv/bin/activate

echo "✓ Virtual environment activated"

# Upgrade pip
pip install --upgrade pip

# Install required packages
echo "Installing required packages..."
pip install strands
pip install strands-tools

echo "✓ Packages installed successfully"

# Display Python interpreter path for IntelliJ
echo ""
echo "================================"
echo "IntelliJ IDEA Configuration:"
echo "================================"
echo "Python Interpreter Path: $PYTHON_PATH"
echo "Virtual Environment Path: $PWD/.venv/bin/python3"
echo "================================"
echo ""
echo "To configure IntelliJ IDEA:"
echo "1. Open IntelliJ IDEA"
echo "2. Go to Preferences/Settings → Project Structure → SDKs"
echo "3. Add Python SDK with path: $PYTHON_PATH"
echo "4. Or for virtual environment: $PWD/.venv/bin/python3"
echo "5. Apply the changes"
echo ""

