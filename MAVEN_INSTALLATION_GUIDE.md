# Maven Global Installation Guide - macOS

## ✅ Quick Installation

### Option 1: Using Homebrew (Easiest - Recommended)

```bash
# Install Maven using Homebrew
brew install maven

# Verify installation
mvn --version
```

**Expected Output:**
```
Apache Maven 3.x.x
Maven home: /usr/local/Cellar/maven/3.x.x/libexec
Java version: 17.x.x
```

---

### Option 2: Manual Installation (If Homebrew not available)

```bash
# Download Maven binary
cd /tmp
curl -O https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz

# Extract to /usr/local
sudo tar -xzf apache-maven-3.9.6-bin.tar.gz -C /usr/local/
sudo mv /usr/local/apache-maven-3.9.6 /usr/local/maven

# Create symlink
sudo ln -s /usr/local/maven/bin/mvn /usr/local/bin/mvn

# Verify
mvn --version
```

---

### Option 3: Using SDKMAN (Multi-version management)

```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash

# Source sdkman
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install Maven
sdk install maven

# Verify
mvn --version
```

---

## Verify Installation

After installation, run:
```bash
mvn --version
```

Expected output should show Maven version, Java version, and paths.

---

## Fix PATH Issues (If mvn command not found)

If `mvn` command still not found after installation:

### Add to Shell Profile

#### For Zsh (default on modern macOS):
```bash
# Edit ~/.zshrc
nano ~/.zshrc

# Add this line at the end:
export PATH="/usr/local/maven/bin:$PATH"

# Or for Homebrew installation:
export PATH="/usr/local/Cellar/maven/3.9.6/bin:$PATH"

# Save (Ctrl+X, then Y, then Enter)
# Reload shell
source ~/.zshrc
```

#### For Bash (older macOS):
```bash
# Edit ~/.bash_profile
nano ~/.bash_profile

# Add this line at the end:
export PATH="/usr/local/maven/bin:$PATH"

# Reload shell
source ~/.bash_profile
```

---

## Step-by-Step Installation (Homebrew)

### 1. Check if Homebrew is installed
```bash
brew --version
```

### 2. If Homebrew not installed, install it
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### 3. Install Maven
```bash
brew install maven
```

### 4. Wait for installation to complete
Installation typically takes 2-5 minutes

### 5. Verify installation
```bash
mvn --version
java --version
```

---

## Java Requirement

Maven requires Java to be installed. Check:
```bash
java --version
```

Should show Java 8 or higher.

If not installed:
```bash
brew install openjdk@17
```

---

## Common Issues & Solutions

### Issue: "mvn: command not found"

**Solution 1: Add to PATH**
```bash
echo 'export PATH="/usr/local/maven/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

**Solution 2: Reinstall Maven**
```bash
brew uninstall maven
brew install maven
```

**Solution 3: Check where Maven is installed**
```bash
which mvn
brew list maven
```

---

### Issue: "Cannot find Java"

```bash
# Check Java installation
java -version

# If not found, install Java
brew install openjdk@17

# Add Java to PATH if needed
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

---

## After Installation

Once Maven is installed globally:

### Use mvn directly (no need for ./mvnw)
```bash
cd /Users/mjh5153/Downloads/demo

# Instead of ./mvnw, now you can use:
mvn --version
mvn clean install
mvn spring-boot:run
mvn test
```

### Run the project
```bash
cd /Users/mjh5153/Downloads/demo
mvn spring-boot:run
```

### Run tests
```bash
mvn test
```

---

## Verify Maven Works with Project

```bash
cd /Users/mjh5153/Downloads/demo

# Check Maven recognizes project
mvn -v

# Build the project
mvn clean package -DskipTests

# Run tests
mvn test

# Start application
mvn spring-boot:run
```

---

## Troubleshooting Maven

### Update Maven to latest
```bash
brew upgrade maven
```

### Remove and reinstall
```bash
brew uninstall maven
brew install maven
```

### Check Maven home
```bash
mvn -version | grep "Maven home"
```

### Clear Maven cache (if needed)
```bash
rm -rf ~/.m2/repository
```

---

## Summary

**Quick Installation (3 commands):**
```bash
# 1. Install Maven
brew install maven

# 2. Verify
mvn --version

# 3. You're done!
```

**Expected Result:**
- `mvn` command available globally
- Can run from any directory
- No need for `./mvnw`

---

## Next Steps

After Maven is installed:

```bash
# Go to project
cd /Users/mjh5153/Downloads/demo

# Terminal 1: Start backend
mvn spring-boot:run

# Terminal 2: Run tests
mvn test
```

Or use the test script:
```bash
./test-endpoints.sh
```

---

**Installation Status:** Complete ✅
**Maven Available:** Global command `mvn` ready to use
**Next:** Run `mvn --version` to confirm

