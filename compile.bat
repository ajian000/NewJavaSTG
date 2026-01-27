@echo off
chcp 65001 >nul
echo ========================================
echo   JavaSTG Compile Script
echo ========================================
echo.

echo [1/3] Compile JOrbis library...
javac -encoding UTF-8 -d bin/jorbis -sourcepath lib/jorbis-master lib/jorbis-master/com/jcraft/jogg/*.java lib/jorbis-master/com/jcraft/jorbis/*.java
if %errorlevel% neq 0 (
    echo [ERROR] JOrbis compilation failed
    pause
    exit /b 1
)
echo [SUCCESS] JOrbis compiled
echo.

echo [2/3] Package JOrbis JAR...
cd bin/jorbis
jar cvf ../../lib/jorbis.jar *
cd ../..
if %errorlevel% neq 0 (
    echo [ERROR] JOrbis JAR packaging failed
    pause
    exit /b 1
)
echo [SUCCESS] JOrbis JAR created
echo.

echo [3/3] Compile project...
javac -encoding UTF-8 -d bin -sourcepath src src/Main/Main.java src/stg/base/Window.java src/stg/base/VirtualKeyboardPanel.java src/stg/game/ui/TitleScreen.java src/stg/game/ui/GameCanvas.java src/stg/game/ui/GameStatusPanel.java src/stg/util/AudioManager.java src/stg/util/OGGAudioSupport.java src/stg/util/ResourceManager.java
if %errorlevel% neq 0 (
    echo [ERROR] Project compilation failed
    pause
    exit /b 1
)
echo [SUCCESS] Project compiled
echo.

echo ========================================
echo   Compilation Complete!
echo ========================================
echo.
echo Run game:
echo   java -cp "bin;bin/jorbis" Main.Main
echo.
echo Or use JAR:
echo   java -jar JavaSTG.jar
echo.
pause
