# 🤖 AGENTS.md — Instruções para Agentes de IA

Este arquivo contém o mapa completo do projeto, convenções adotadas e os TODOs priorizados.
**Leia este arquivo antes de qualquer intervenção no código.**

---

## 📌 Contexto do Projeto

**Nome:** Autohub API  
**Tipo:** Backend SaaS multi-tenant para gestão de oficinas automotivas  
**Stack:** Java 21 · Spring Boot 3.5.x · PostgreSQL 16 · Flyway · Docker  
**Padrão:** Arquitetura em camadas (Controller → Service → Repository)  
**Build:** Maven Wrapper (`.\mvnw` no Windows, `./mvnw` no Linux/Mac)  

---

## 📂 Estrutura de Pacotes

```
com.autohub_api
 ├── controller/        ← ⬜ VAZIO — próxima etapa
 ├── service/           ← ⬜ VAZIO — próxima etapa
 ├── repository/        ← ✅ IMPLEMENTADO
 ├── model/
 │    ├── entity/       ← ✅ IMPLEMENTADO
 │    ├── enums/        ← ✅ IMPLEMENTADO
 │    └── dto/          ← ⬜ AINDA NÃO EXISTE — criar aqui
 ├── config/            ← ⬜ VAZIO — próxima etapa
 ├── exception/         ← ⬜ VAZIO — próxima etapa
 └── validation/        ← ⬜ VAZIO — próxima etapa
```

---

## ✅ O que já está feito

| Camada | Arquivo | Status |
|--------|---------|--------|
| Entity | `Tenant.java` | ✅ |
| Entity | `Cliente.java` | ✅ |
| Entity | `Car.java` | ✅ |
| Entity | `ServiceHistory.java` | ✅ |
| Entity | `Usuario.java` | ✅ |
| Enum | `UserRole.java` (LEITOR / ESCRITOR) | ✅ |
| Repository | `TenantRepository.java` | ✅ |
| Repository | `ClienteRepository.java` | ✅ |
| Repository | `CarRepository.java` | ✅ |
| Repository | `ServiceHistoryRepository.java` | ✅ |
| Repository | `UsuarioRepository.java` | ✅ |
| Migration | `V1__create_tenants.sql` | ✅ |
| Migration | `V2__create_clientes.sql` | ✅ |
| Migration | `V3__create_cars.sql` | ✅ |
| Migration | `V4__create_service_history.sql` | ✅ |
| Migration | `V5__create_usuarios.sql` | ✅ |
| Config | `application.yaml` (env vars) | ✅ |
| Infra | `docker-compose.yml` (PostgreSQL + PgAdmin) | ✅ |
| Infra | `.env` | ✅ |

---

## 🔴 TODOs — Ordem de Implementação

### TODO 1 — DTOs (Request / Response)
**Pacote:** `com.autohub_api.model.dto`  
**Criar subpacotes:** `request/` e `response/`

Criar um DTO de Request (entrada) e um de Response (saída) para cada entidade.
Não expor entidades JPA diretamente nos controllers.

| DTO | Campos obrigatórios |
|-----|-------------------|
| `TenantRequest` | `name`, `slug` |
| `TenantResponse` | `id`, `name`, `slug`, `createdAt` |
| `ClienteRequest` | `tenantId`, `nome`, `cpf`, `telefone`, `email`, `endereco` |
| `ClienteResponse` | `id`, `tenantId`, `nome`, `cpf`, `telefone`, `email`, `endereco`, `createdAt`, `updatedAt` |
| `CarRequest` | `tenantId`, `clienteId`, `plate`, `model`, `brand`, `color`, `year` |
| `CarResponse` | `id`, `tenantId`, `clienteId`, `plate`, `model`, `brand`, `color`, `year`, `createdAt` |
| `ServiceHistoryRequest` | `carId`, `tenantId`, `serviceType`, `description`, `serviceDate`, `cost`, `mileage`, `technicianName`, `notes` |
| `ServiceHistoryResponse` | todos os campos + `id`, `createdAt`, `updatedAt` |
| `UsuarioRequest` | `tenantId`, `nome`, `email`, `senha`, `role` |
| `UsuarioResponse` | `id`, `tenantId`, `nome`, `email`, `role`, `ativo`, `createdAt` — **nunca incluir `senha`** |

