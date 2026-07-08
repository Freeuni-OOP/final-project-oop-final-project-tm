[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/skmUAHf8)
# Final-Project
OOP ფინალური პროექტი საბოლოო :DDDD

# 🔧 Service Clinic — Booking System

## 1. Install Requirements

Run the setup script — it installs everything automatically (Docker, Node.js, Java 21):

**Linux / macOS:**
```bash
chmod +x setup.sh && ./setup.sh
```

**Windows** — right-click `setup.bat` → **Run as Administrator**

---

## 2. Run the App

One command starts everything (database + backend + frontend):

**Linux / macOS:**
```bash
chmod +x start.sh && ./start.sh
```

**Windows** — double-click `start.bat`

Then open **http://localhost:5173** in your browser. ✅

Press **CTRL+C** to stop everything cleanly.

---

> If you prefer to start services manually, run these in 3 separate terminals:
> ```bash
> docker compose up -d                        # Terminal 1 — Database
> cd backend  && ./mvnw spring-boot:run       # Terminal 2 — Backend
> cd frontend && npm install && npm run dev   # Terminal 3 — Frontend
> ```
