#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
# reset-db.sh
# Drops the project database and re-runs every V*.sql migration file in
# backend/src/main/resources/db/migration in version order.
#
# Usage: ./reset-db.sh
#
# Requires: docker-compose mysql container ("project_db") running.
# After this script finishes, restart the backend — Flyway will baseline the
# schema automatically (baseline-on-migrate=true in application.properties).
# ─────────────────────────────────────────────────────────────────────────────

set -e

DB_NAME="project_db"
DB_USER="student"
DB_PASS="password"
ROOT_PASS="rootpassword"
CONTAINER_NAME="project_db"

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
MIGRATIONS_DIR="$SCRIPT_DIR/backend/src/main/resources/db/migration"

# ── 1. Make sure the MySQL container is up ────────────────────────────────────
if ! docker ps --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    echo "📦 MySQL container '$CONTAINER_NAME' is not running. Starting it now..."
    (cd "$SCRIPT_DIR" && docker compose up -d)
    echo "⏳ Waiting 10s for MySQL to be ready..."
    sleep 10
fi

# ── 2. Drop & recreate the database ───────────────────────────────────────────
echo "🗑️  Dropping and recreating database '$DB_NAME'..."
docker exec -i "$CONTAINER_NAME" mysql -u root -p"$ROOT_PASS" <<SQL
DROP DATABASE IF EXISTS $DB_NAME;
CREATE DATABASE $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON $DB_NAME.* TO '$DB_USER'@'%';
FLUSH PRIVILEGES;
SQL

# ── 3. Run every migration in version order ───────────────────────────────────
if [ ! -d "$MIGRATIONS_DIR" ]; then
    echo "❌ Migrations directory not found: $MIGRATIONS_DIR"
    exit 1
fi

echo "▶️  Running migrations from $MIGRATIONS_DIR"
for f in $(ls "$MIGRATIONS_DIR"/V*.sql | sort -V); do
    name=$(basename "$f")
    echo "   • $name"
    docker exec -i "$CONTAINER_NAME" mysql -u "$DB_USER" -p"$DB_PASS" "$DB_NAME" < "$f"
done

echo ""
echo "✅ Database '$DB_NAME' has been reset and all SQL files applied."
echo "ℹ️  Start the backend (./start.sh) — Flyway will baseline the schema."
