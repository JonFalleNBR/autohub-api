# 🚗 AutoHub API

Sistema de gerenciamento de oficina mecânica com Spring Boot.

---

## ⚡ INÍCIO RÁPIDO

### 1️⃣ Setup Inicial (Primeira Vez)
```powershell
.\setup.ps1
```

Digite `sim` quando solicitado.

### 2️⃣ Executar Aplicação
Execute pelo IntelliJ ou:
```powershell
.\mvnw spring-boot:run
```

✅ **Pronto!** A aplicação estará rodando em `http://localhost:8080`

---

## 📋 Pré-requisitos

- ☕ Java 21
- 🐳 Docker Desktop
- 📦 Maven (incluído via mvnw)

---

## 🔧 Scripts Disponíveis

### `setup.ps1` (Recomendado)
Setup completo automático. Usa na primeira vez ou quando tiver problemas.
```powershell
.\setup.ps1
```

### `start-db-quick.ps1`
Inicia o banco rapidamente (sem resetar dados).
```powershell
.\start-db-quick.ps1
```

### `reset-db.ps1`
Reseta o banco (DELETA todos os dados).
```powershell
.\reset-db.ps1
```

---

## 🗄️ Estrutura do Banco

```
tenants (Oficinas)
    ↓
clientes (Donos dos carros)
    ↓
cars (Carros)
    ↓
service_history (Histórico de serviços)
```

### Migrations Flyway:
- `V1__create_tenants.sql` - Tabela de oficinas
- `V2__create_clientes.sql` - Tabela de clientes
- `V3__create_cars.sql` - Tabela de carros
- `V4__create_service_history.sql` - Histórico de serviços

---

## 🚨 Problemas Comuns

### Erro: "autenticação do tipo senha falhou"
**Solução:**
```powershell
.\setup.ps1
```

### Erro: "Docker is not running"
**Solução:**
1. Abra o Docker Desktop
2. Aguarde iniciar
3. Execute `.\setup.ps1` novamente

### Erro: "Port 5432 is already in use"
**Solução 1:** Pare o processo na porta 5432
```powershell
netstat -ano | findstr :5432
```

**Solução 2:** Mude a porta no `.env`:
```env
POSTGRES_PORT=5433
```

---

## 📚 Documentação

- **FIX_AUTH_ERROR.md** - Solução detalhada de problemas de autenticação
- **ESTRUTURA_CORRETA_CLIENTE_CARRO.md** - Modelo de dados completo
- **GUIA_FINAL_EXECUCAO.md** - Guia passo a passo

---

## 🔗 Endpoints

### API Documentation (Swagger)
```
http://localhost:8080/swagger-ui.html
```

### Health Check
```
http://localhost:8080/actuator/health
```

---

## 🛠️ Desenvolvimento

### Estrutura do Projeto
```
src/main/
├── java/com/autohub_api/
│   ├── model/entity/         # Entidades JPA
│   │   ├── Tenant.java
│   │   ├── Cliente.java
│   │   ├── Car.java
│   │   └── ServiceHistory.java
│   │
│   └── repository/           # Repositories Spring Data
│       ├── TenantRepository.java
│       ├── ClienteRepository.java
│       ├── CarRepository.java
│       └── ServiceHistoryRepository.java
│
└── resources/
    ├── application.yaml      # Configuração
    └── db/migration/         # Migrations Flyway
```

### Comandos Maven
```powershell
# Compilar
.\mvnw clean install

# Executar
.\mvnw spring-boot:run

# Testes
.\mvnw test
```

### Comandos Docker
```powershell
# Ver logs do banco
docker-compose logs -f db

# Parar banco
docker-compose down

# Parar e limpar volumes
docker-compose down -v

# Ver containers rodando
docker ps

# Entrar no PostgreSQL
docker exec -it autohub-db psql -U autohub -d autohub
```

---

## 📊 Variáveis de Ambiente

Arquivo `.env` (já configurado):
```env
POSTGRES_DB=autohub
POSTGRES_USER=autohub
POSTGRES_PASSWORD=autohub123
POSTGRES_PORT=5432
```

---

## ✅ Checklist de Funcionamento

Após executar `.\setup.ps1`, você deve ver:

1. ✅ Docker rodando
2. ✅ Container `autohub-db` ativo
3. ✅ PostgreSQL aceitando conexões
4. ✅ Aplicação Spring Boot iniciando
5. ✅ Flyway criando 4 tabelas
6. ✅ Tomcat rodando na porta 8080

---

## 🎯 Fluxo de Trabalho

### Primeira vez:
```powershell
.\setup.ps1              # Setup inicial
.\mvnw spring-boot:run   # Executar app
```

### Uso diário:
```powershell
.\start-db-quick.ps1     # Iniciar banco
.\mvnw spring-boot:run   # Executar app
```

### Limpar dados:
```powershell
.\reset-db.ps1           # Resetar banco
.\mvnw spring-boot:run   # Executar app (Flyway recriará tabelas)
```

---

## 📞 Suporte

Leia a documentação completa em:
- `FIX_AUTH_ERROR.md` - Problemas de autenticação
- `ESTRUTURA_CORRETA_CLIENTE_CARRO.md` - Modelo de dados

---

## 🎉 Status

✅ Banco de dados configurado  
✅ Migrations Flyway prontas  
✅ Entidades JPA completas  
✅ Repositories funcionais  
✅ Scripts automatizados  

**Pronto para desenvolvimento!** 🚀

