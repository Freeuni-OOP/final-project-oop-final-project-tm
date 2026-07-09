@echo off
:: ─────────────────────────────────────────────────────────────────────────────
:: Service Clinic — Auto Setup Script (Windows)
:: Installs: Chocolatey, Docker Desktop, Node.js 18, Java JDK 21
:: Run as Administrator!
:: ─────────────────────────────────────────────────────────────────────────────

echo.
echo ========================================
echo    Service Clinic - Setup Script
echo ========================================
echo.

:: ── Check Admin ──────────────────────────────────────────────────────────────
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo [!] Please run this script as Administrator.
    echo     Right-click setup.bat and choose "Run as administrator"
    pause
    exit /b 1
)

:: ── Install Chocolatey (package manager) ─────────────────────────────────────
where choco >nul 2>&1
if %errorLevel% neq 0 (
    echo [->] Installing Chocolatey...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
    echo [OK] Chocolatey installed.
) else (
    echo [OK] Chocolatey already installed.
)

:: ── Install Docker Desktop ────────────────────────────────────────────────────
where docker >nul 2>&1
if %errorLevel% neq 0 (
    echo [->] Installing Docker Desktop...
    choco install docker-desktop -y
    echo [OK] Docker Desktop installed. Please restart your PC before running the app.
) else (
    echo [OK] Docker already installed.
)

:: ── Install Node.js 18 ────────────────────────────────────────────────────────
where node >nul 2>&1
if %errorLevel% neq 0 (
    echo [->] Installing Node.js 18...
    choco install nodejs-lts -y
    echo [OK] Node.js installed.
) else (
    echo [OK] Node.js already installed.
)

:: ── Install Java JDK 21 ───────────────────────────────────────────────────────
where java >nul 2>&1
if %errorLevel% neq 0 (
    echo [->] Installing Java JDK 21...
    choco install temurin21 -y
    echo [OK] Java 21 installed.
) else (
    echo [OK] Java already installed.
)

:: ── Done ──────────────────────────────────────────────────────────────────────
echo.
echo ========================================
echo [OK] All requirements installed!
echo ========================================
echo.
echo Now run the app with 3 terminals:
echo.
echo   Terminal 1:  docker compose up -d
echo   Terminal 2:  cd backend  ^&^& mvnw.cmd spring-boot:run
echo   Terminal 3:  cd frontend ^&^& npm install ^&^& npm run dev
echo.
echo Then open: http://localhost:5173
echo.
pause