**Convenções:**
- Use `record` do Java 21 para DTOs imutáveis quando possível
- Anote campos obrigatórios com `@NotBlank`, `@NotNull`, `@Valid`
- `UsuarioResponse` NUNCA deve expor o campo `senha`

---

### TODO 2 — Exception Handler Global
**Pacote:** `com.autohub_api.exception`

Criar antes do Service para padronizar respostas de erro.

| Arquivo | Responsabilidade |
|---------|-----------------|
| `ResourceNotFoundException.java` | Lançada quando entidade não é encontrada (404) |
| `BusinessException.java` | Erros de regra de negócio (409/422) |
| `GlobalExceptionHandler.java` | `@RestControllerAdvice` — captura todas as exceções e retorna JSON padronizado |

**Formato padrão de resposta de erro:**
```json
{
  "timestamp": "2026-04-29T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente não encontrado para o tenant informado",
  "path": "/clientes/abc-123"
}
```

---

### TODO 3 — Service Layer
**Pacote:** `com.autohub_api.service`

Um Service por entidade com interface + implementação.

| Interface | Implementação | Responsabilidade |
|-----------|---------------|-----------------|
| `TenantService` | `TenantServiceImpl` | CRUD de tenants |
| `ClienteService` | `ClienteServiceImpl` | CRUD de clientes com validação de CPF por tenant |
| `CarService` | `CarServiceImpl` | CRUD de carros com validação de placa por tenant |
| `ServiceHistoryService` | `ServiceHistoryServiceImpl` | CRUD de histórico de serviços |
| `UsuarioService` | `UsuarioServiceImpl` | CRUD de usuários, ativação/desativação, troca de role |

**Regras a enforçar no Service (não no Controller):**
- CPF único por tenant → `ClienteRepository.existsByTenantIdAndCpf` antes de criar
- Placa única por tenant → `CarRepository.existsByTenantIdAndPlate` antes de criar
- E-mail único por tenant → `UsuarioRepository.existsByTenantIdAndEmail` antes de criar
- Para GET/PUT/DELETE por ID: sempre usar `findByIdAndTenantId` para garantir isolamento multi-tenant
- Senhas de usuário: **nunca salvar em texto puro** — aguardar TODO 5 (Spring Security) para hash com BCrypt

---

### TODO 4 — Controllers REST
**Pacote:** `com.autohub_api.controller`  
**Prefixo de URL:** `/api/v1`

| Controller | Base URL | Operações |
|------------|----------|-----------|
| `TenantController` | `/api/v1/tenants` | POST, GET (all), GET (by id), PUT, DELETE |
| `ClienteController` | `/api/v1/clientes` | POST, GET (all by tenant), GET (by id), PUT, DELETE |
| `CarController` | `/api/v1/cars` | POST, GET (all by tenant), GET (by id), GET (by cliente), PUT, DELETE |
| `ServiceHistoryController` | `/api/v1/service-history` | POST, GET (by car), GET (by tenant), GET (by id), PUT, DELETE |
| `UsuarioController` | `/api/v1/usuarios` | POST, GET (all by tenant), GET (by id), PUT, PATCH (ativo), DELETE |

**Convenções:**
- Anotar com `@RestController`, `@RequestMapping`, `@Validated`
- Retornar `ResponseEntity<T>` sempre
- POST retorna `201 Created` com o DTO de response no body
- DELETE retorna `204 No Content`
- Usar `@PathVariable` para IDs, `@RequestBody @Valid` para entrada

---

### TODO 5 — Autenticação JWT (Spring Security)
**Pacote:** `com.autohub_api.config` e `com.autohub_api.security`

Implementar após controllers estarem funcionando.

