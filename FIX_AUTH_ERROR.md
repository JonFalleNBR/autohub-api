# 🔧 SOLUÇÃO DO ERRO DE AUTENTICAÇÃO POSTGRESQL

## ❌ Erro que Você Está Vendo

```
FATAL: autenticação do tipo senha falhou para o usuário "autohub"
SQL State: 28P01
```

## 🎯 Causa

O banco de dados PostgreSQL não está rodando ou foi iniciado com credenciais diferentes.

---

## ✅ SOLUÇÃO RÁPIDA (RECOMENDADA)

### Passo 1: Resetar o Banco de Dados
```powershell
.\reset-db.ps1
```

Digite `yes` quando solicitado.

### Passo 2: Executar a Aplicação
Execute a aplicação Spring Boot pelo IntelliJ ou:
```powershell
.\mvnw spring-boot:run
```

**PRONTO!** A aplicação deve iniciar corretamente.

---

## 🚀 USO FUTURO

### Para iniciar o banco (sem resetar):
```powershell
.\start-db-quick.ps1
```

Este script:
- ✅ Verifica se o Docker está rodando
- ✅ Verifica se o banco já está rodando
- ✅ Inicia o banco se necessário (SEM perder dados)
- ✅ NÃO pede confirmação

### Para resetar o banco (limpar tudo):
```powershell
.\reset-db.ps1
```

Este script:
- ⚠️ DELETA todos os dados
- ✅ Recria o banco do zero
- ✅ Pede confirmação

---

## 📋 Checklist de Verificação

Antes de executar a aplicação, verifique:

### 1. Docker está rodando?
```powershell
docker ps
```

Se der erro, abra o **Docker Desktop**.

### 2. Container do banco está rodando?
```powershell
docker ps --filter "name=autohub-db"
```

Deve mostrar um container chamado `autohub-db`.

### 3. Banco está respondendo?
```powershell
docker exec autohub-db pg_isready -U autohub
```

Deve mostrar: `accepting connections`.

---

## 🔍 Diagnóstico de Problemas

### Problema: Docker não está rodando
**Solução:** Abra o Docker Desktop e aguarde iniciar.

### Problema: Container não inicia
**Verificar logs:**
```powershell
docker-compose logs db
```

**Solução:** Execute `.\reset-db.ps1`

### Problema: Porta 5432 já está em uso
**Verificar o que está usando:**
```powershell
netstat -ano | findstr :5432
```

**Solução 1:** Pare o processo que está usando a porta.

**Solução 2:** Mude a porta no `.env`:
```env
POSTGRES_PORT=5433
```

E no `application.yaml`:
```yaml
url: jdbc:postgresql://localhost:5433/autohub
```

### Problema: Senha incorreta
**Solução:** Execute `.\reset-db.ps1` para garantir que as credenciais estejam corretas.

---

## 📊 Estrutura de Arquivos

```
autohub-api/
├── docker-compose.yml           ← Configuração do banco
├── .env                         ← Credenciais (gitignored)
├── start-db-quick.ps1          ← Iniciar banco (rápido)
├── reset-db.ps1                ← Resetar banco (com confirmação)
└── src/main/resources/
    ├── application.yaml         ← Configuração Spring
    └── db/migration/            ← Migrations Flyway
        ├── V1__create_tenants.sql
        ├── V2__create_clientes.sql
        ├── V3__create_cars.sql
        └── V4__create_service_history.sql
```

---

## 🎯 Fluxo de Trabalho Recomendado

### Primeira vez (ou após problemas):
```powershell
# 1. Resetar banco (limpa tudo)
.\reset-db.ps1

# 2. Executar aplicação
# (Flyway criará as tabelas automaticamente)
```

### Uso diário:
```powershell
# 1. Iniciar banco (se não estiver rodando)
.\start-db-quick.ps1

# 2. Executar aplicação
```

### Quando precisar limpar dados:
```powershell
# Resetar banco
.\reset-db.ps1
```

---

## ✅ Verificação de Sucesso

Após executar `.\reset-db.ps1` ou `.\start-db-quick.ps1`, você deve ver:

```
✅ DATABASE IS READY!

Connection details:
  Host:     localhost:5432
  Database: autohub
  User:     autohub
  Password: autohub123
```

Então execute a aplicação e você deve ver:

```
✅ Flyway migration completed successfully
✅ Started AutohubApiApplication in X.XXX seconds
✅ Tomcat started on port(s): 8080 (http)
```

---

## 🆘 Ainda com Problemas?

### 1. Verificar se o .env existe:
```powershell
cat .env
```

Deve conter:
```env
POSTGRES_DB=autohub
POSTGRES_USER=autohub
POSTGRES_PASSWORD=autohub123
POSTGRES_PORT=5432
```

### 2. Verificar application.yaml:
```powershell
cat src/main/resources/application.yaml
```

Deve ter:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/autohub
    username: autohub
    password: autohub123
```

### 3. Limpar tudo e começar do zero:
```powershell
# Parar tudo
docker-compose down -v

# Remover volume manualmente
docker volume rm autohub-api_postgres_data -f

# Resetar
.\reset-db.ps1
```

---

## 📞 Comandos Úteis

```powershell
# Ver logs do banco em tempo real
docker-compose logs -f db

# Parar o banco
docker-compose down

# Parar e remover volumes (dados)
docker-compose down -v

# Entrar no PostgreSQL (linha de comando)
docker exec -it autohub-db psql -U autohub -d autohub

# Ver tabelas criadas
docker exec -it autohub-db psql -U autohub -d autohub -c "\dt"

# Ver status dos containers
docker ps -a
```

---

## 🎉 Resumo

1. ✅ Execute: `.\reset-db.ps1`
2. ✅ Digite: `yes`
3. ✅ Aguarde 8 segundos
4. ✅ Execute a aplicação Spring Boot
5. ✅ Sucesso! 🎉

**O erro de autenticação será resolvido!**

