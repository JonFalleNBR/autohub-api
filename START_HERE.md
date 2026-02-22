# START HERE - Quick Guide

## Problem: Authentication Error

```
FATAL: autenticacao do tipo senha falhou para o usuario "autohub"
```

## Solution (Run NOW!)

```powershell
.\setup.ps1
```

Type `sim` (yes) when asked.

**Alternative (if setup.ps1 has encoding issues):**

```powershell
.\setup-simple.ps1
```

Type `yes` when asked.

---

## What It Does

1. Checks if Docker is running
2. Stops old containers
3. Removes old volumes (clears data)
4. Starts fresh database
5. Waits for PostgreSQL to be ready

**Time: ~30 seconds**

---

## After Setup

Run your Spring Boot application:

```powershell
.\mvnw spring-boot:run
```

Or use IntelliJ IDEA Run button.

---

## Expected Result

```
[OK] Database started
Database is ready!

Connection Details:
  Host:     localhost:5432
  Database: autohub
  User:     autohub
  Password: autohub123

Ready to use!
```

Then in Spring Boot:

```
Flyway: Successfully applied 4 migrations
Started AutohubApiApplication in 5.234 seconds
Tomcat started on port(s): 8080 (http)
```

---

## Troubleshooting

### Error: "Docker is not running"
1. Open Docker Desktop
2. Wait for it to start
3. Run setup again

### Error: "Port 5432 already in use"
```powershell
docker-compose down
```

Then run setup again.

### Script has encoding errors
Use the simple version:
```powershell
.\setup-simple.ps1
```

---

## Daily Use

### Start database (without reset):
```powershell
.\start-db-quick.ps1
```

### Reset database (delete all data):
```powershell
.\reset-db.ps1
```

### Full setup:
```powershell
.\setup.ps1
```

---

## Quick Commands

```powershell
# View database logs
docker-compose logs -f db

# Stop database
docker-compose down

# Check if database is running
docker ps

# Connect to database
docker exec -it autohub-db psql -U autohub -d autohub
```

---

## Files Created

- `setup.ps1` - Full setup (recommended)
- `setup-simple.ps1` - Simple setup (backup)
- `start-db-quick.ps1` - Quick start
- `reset-db.ps1` - Reset database
- `README.md` - Full documentation
- `FIX_AUTH_ERROR.md` - Detailed troubleshooting

---

## Run Now!

```powershell
.\setup.ps1
```

If that fails due to encoding:

```powershell
.\setup-simple.ps1
```

Then:

```powershell
.\mvnw spring-boot:run
```

**Done!** Your application will be running at http://localhost:8080