- Adicionar dependência `spring-boot-starter-security` no `pom.xml`
- `JwtUtil.java` — geração e validação de tokens
- `JwtFilter.java` — filtro que extrai o token do header `Authorization: Bearer <token>`
- `SecurityConfig.java` — define quais rotas são públicas (ex: `/auth/login`) e quais exigem token
- Endpoint `POST /auth/login` — recebe email + senha, retorna JWT
- Usar BCrypt para hash de senha ao criar usuário (`PasswordEncoder`)
- Payload do JWT deve conter: `tenantId`, `usuarioId`, `role`

---

### TODO 6 — Filtro Automático por Tenant
Após JWT implementado, injetar `tenantId` automaticamente via token — não exigir que o cliente mande `tenantId` no body.

- Criar `TenantContext.java` com `ThreadLocal<UUID>` para armazenar o tenant da requisição
- `JwtFilter` popula o `TenantContext` ao validar o token
- Services usam `TenantContext.get()` ao invés de receber `tenantId` por parâmetro

---

### TODO 7 — Testes com Testcontainers
**Pacote:** `src/test/java/com/autohub_api`

- Adicionar dependências `testcontainers` e `spring-boot-testcontainers` no `pom.xml`
- Criar `AbstractIntegrationTest.java` com configuração do container PostgreSQL
- Testes de integração para cada Repository
- Testes de integração para cada Service (regras de negócio)
- Testes de controller com `MockMvc`

---

## 🔧 Convenções do Projeto

### Nomenclatura
- Entidades em **PascalCase** português/inglês (ex: `Cliente`, `ServiceHistory`)
- Tabelas SQL em **snake_case** português (ex: `clientes`, `service_history`)
- Métodos de Repository seguem os padrões **Spring Data JPA Derived Query**
- Mensagens de erro em **português**

### Banco de Dados
- **NUNCA** usar `ddl-auto: create` ou `update` — somente `validate`
- **TODA** alteração de schema via Flyway migration (`V6__...`, `V7__...`, etc.)
- Próxima migration disponível: **`V6__`**
- UUIDs gerados pelo banco (`gen_random_uuid()`) ou pelo Hibernate (`GenerationType.UUID`)

### Variáveis de Ambiente
Todas as credenciais vêm do `.env` (nunca hardcodadas):

| Variável | Uso |
|----------|-----|
| `DB_URL` | JDBC URL do PostgreSQL |
| `DB_USERNAME` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |
| `POSTGRES_DB` | Nome do banco (Docker) |
| `POSTGRES_USER` | Usuário (Docker) |
| `POSTGRES_PASSWORD` | Senha (Docker) |
| `POSTGRES_PORT` | Porta exposta no host (padrão: 5433) |
| `PGADMIN_EMAIL` | Login do PgAdmin |
| `PGADMIN_PASSWORD` | Senha do PgAdmin |

### Docker
- Container do banco: `autohub-db`
- Container do PgAdmin: porta `5050` no host
- Subir ambiente: `docker-compose up -d`
- Reset completo: `docker-compose down -v && docker-compose up -d`

---

## 🚦 Comandos Essenciais

```bash
# Subir banco
docker-compose up -d

# Rodar aplicação
.\mvnw spring-boot:run          # Windows
./mvnw spring-boot:run           # Linux/Mac

# Compilar sem rodar
.\mvnw compile

# Rodar testes
.\mvnw test

# Verificar migrations aplicadas
docker exec -it autohub-db psql -U autohub -d autohub -c "SELECT version, description, success FROM flyway_schema_history ORDER BY installed_rank;"

# Listar tabelas no banco
docker exec -it autohub-db psql -U autohub -d autohub -c "\dt"
```

---

## ⚠️ Avisos Importantes

1. **Senha nunca em texto puro** — aguardar BCrypt no TODO 5 antes de criar endpoint de usuário
2. **Nunca expor `senha` em DTOs de response**
3. **Toda nova tabela** exige migration Flyway — não usar `ddl-auto: update`
4. **Isolamento multi-tenant** — todo `findById` deve ser `findByIdAndTenantId`
5. **`service/` e `controller/` estão vazios** — não alterar repositories ou entities sem verificar o que está nessas pastas primeiro

