#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
# Service Clinic — One-command startup script (Linux / macOS)
# Usage: ./start.sh
# ─────────────────────────────────────────────────────────────────────────────

BACKEND_PID=""
FRONTEND_PID=""

# ── Clean shutdown on CTRL+C ─────────────────────────────────────────────────
cleanup() {
    echo -e "\n🛑 Shutting down services..."
    [[ -n "$BACKEND_PID" ]]  && kill "$BACKEND_PID"  2>/dev/null
    [[ -n "$FRONTEND_PID" ]] && kill "$FRONTEND_PID" 2>/dev/null
    echo "🗑️  Wiping database..."
    docker exec project_db mysql -ustudent -ppassword book_to -e "
        SET FOREIGN_KEY_CHECKS=0;
        DROP TABLE IF EXISTS booking_slots, bookings, service, services, slots, users, flyway_schema_history;
        SET FOREIGN_KEY_CHECKS=1;
    " 2>/dev/null
    docker compose stop
    echo -e "👋 All services stopped. Bye!\n"
    exit 0
}
trap cleanup SIGINT SIGTERM

# ── 1. Database ───────────────────────────────────────────────────────────────
echo -e "\n🚀 Starting Service Clinic App...\n"
echo "📦 Starting MySQL (Docker)..."
docker compose down -v 2>/dev/null   # wipe old volume so MySQL re-creates a fresh book_to each run
docker compose up -d

echo "⏳ Waiting for MySQL to be ready (15s)..."
sleep 15

echo "🗑️  Resetting database..."
docker exec project_db mysql -ustudent -ppassword book_to -e "
    SET FOREIGN_KEY_CHECKS=0;
    DROP TABLE IF EXISTS booking_slots, bookings, service, services, slots, users, flyway_schema_history;
    SET FOREIGN_KEY_CHECKS=1;
" 2>/dev/null

# ── 2. Backend ────────────────────────────────────────────────────────────────
echo -e "\n☕ Starting Spring Boot Backend..."
if lsof -ti:8080 >/dev/null 2>&1; then
    echo "⚠️  Port 8080 already in use — killing existing process..."
    kill $(lsof -ti:8080) 2>/dev/null
    sleep 1
fi
ROOT_DIR="$(pwd)"
cd backend
./mvnw clean spring-boot:run -Dspring-boot.run.workingDirectory="$ROOT_DIR" &
BACKEND_PID=$!
cd ..

# ── 3. Frontend ───────────────────────────────────────────────────────────────
echo -e "\n⚛️  Starting React Frontend..."
if lsof -ti:5173 >/dev/null 2>&1; then
    echo "⚠️  Port 5173 already in use — killing existing process..."
    kill $(lsof -ti:5173) 2>/dev/null
    sleep 1
fi
cd frontend
npm install --silent
npm run dev &
FRONTEND_PID=$!
cd ..

# ── Ready ─────────────────────────────────────────────────────────────────────
echo -e "\n✅ All services started!"
echo -e "🌐 Open in browser: http://localhost:5173"
echo -e "🛑 Press CTRL+C to stop everything.\n"

# Keep script alive — trap handles CTRL+C
wait $BACKEND_PID $FRONTEND_PID
