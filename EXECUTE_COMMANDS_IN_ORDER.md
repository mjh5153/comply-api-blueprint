# EXECUTE THESE COMMANDS IN ORDER

## Step 1: Install Xcode Command Line Tools

Copy and paste this command:
```bash
xcode-select --install
```

**What happens:**
- A dialog box appears
- Click the "Install" button
- Accept the terms
- Wait for installation (5-15 minutes)

---

## Step 2: Verify Xcode Installation

After Xcode finishes, run:
```bash
xcode-select -p
```

**You should see:**
```
/Library/Developer/CommandLineTools
```

(If you see an error, repeat Step 1)

---

## Step 3: Accept Xcode License Agreement

Run:
```bash
sudo xcodebuild -license accept
```

**It will ask for your password and confirmation:**
- Type your Mac password (nothing displays as you type)
- Press Enter
- Type: `agree`
- Press Enter

---

## Step 4: Install Maven

Once Xcode is installed, run:
```bash
brew install maven
```

**Wait for installation to complete (1-2 minutes)**

---

## Step 5: Verify Maven Installation

Run:
```bash
mvn --version
```

**You should see:**
```
Apache Maven 3.9.6
Maven home: /usr/local/Cellar/maven/3.9.6/libexec
Java version: 17.x.x
```

(If you see an error, something didn't install correctly - check Step 1-4)

---

## NOW YOU CAN RUN THE TESTS

### Terminal 1: Start the Backend

Open a new Terminal and run:
```bash
cd /Users/mjh5153/Downloads/demo
mvn spring-boot:run
```

**Wait for:**
```
Tomcat started on port(s): 8080 (http)
```

### Terminal 2: Run the Tests

Open another new Terminal and run:
```bash
cd /Users/mjh5153/Downloads/demo
mvn test
```

**You should see:**
```
Tests run: 12, Failures: 0
BUILD SUCCESS
```

---

## Quick Reference - All Commands in Order

```bash
# 1. Install Xcode tools
xcode-select --install
# → Wait for dialog and click Install

# 2. Verify Xcode
xcode-select -p

# 3. Accept license
sudo xcodebuild -license accept
# → Type your password, then "agree"

# 4. Install Maven
brew install maven

# 5. Verify Maven
mvn --version

# 6. Start backend (Terminal 1)
cd /Users/mjh5153/Downloads/demo && mvn spring-boot:run

# 7. Run tests (Terminal 2)
cd /Users/mjh5153/Downloads/demo && mvn test
```

---

## Troubleshooting

### If "Xcode/CLT is not installed" after Step 1
- Try again: `xcode-select --install`
- Or reset: `sudo xcode-select --reset`

### If Maven still not found after Step 4
```bash
source ~/.zshrc
mvn --version
```

### If Java not found
```bash
brew install openjdk@17
```

---

## Summary

**Complete installation takes ~15-30 minutes:**
- Xcode installation: 5-15 minutes (automatic)
- Maven installation: 1-2 minutes
- Tests execution: 1-2 minutes

**Then all 12 endpoints will be verified working!**

---

**Status:** Ready to follow these steps
**Next:** Copy the commands above and execute them one by one

