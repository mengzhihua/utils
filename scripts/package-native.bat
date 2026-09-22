@echo off
setlocal
set "ROOT=%~dp0.."
cd /d "%ROOT%"
set "VERSION=%~1"
if "%VERSION%"=="" (
  for /f "usebackq delims=" %%i in (`python -c "import re;from pathlib import Path;t=re.sub(r'<parent>.*?</parent>','',Path('pom.xml').read_text(),flags=re.S);m=re.search(r'<version>([^<]+)</version>',t);print(m.group(1) if m else '1.3.0')"`) do set "VERSION=%%i"
)
if not exist dist\utils.jar (
  echo 请先运行 scripts\package.sh 生成 JAR
  if exist target\utils.jar (
    mkdir dist 2>nul
    copy /y target\utils.jar dist\utils.jar >nul
  ) else (
    exit /b 1
  )
)
where candle >nul 2>&1
if errorlevel 1 (
  echo 打 Windows exe 需要 WiX 3.14：choco install wixtoolset --version 3.14.1
  echo 安装后重新打开终端，再执行本脚本。
  exit /b 1
)
python scripts\gen-icon.py
if exist dist\native rmdir /s /q dist\native
if exist dist\jpackage-input rmdir /s /q dist\jpackage-input
mkdir dist\jpackage-input dist\native
copy /y dist\utils.jar dist\jpackage-input\utils.jar >nul
jpackage --name Utils --app-version %VERSION% --dest dist\native --input dist\jpackage-input --main-jar utils.jar --main-class org.springframework.boot.loader.launch.JarLauncher --java-options "-Dutils.desktop=true" --arguments "--desktop" --type exe --icon packaging\icon.png --win-shortcut --win-menu --win-menu-group Utils --win-dir-chooser --win-per-user-install --win-upgrade-uuid e6c3d8a1-4f2b-4c9e-9a71-8b2d5e1f0c44
python scripts\stage-native.py dist\native %VERSION% windows x64
echo 已生成 dist\native\utils-%VERSION%-windows-x64.exe
echo 双击该 exe 安装后即可从开始菜单打开 Utils。
