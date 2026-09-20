@echo off
setlocal
set "ROOT=%~dp0.."
cd /d "%ROOT%"
if not exist dist\utils.jar (
  echo 请先在 Git Bash / WSL 运行 scripts\package.sh 或在已有 JAR 后继续
  if exist target\utils.jar (
    mkdir dist 2>nul
    copy /y target\utils.jar dist\utils.jar >nul
  ) else (
    exit /b 1
  )
)
python scripts\gen-icon.py
if exist dist\native rmdir /s /q dist\native
if exist dist\jpackage-input rmdir /s /q dist\jpackage-input
mkdir dist\jpackage-input dist\native
copy /y dist\utils.jar dist\jpackage-input\utils.jar >nul
jpackage --name Utils --app-version 1.0.0 --dest dist\native --input dist\jpackage-input --main-jar utils.jar --main-class org.springframework.boot.loader.launch.JarLauncher --java-options "-Dutils.desktop=true" --arguments "--desktop" --type app-image --icon packaging\icon.png
python scripts\stage-native.py dist\native 1.0.0 windows x64
echo 已生成 dist\native\utils-1.0.0-windows-x64.zip
echo 若要安装包：再执行 jpackage --type exe
