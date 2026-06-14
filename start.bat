@echo off
:: ─────────────────────────────────────────────────────────────────────────────
:: Service Clinic — One-command startup script (Windows)
:: Usage: double-click start.bat  OR  run from terminal
:: ─────────────────────────────────────────────────────────────────────────────

echo.
echo 🚀 Starting Service Clinic App...
echo.

:: ── 1. Database ───────────────────────────────────────────────────────────────
echo 📦 Starting MySQL (Docker)...
docker compose up -d

echo ⏳ Waiting for MySQL to be ready (15s)...
timeout /t 15 /nobreak >nul

:: ── 2. Backend ────────────────────────────────────────────────────────────────
echo.
echo ☕ Starting Spring Boot Backend...
cd backend
start "Backend" cmd /k "mvnw.cmd spring-boot:run"
cd ..

:: ── 3. Frontend ───────────────────────────────────────────────────────────────
echo.
echo ⚛️  Starting React Frontend...
cd frontend
start "Frontend" cmd /k "npm install && npm run dev"
cd ..

:: ── Ready ─────────────────────────────────────────────────────────────────────
echo.
echo ✅ All services started!
echo 🌐 Open in browser: http://localhost:5173
echo.
echo Backend and Frontend are running in separate windows.
echo Close those windows to stop the services.
echo.
pause
