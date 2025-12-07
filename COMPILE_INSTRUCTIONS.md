# How to Compile and Run the Quiz System

## Quick Start (Using Batch Files)

1. **Edit `compile.bat`** - Set your JavaFX path:
   ```batch
   set JAVAFX_PATH=C:\javafx-sdk-17.0.2\lib
   ```
   (Replace with your actual JavaFX SDK path)

2. **Double-click `compile.bat`** to compile

3. **Double-click `run.bat`** to run the application

---

## Manual Compilation (PowerShell)

### Step 1: Set JavaFX Path
Replace `C:\javafx-sdk-17.0.2\lib` with your actual JavaFX SDK path.

### Step 2: Compile
Open PowerShell in the project folder and run:

```powershell
# Create output directory
New-Item -ItemType Directory -Force -Path out

# Compile all files
javac --module-path "C:\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml `
  -d out `
  src\main\App.java `
  src\quiz\*.java `
  src\ui\*.java `
  src\user\*.java `
  src\leaderboard\*.java `
  src\analytics\*.java `
  src\utils\*.java `
  src\exceptions\*.java
```

### Step 3: Run
```powershell
java --module-path "C:\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp out main.App
```

---

## Manual Compilation (CMD)

### Step 1: Set JavaFX Path
Replace `C:\javafx-sdk-17.0.2\lib` with your actual JavaFX SDK path.

### Step 2: Compile
Open CMD in the project folder and run:

```cmd
mkdir out
javac --module-path "C:\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml -d out src\main\App.java src\quiz\*.java src\ui\*.java src\user\*.java src\leaderboard\*.java src\analytics\*.java src\utils\*.java src\exceptions\*.java
```

### Step 3: Run
```cmd
java --module-path "C:\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp out main.App
```

---

## Prerequisites

1. **Java JDK 11 or higher** - Download from https://adoptium.net/
2. **JavaFX SDK** - Download from https://openjfx.io/
   - Extract the ZIP file
   - Note the path to the `lib` folder (e.g., `C:\javafx-sdk-17.0.2\lib`)

---

## Troubleshooting

### "javac is not recognized"
- Make sure Java JDK is installed
- Add Java to your PATH environment variable
- Or use full path: `C:\Program Files\Java\jdk-17\bin\javac`

### "module not found: javafx.controls"
- Check your JavaFX path is correct
- Make sure you're pointing to the `lib` folder inside the JavaFX SDK

### "package does not exist"
- Make sure you're running commands from the project root folder
- Check that all source files are in the correct package directories

### To clean and recompile:
```powershell
Remove-Item -Recurse -Force out
# Then compile again
```

---

## Project Structure

```
NewQuiz/
├── src/           (Source code)
├── out/           (Compiled classes - created during compilation)
├── resources/     (Resource files)
├── compile.bat    (Compilation script)
└── run.bat        (Run script)
```

