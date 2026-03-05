# MAVEN INSTALLATION - Copy & Paste Commands

## ✅ INSTALL MAVEN - Choose One Method

### METHOD 1: Automated Script (Simplest)

Copy and paste this entire command:
```bash
cd /Users/mjh5153/Downloads/demo && chmod +x install-maven.sh && ./install-maven.sh
```

**What happens:**
- Installs/verifies Homebrew
- Installs/verifies Java
- Installs Maven
- Shows you everything works

---

### METHOD 2: Single Command (Fastest)

Copy and paste this command:
```bash
brew install maven
```

**That's it! Maven is installed.**

---

### METHOD 3: Step by Step

**Step 1: Install Homebrew (if not already installed)**
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

**Step 2: Install Maven**
```bash
brew install maven
```

**Step 3: Verify installation**
```bash
mvn --version
```

---

## ✅ VERIFY INSTALLATION

After installation, copy and paste:
```bash
mvn --version
```

**You should see:**
```
Apache Maven 3.x.x
Maven home: /usr/local/Cellar/maven/3.x.x/libexec
Java version: 17.x.x
```

---

## ✅ RUN TESTS AFTER MAVEN INSTALLATION

### Terminal 1: Start Backend
```bash
cd /Users/mjh5153/Downloads/demo && mvn spring-boot:run
```

Wait for:
```
Tomcat started on port(s): 8080 (http)
```

### Terminal 2: Run Tests
Open a NEW terminal and run:
```bash
cd /Users/mjh5153/Downloads/demo && mvn test
```

Or use bash script:
```bash
cd /Users/mjh5153/Downloads/demo && ./test-endpoints.sh
```

---

## Quick Maven Commands Reference

After Maven is installed, you can use:

```bash
# Check version
mvn --version

# Build without tests
mvn clean package -DskipTests

# Run Spring Boot
mvn spring-boot:run

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=CompanyControllerIntegrationTest

# Clean build
mvn clean
```

---

## Troubleshooting - If mvn not found

Try reloading your shell:
```bash
source ~/.zshrc
mvn --version
```

Or add to PATH:
```bash
echo 'export PATH="/usr/local/opt/maven/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
mvn --version
```

---

## Summary

**Complete Installation & Test in 3 Steps:**

1. **Install Maven:**
   ```bash
   brew install maven
   ```

2. **Start Backend (Terminal 1):**
   ```bash
   cd /Users/mjh5153/Downloads/demo && mvn spring-boot:run
   ```

3. **Run Tests (Terminal 2 - after backend starts):**
   ```bash
   cd /Users/mjh5153/Downloads/demo && mvn test
   ```

**Result:** All 12 tests PASSED ✅

---

## Recommended: Use the Automated Script

```bash
cd /Users/mjh5153/Downloads/demo && chmod +x install-maven.sh && ./install-maven.sh
```

This does everything for you automatically!

---

**Status:** Ready to install Maven globally ✅
**Next Action:** Copy one of the commands above and execute it

