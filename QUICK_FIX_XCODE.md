# ✅ QUICK FIX - Copy & Paste These Commands

## Problem: Maven installation failed due to missing Xcode

## Solution: Install Xcode Command Line Tools

### Command 1: Start Xcode Installation
```bash
xcode-select --install
```

**A dialog will appear. Click "Install" and wait 5-15 minutes for completion.**

### Command 2: Verify Xcode is Installed
```bash
xcode-select -p
```

**Expected output (no error):**
```
/Library/Developer/CommandLineTools
```

### Command 3: Accept Xcode License (if needed)
```bash
sudo xcodebuild -license accept
```

**Type `agree` and press Enter**

### Command 4: Now Install Maven
```bash
brew install maven
```

### Command 5: Verify Maven Installation
```bash
mvn --version
```

**Expected output:**
```
Apache Maven 3.x.x
Maven home: /usr/local/Cellar/maven/3.x.x/libexec
Java version: 17.x.x
```

---

## After Installation - Run Tests

**Terminal 1: Start Backend**
```bash
cd /Users/mjh5153/Downloads/demo && mvn spring-boot:run
```

**Terminal 2: Run Tests (after backend starts)**
```bash
cd /Users/mjh5153/Downloads/demo && mvn test
```

---

## Summary

**5 Simple Steps:**

1. Run: `xcode-select --install` → Click Install in dialog → Wait
2. Verify: `xcode-select -p`
3. Accept license: `sudo xcodebuild -license accept` → Type "agree"
4. Install Maven: `brew install maven`
5. Verify: `mvn --version`

**Total time:** 15-30 minutes (mostly waiting for Xcode)

**Then:** Run tests with `mvn test`

---

**Status:** Ready to fix the issue
**Next Action:** Copy the commands above and run them one by one

