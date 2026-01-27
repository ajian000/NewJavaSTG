@echo off
REM JavaSTG 打包脚本（包含 OGG 支持）
REM @Time 2026-01-24

echo ========================================
echo   JavaSTG 打包脚本（包含 OGG 支持）
echo ========================================
echo.

echo [1/2] 检查编译输出...
if not exist "bin\Main\Main.class" (
    echo [错误] 项目未编译，请先运行 compile.bat
    pause
    exit /b 1
)
if not exist "lib\jorbis.jar" (
    echo [错误] JOrbis JAR 不存在，请先运行 compile.bat
    pause
    exit /b 1
)
echo [成功] 编译输出检查完成
echo.

echo [2/2] 打包 JAR 文件...
jar cvfm JavaSTG.jar MANIFEST.MF -C bin .
if %errorlevel% neq 0 (
    echo [错误] JAR 打包失败
    pause
    exit /b 1
)
echo [成功] JAR 打包完成
echo.

echo ========================================
echo   打包完成！
echo ========================================
echo.
echo 运行游戏：
echo   java -jar JavaSTG.jar
echo.
echo 注意：需要确保 lib/jorbis.jar 与 JavaSTG.jar 在同一目录
echo.
pause
