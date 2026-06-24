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
    docker compose stop
    echo -e "👋 All services stopped. Bye!\n"
    exit 0
}
trap cleanup SIGINT SIGTERM

# ── 1. Database ───────────────────────────────────────────────────────────────
echo -e "\n🚀 Starting Service Clinic App...\n"
echo "📦 Starting MySQL (Docker)..."
docker compose up -d

echo "⏳ Waiting for MySQL to be ready (15s)..."
sleep 15

# ── 2. Backend ────────────────────────────────────────────────────────────────
echo -e "\n☕ Starting Spring Boot Backend..."
cd backend
./mvnw clean spring-boot:run &
BACKEND_PID=$!
cd ..

# ── 3. Frontend ───────────────────────────────────────────────────────────────
echo -e "\n⚛️  Starting React Frontend..."
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
