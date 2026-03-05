# ✅ FIX Maven Installation Error - Xcode Required

## Problem Identified

**Error:** `maven: An unsatisfied requirement failed this build`

**Root Cause:** Xcode command line tools are not installed. Homebrew and Maven both require Xcode.

---

## ✅ Solution - Install Xcode Command Line Tools

### Step 1: Open Terminal and Run This Command
```bash
xcode-select --install
```

**A dialog will appear asking to install Xcode command line tools.**

### Step 2: Click "Install" Button
- A popup window will appear
- Click "Install" to proceed
- Accept the license agreement
- Wait for installation to complete (5-15 minutes)

### Step 3: Verify Installation
```bash
xcode-select -p
```

**Expected output:**
```
/Applications/Xcode.app/Contents/Developer
```

Or for command line tools only:
```
/Library/Developer/CommandLineTools
```

---

## ✅ After Xcode is Installed - Install Maven

### Option 1: Use Homebrew
```bash
brew install maven
```

### Option 2: Or use the install script
```bash
cd /Users/mjh5153/Downloads/demo && chmod +x install-maven.sh && ./install-maven.sh
```

---

## Detailed Step-by-Step Guide

### Complete Installation Process:

**1. Install Xcode Command Line Tools**
```bash
xcode-select --install
```
- Wait for dialog box
- Click "Install"
- Wait for completion (5-15 minutes)

**2. Verify Xcode Installation**
```bash
xcode-select -p
```
- Should show a path without errors

**3. Agree to Xcode License (if needed)**
```bash
sudo xcodebuild -license accept
```

**4. Now Install Maven**
```bash
brew install maven
```

**5. Verify Maven Installation**
```bash
mvn --version
```

---

## Why This Happens

- **Xcode command line tools** include essential build tools (Git, compilers, etc.)
- **Homebrew** requires Xcode tools to build/install packages
- **Maven** requires build tools to function

All three are interconnected and required together.

---

## If Installation Still Fails

### Check what's blocking
```bash
softwareupdate -l
```

### Try to install specific tools
```bash
brew install git
brew install maven
```

### Or manually install from App Store
1. Open **App Store**
2. Search for **Xcode**
3. Click **Install** (requires ~12 GB space)
4. After installation, run:
   ```bash
   sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
   ```

---

## Complete Installation Steps

**In Terminal, run these commands in order:**

```bash
# 1. Install Xcode command line tools
xcode-select --install

# Wait for dialog to appear and click Install
# This will take 5-15 minutes

# 2. After installation completes, verify
xcode-select -p

# 3. Accept Xcode license (if needed)
sudo xcodebuild -license accept

# 4. Install Maven
brew install maven

# 5. Verify Maven is installed
mvn --version

# 6. Verify Java is installed
java --version

# 7. Now you can use Maven!
cd /Users/mjh5153/Downloads/demo
mvn spring-boot:run
```

---

## Troubleshooting

### Dialog doesn't appear
```bash
# Force installation
sudo xcode-select --reset
xcode-select --install
```

### Still getting errors
```bash
# Try updating Xcode tools
softwareupdate -i -a

# Check if installed
pkgutil --pkg-info=com.apple.pkg.CLTools_Executables
```

### Maven still not found after installation
```bash
# Reload your shell
source ~/.zshrc

# Check Maven location
which mvn

# Check PATH
echo $PATH
```

---

## Expected Output

After successful installation:

**Xcode Check:**
```bash
$ xcode-select -p
/Library/Developer/CommandLineTools
```

**Maven Check:**
```bash
$ mvn --version
Apache Maven 3.9.6
Maven home: /usr/local/Cellar/maven/3.9.6/libexec
Java version: 17.x.x
```

**Java Check:**
```bash
$ java --version
openjdk 17.x.x YYYY-MM-DD
OpenJDK Runtime Environment
```

---

## After Everything is Installed

You can now run tests:

**Terminal 1: Start Backend**
```bash
cd /Users/mjh5153/Downloads/demo
mvn spring-boot:run
```

**Terminal 2: Run Tests**
```bash
cd /Users/mjh5153/Downloads/demo
mvn test
```

---

## Summary

**The complete fix:**

1. **Install Xcode command line tools:** `xcode-select --install`
2. **Wait for installation** (5-15 minutes)
3. **Install Maven:** `brew install maven`
4. **Verify:** `mvn --version`
5. **Run tests:** `mvn test`

**Time required:** 15-30 minutes total

---

**Status:** Follow steps above to fix Maven installation
**Next:** Run Xcode tools installation, then Maven will work

