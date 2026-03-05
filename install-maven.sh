#!/bin/bash

# Maven Installation Script for macOS
# This script installs Maven globally using Homebrew

echo "========================================="
echo "Maven Global Installation Script"
echo "========================================="
echo ""

# Step 1: Check if Homebrew is installed
echo "Step 1: Checking Homebrew installation..."
if command -v brew &> /dev/null; then
    echo "✓ Homebrew is already installed"
    BREW_VERSION=$(brew --version)
    echo "  Version: $BREW_VERSION"
else
    echo "✗ Homebrew not found. Installing Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi

echo ""

# Step 2: Check Java installation
echo "Step 2: Checking Java installation..."
if command -v java &> /dev/null; then
    echo "✓ Java is already installed"
    java --version
else
    echo "✗ Java not found. Installing Java 17..."
    brew install openjdk@17
    echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
    source ~/.zshrc
fi

echo ""

# Step 3: Install Maven
echo "Step 3: Installing Maven..."
if command -v mvn &> /dev/null; then
    echo "✓ Maven is already installed"
    mvn --version
else
    echo "Installing Maven via Homebrew..."
    brew install maven
fi

echo ""

# Step 4: Verify Maven installation
echo "Step 4: Verifying Maven installation..."
if command -v mvn &> /dev/null; then
    echo "✓ Maven installed successfully!"
    echo ""
    mvn --version
    echo ""
    echo "Maven is ready to use!"
else
    echo "✗ Maven installation failed"
    echo "Try manual installation:"
    echo "  brew install maven"
    exit 1
fi

echo ""
echo "========================================="
echo "Installation Complete!"
echo "========================================="
echo ""
echo "You can now use: mvn [command]"
echo ""
echo "Common commands:"
echo "  mvn --version          - Check Maven version"
echo "  mvn clean install      - Build project"
echo "  mvn spring-boot:run    - Run Spring Boot app"
echo "  mvn test               - Run tests"
echo ""
echo "Go to project directory:"
echo "  cd /Users/mjh5153/Downloads/demo"
echo ""
echo "Start backend:"
echo "  mvn spring-boot:run"
echo ""
echo "Run tests (in new terminal):"
echo "  mvn test"
echo ""

