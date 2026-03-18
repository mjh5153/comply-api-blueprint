
# ✅ Maven Global Installation - Ready to Execute

## Install Maven Globally (2 Options)

### Option 1: Automatic Installation Script (Easiest)

```bash
cd /Users/mjh5153/Downloads/demo
chmod +x install-maven.sh
./install-maven.sh
```

This script will:
- ✅ Check/install Homebrew
- ✅ Check/install Java
- ✅ Install Maven
- ✅ Verify everything works
- ✅ Show usage instructions

---

### Option 2: Manual Installation (One Command)

```bash
brew install maven
```

That's it! Maven will be installed globally.

---

## Verify Maven Installation

After installation, run:
```bash
mvn --version
```

Expected output:
```
Apache Maven 3.x.x
Maven home: /usr/local/Cellar/maven/3.x.x/libexec
Java version: 17.x.x
```

---

## After Maven is Installed

You can now use `mvn` from anywhere instead of `./mvnw`:

**Before (with wrapper):**
```bash
./mvnw spring-boot:run
./mvnw test
```

**After (with global Maven):**
```bash
mvn spring-boot:run
mvn test
```

---

## Run Tests with Global Maven

```bash
cd /Users/mjh5153/Downloads/demo

# Terminal 1: Start backend
mvn spring-boot:run

# Terminal 2: Run tests
mvn test

# Or run bash test script
./test-endpoints.sh
```

---

## Commands You Can Now Use

```bash
# Check Maven version
mvn --version

# Check Java version
java --version

# Build project
mvn clean install

# Build without tests
mvn clean package -DskipTests

# Run Spring Boot app
mvn spring-boot:run

# Run tests
mvn test

# Run specific test class
mvn test -Dtest=CompanyControllerIntegrationTest

# Clean build files
mvn clean
```

---

## If Installation Fails

### Check if Homebrew is installed
```bash
brew --version
```

If not installed:
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### Then install Maven
```bash
brew install maven
```

### Verify
```bash
mvn --version
```

---

## Troubleshooting

### "mvn: command not found" after installation

1. **Reload shell:**
   ```bash
   source ~/.zshrc    # For Zsh
   source ~/.bash_profile  # For Bash
   ```

2. **Add to PATH manually:**
   ```bash
   echo 'export PATH="/usr/local/opt/maven/bin:$PATH"' >> ~/.zshrc
   source ~/.zshrc
   ```

3. **Find where Maven is installed:**
   ```bash
   which mvn
   brew list maven
   ```

---

### "Cannot find Java"

```bash
# Check Java
java --version

# If not found, install Java
brew install openjdk@17

# Add to PATH
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

---

## Summary

**Quick Start:**
1. Run: `brew install maven`
2. Verify: `mvn --version`
3. Done! Use `mvn` commands globally

**Or use automatic script:**
1. Run: `./install-maven.sh`
2. Follow prompts
3. Done!

---

## Next Steps

Once Maven is installed globally:

```bash
cd /Users/mjh5153/Downloads/demo

# Start backend (Terminal 1)
mvn spring-boot:run

# Run tests (Terminal 2)
mvn test
```

---

**Status:** Installation scripts ready ✅
**Files Created:** 
- install-maven.sh (automated installer)
- MAVEN_INSTALLATION_GUIDE.md (detailed guide)

Execute one of the commands above to install Maven globally!

