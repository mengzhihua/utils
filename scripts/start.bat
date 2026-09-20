@echo off
setlocal
set "DIR=%~dp0"
if exist "%DIR%utils.jar" (
  set "JAR=%DIR%utils.jar"
) else if exist "%DIR%..\dist\utils.jar" (
  set "JAR=%DIR%..\dist\utils.jar"
) else if exist "%DIR%..\target\utils.jar" (
  set "JAR=%DIR%..\target\utils.jar"
) else (
  echo 找不到 utils.jar，请先运行 scripts\package.sh
  exit /b 1
)
echo 启动桌面模式（办公工作台）…
java -Dutils.desktop=true -jar "%JAR%" --desktop
