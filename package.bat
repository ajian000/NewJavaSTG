@echo off
echo 正在打包 JavaSTG 项目...

REM 创建临时目录
if not exist "temp_jar" mkdir temp_jar

REM 复制编译后的class文件
echo 复制编译文件...
xcopy /E /I /Y bin temp_jar >nul

REM 复制资源文件
echo 复制资源文件...
xcopy /E /I /Y resources temp_jar\resources >nul

REM 创建jar包
echo 创建jar包...
jar cvfm JavaSTG.jar MANIFEST.MF -C temp_jar .

REM 清理临时文件
echo 清理临时文件...
rmdir /S /Q temp_jar

echo 打包完成！
echo 生成的jar包: JavaSTG.jar
echo.
echo 运行命令: java -jar JavaSTG.jar
