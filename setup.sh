#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
# Service Clinic — Auto Setup Script (Linux / macOS)
# Installs: Docker, Node.js 18, Java JDK 21
# ─────────────────────────────────────────────────────────────────────────────

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

ok()   { echo -e "${GREEN}[✓]${NC} $1"; }
info() { echo -e "${YELLOW}[→]${NC} $1"; }
err()  { echo -e "${RED}[✗]${NC} $1"; exit 1; }

echo ""
echo "========================================"
echo "   Service Clinic — Setup Script"
echo "========================================"
echo ""

# ── Detect OS ────────────────────────────────────────────────────────────────
OS="$(uname -s)"
if [[ "$OS" == "Linux" ]]; then
    if ! command -v apt-get &>/dev/null; then
        err "This script supports Ubuntu/Debian only. Please install Docker, Node.js 18+, and Java 21 manually."
    fi
    LINUX=true
elif [[ "$OS" == "Darwin" ]]; then
    LINUX=false
else
    err "Unsupported OS: $OS. Please use setup.bat on Windows."
fi

# ── DOCKER ───────────────────────────────────────────────────────────────────
info "Checking Docker..."
if command -v docker &>/dev/null; then
    ok "Docker already installed ($(docker --version))"
else
    info "Installing Docker..."
    if $LINUX; then
        curl -fsSL https://get.docker.com | sudo sh
        sudo usermod -aG docker "$USER"
        ok "Docker installed. NOTE: Log out and back in for Docker to work without sudo."
    else
        err "On macOS please install Docker Desktop manually: https://www.docker.com/products/docker-desktop"
    fi
fi

# ── NODE.JS ──────────────────────────────────────────────────────────────────
info "Checking Node.js..."
NODE_OK=false
if command -v node &>/dev/null; then
    NODE_VER=$(node -e "console.log(parseInt(process.version.slice(1)))")
    if [[ "$NODE_VER" -ge 18 ]]; then
        NODE_OK=true
    fi
fi

if $NODE_OK; then
    ok "Node.js already installed ($(node --version))"
else
    info "Installing Node.js 18..."
    if $LINUX; then
        curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
        sudo apt-get install -y nodejs
        ok "Node.js installed ($(node --version))"
    else
        if ! command -v brew &>/dev/null; then
            info "Installing Homebrew first..."
            /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
        fi
        brew install node@18
        ok "Node.js installed ($(node --version))"
    fi
fi

# ── JAVA JDK 21 ──────────────────────────────────────────────────────────────
info "Checking Java..."
JAVA_OK=false
if command -v java &>/dev/null; then
    JAVA_VER=$(java -version 2>&1 | head -1 | sed 's/.*version "\([0-9]*\).*/\1/')
    if [[ "$JAVA_VER" -ge 21 ]]; then
        JAVA_OK=true
    fi
fi

if $JAVA_OK; then
    ok "Java already installed ($(java -version 2>&1 | head -1))"
else
    info "Installing Java JDK 21..."
    if $LINUX; then
        sudo apt-get update -qq
        sudo apt-get install -y openjdk-21-jdk
        ok "Java 21 installed"
    else
        if ! command -v brew &>/dev/null; then
            err "Homebrew not found. Install it first: https://brew.sh"
        fi
        brew install openjdk@21
        sudo ln -sfn "$(brew --prefix)/opt/openjdk@21/libexec/openjdk.jdk" /Library/Java/JavaVirtualMachines/openjdk-21.jdk
        ok "Java 21 installed"
    fi
fi

# ── DONE ─────────────────────────────────────────────────────────────────────
echo ""
echo "========================================"
ok "All requirements installed!"
echo "========================================"
echo ""
echo "Now run the app with 3 terminals:"
echo ""
echo "  Terminal 1:  docker compose up -d"
echo "  Terminal 2:  cd backend  && ./mvnw spring-boot:run"
echo "  Terminal 3:  cd frontend && npm install && npm run dev"
echo ""
echo "Then open: http://localhost:5173"
echo ""