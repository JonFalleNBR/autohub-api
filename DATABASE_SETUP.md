# Database Setup Instructions

## Problem
The application is failing to connect to PostgreSQL with the error:
```
FATAL: autenticação do tipo senha falhou para o usuário "autohub"
```

## Solution

### Option 1: Start PostgreSQL with Docker Compose (Recommended)

1. Make sure Docker Desktop is running on your Windows machine

2. Open PowerShell in the project directory and run:
   ```powershell
   docker-compose up -d
   ```

3. Wait a few seconds for PostgreSQL to start

4. Verify the container is running:
   ```powershell
   docker-compose ps
   ```

5. Now run your Spring Boot application

### Option 2: Recreate the Database Container

If the database container exists but has wrong credentials:

1. Stop and remove the existing container:
   ```powershell
   docker-compose down -v
   ```
   Note: The `-v` flag removes volumes, which will delete existing data

2. Start fresh:
   ```powershell
   docker-compose up -d
   ```

3. Run your application

### Option 3: Check Existing PostgreSQL Instance

If you have PostgreSQL running locally (not in Docker):

1. Make sure the credentials match:
   - Database: `autohub`
   - User: `autohub`
   - Password: `autohub123`
   - Port: `5432`

2. If credentials are different, either:
   - Update the `.env` file with correct credentials, OR
   - Update your PostgreSQL user/database to match the `.env` file

## Verify Connection

After starting the database, you can test the connection:

```powershell
docker exec -it autohub-api-db-1 psql -U autohub -d autohub
```

Type the password when prompted: `autohub123`

If successful, you'll see the PostgreSQL prompt. Type `\q` to exit.

## Configuration

The application now reads database credentials from environment variables defined in `.env`:
- `POSTGRES_DB=autohub`
- `POSTGRES_USER=autohub`
- `POSTGRES_PASSWORD=autohub123`
- `POSTGRES_PORT=5432`

