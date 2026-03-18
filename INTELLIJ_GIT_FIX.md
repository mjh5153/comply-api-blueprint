# IntelliJ Git Configuration Fix

## Your System Information ✅

```
Git Installed: ✅ Yes
Git Version: 2.39.0
Git Location: /usr/bin/git
User Name: mjh5153
User Email: your-email@example.com
```

## The Problem

IntelliJ cannot identify or communicate with your Git executable, even though Git is properly installed on your system.

## Solution - Configure Git in IntelliJ

### Step 1: Open IntelliJ Preferences
- **Mac:** `IntelliJ IDEA` menu → `Preferences` (or press `Cmd + ,`)
- **Windows/Linux:** `File` → `Settings` (or press `Ctrl + Alt + S`)

### Step 2: Navigate to Git Settings
In the Preferences/Settings dialog:
1. Search for "Git" in the search box (top-left)
2. OR navigate to: `Version Control` → `Git`

### Step 3: Configure Git Executable Path
In the "Git executable" field, enter:
```
/usr/bin/git
```

**Visual Steps:**
1. Locate the "Git executable:" text field
2. Clear any existing content
3. Type: `/usr/bin/git`
4. Click the "Test" button next to it
5. You should see: `Git version 2.39.0` (or similar)

### Step 4: Save Configuration
- Click "OK" or "Apply" button
- Close Preferences/Settings

### Step 5: Restart IntelliJ (Recommended)
1. Close IntelliJ completely
2. Wait 5 seconds
3. Reopen IntelliJ
4. Open your project

## Try Committing Again

1. Open the Commit panel:
   - **Mac:** Press `Cmd + K`
   - **Windows/Linux:** Press `Ctrl + Shift + C`
   
2. Select files to commit
3. Add commit message
4. Click "Commit" or "Commit and Push"

## If It Still Doesn't Work

### Quick Troubleshooting

**Try Alternative Path:**
```
/usr/local/bin/git
```

**Verify Git Works in Terminal:**
```bash
git status
git log --oneline
```

**Reconfigure Git Globally:**
```bash
git config --global user.name "mjh5153"
git config --global user.email "your-email@example.com"
```

### Nuclear Option - Invalidate Cache

1. In IntelliJ: `File` → `Invalidate Caches...`
2. Select: `Invalidate and Restart`
3. Wait for restart
4. Try commit again

## Step-by-Step Screenshots Guide

```
1. Open IntelliJ
   ↓
2. Click IntelliJ IDEA → Preferences (Mac) or File → Settings (Windows)
   ↓
3. Type "Git" in search box
   ↓
4. Click on "Version Control" → "Git"
   ↓
5. Find "Git executable:" field
   ↓
6. Clear current value
   ↓
7. Type: /usr/bin/git
   ↓
8. Click "Test" button
   ↓
   ✅ Should show: "Git version 2.39.0"
   ↓
9. Click "OK"
   ↓
10. Close and reopen IntelliJ
    ↓
11. Try commit again
```

## Verify From Terminal (Optional)

Run this to confirm everything is configured:

```bash
cd /Users/mjh5153/Downloads/demo
git status
git config user.name
git config user.email
```

You should see:
- `On branch main`
- `mjh5153`
- `your-email@example.com`

## Summary

| Item | Status | Action |
|------|--------|--------|
| Git Installed | ✅ Yes | None |
| Git Path | ✅ `/usr/bin/git` | Use this path in IntelliJ |
| User Configured | ✅ Yes | None |
| IntelliJ Config | ❌ Missing | Set Git path in Preferences |

---

## Next Steps After Fix

Once Git is configured in IntelliJ:

1. ✅ Open commit panel (Cmd+K)
2. ✅ Select files
3. ✅ Add commit message
4. ✅ Click "Commit"
5. ✅ Then push to GitHub using the command from PUSH_COMMAND.md

---

**Your Git Path:** `/usr/bin/git`
**Copy this to IntelliJ → Preferences → Version Control → Git → Git executable**

Good luck! 🚀

