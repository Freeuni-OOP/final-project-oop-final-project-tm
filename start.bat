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
docker compose down -v >nul 2>&1
docker compose up -d

echo ⏳ Waiting for MySQL to be ready (15s)...
timeout /t 15 /nobreak >nul

echo 🗑️  Resetting database...
docker exec project_db mysql -ustudent -ppassword book_to -e "SET FOREIGN_KEY_CHECKS=0; DROP TABLE IF EXISTS booking_slots, bookings, service, services, slots, users, flyway_schema_history; SET FOREIGN_KEY_CHECKS=1;" 2>nul

:: ── 2. Backend ────────────────────────────────────────────────────────────────
echo.
echo ☕ Starting Spring Boot Backend...
echo Checking port 8080...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":8080 " ^| findstr "LISTENING"') do (
    echo ⚠️  Port 8080 in use — killing PID %%a...
    taskkill /PID %%a /F >nul 2>&1
)
cd backend
start "Backend" cmd /k "mvnw.cmd clean spring-boot:run"
cd ..

:: ── 3. Frontend ───────────────────────────────────────────────────────────────
echo.
echo ⚛️  Starting React Frontend...
echo Checking port 5173...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":5173 " ^| findstr "LISTENING"') do (
    echo ⚠️  Port 5173 in use — killing PID %%a...
    taskkill /PID %%a /F >nul 2>&1
)
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
