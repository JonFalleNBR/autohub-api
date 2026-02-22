# Quick Fix for Database Connection Error

## The Error
```
FATAL: autenticação do tipo senha falhou para o usuário "autohub"
(Password authentication failed for user "autohub")
```

## Quick Fix Steps

### Step 1: Start the Database

**Option A - Using the helper script (easiest):**
```powershell
.\start-db.ps1
```

**Option B - Manually:**
```powershell
docker-compose up -d
```

### Step 2: Reload Maven Dependencies

Since we added a new dependency to load `.env` files, you need to reload:

1. In IntelliJ IDEA, open the Maven tool window (View → Tool Windows → Maven)
2. Click the "Reload All Maven Projects" button (circular arrow icon)
3. Wait for dependencies to download

### Step 3: Run Your Application

Now run your Spring Boot application again.

---

## If Database Container Already Exists with Wrong Password

If you see the error even after starting docker-compose, the container might have been created before with different credentials:

**Option A - Using the helper script:**
```powershell
.\reset-db.ps1
```

**Option B - Manually:**
```powershell
docker-compose down -v
docker-compose up -d
```

**Note:** The `-v` flag removes volumes and **will delete all existing data**.

---

## What Was Changed

1. **application.yaml** - Now uses environment variables from `.env` file:
   ```yaml
   datasource:
     url: jdbc:postgresql://localhost:${POSTGRES_PORT:5432}/${POSTGRES_DB:autohub}
     username: ${POSTGRES_USER:autohub}
     password: ${POSTGRES_PASSWORD:autohub123}
   ```

2. **pom.xml** - Added `spring-dotenv` dependency to load `.env` files automatically

3. **Helper scripts** created:
   - `start-db.ps1` - Start the database
   - `reset-db.ps1` - Reset the database (removes all data)

---

## Troubleshooting

### Docker not running
- Start Docker Desktop
- Wait until it's fully started
- Try again

### Port 5432 already in use
- Check if you have another PostgreSQL instance running
- Either stop it or change the port in `.env` file

### Still getting authentication error
1. Make sure you reloaded Maven dependencies
2. Try resetting the database: `.\reset-db.ps1`
3. Check that `.env` file is in the project root
4. Restart your IDE

---

## Verify Database is Running

```powershell
# Check container status
docker-compose ps

# View logs
docker-compose logs -f

# Connect to database (to test credentials)
docker exec -it autohub-api-db-1 psql -U autohub -d autohub
# Password: autohub123
# Type \q to exit
```

