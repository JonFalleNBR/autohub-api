# 🚗 Autohub API

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Database_Migrations-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-Multi--Tenant-blueviolet?style=for-the-badge)

Backend SaaS multi-tenant para gestão de oficinas automotivas.

Projeto desenvolvido com foco em arquitetura profissional, versionamento de banco, isolamento por tenant e boas práticas de engenharia de software.

---

## 🧱 Arquitetura

- Arquitetura em camadas (Controller → Service → Repository)
- Multi-tenancy por `tenant_id`
- Versionamento de banco com Flyway
- Validação dupla (Bean Validation + Constraints SQL)
- Ambiente reproduzível via Docker
- Hibernate com validação de schema

```
HTTP Request
     │
     ▼
┌─────────────┐
│  Controller │  ← Recebe a requisição, valida entrada, retorna resposta HTTP
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Service   │  ← Contém a lógica de negócio (regras, cálculos, orquestração)
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Repository │  ← Acessa o banco de dados via Spring Data JPA
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ PostgreSQL  │  ← Persistência, integridade referencial, índices
└─────────────┘
```

---

## 🗃️ Diagrama de Entidades (ER)

```
┌──────────────────────────────────────────────────────────────────┐
│                            TENANTS                               │
│  id (PK) │ name │ slug (UNIQUE) │ created_at                    │
└──────────┬───────────────────────────────────────────────────────┘
           │ 1
           │ possui muitos
           ├─────────────────────────────────────────┐
           │                                         │
           │ N                                       │ N
┌──────────▼───────────────────────────────┐  ┌─────▼───────────────────────────┐
│               CLIENTES                   │  │            USUARIOS              │
│  id (PK) │ tenant_id (FK) │ nome         │  │  id (PK) │ tenant_id (FK)        │
│  cpf (UNIQUE/tenant) │ telefone          │  │  nome │ email (UNIQUE/tenant)    │
│  email │ endereco                        │  │  senha │ role (LEITOR/ESCRITOR)  │
│  created_at │ updated_at                 │  │  ativo │ created_at │ updated_at  │
└──────────┬───────────────────────────────┘  └─────────────────────────────────┘
           │ 1
           │ tem muitos
           │ N
┌──────────▼────────────────────────────────────────────────┐
│                          CARS                              │
│  id (PK) │ tenant_id (FK) │ cliente_id (FK)               │
│  plate (UNIQUE/tenant) │ model │ brand                    │
│  color │ year │ created_at                                │
└──────────┬─────────────────────────────────────────────────┘
           │ 1
           │ tem muitos
           │ N
┌──────────▼───────────────────────────────────────────────────────────┐
│                        SERVICE_HISTORY                                │
│  id (PK) │ car_id (FK) │ tenant_id (FK)                              │
│  service_type │ description │ service_date                           │
│  cost │ mileage │ technician_name │ notes                            │
│  created_at │ updated_at                                             │
└──────────────────────────────────────────────────────────────────────┘
```

**Regras de negócio refletidas no banco:**
- CPF único por tenant: `UNIQUE (tenant_id, cpf)`
- Placa única por tenant: `UNIQUE (tenant_id, plate)`
- E-mail de usuário único por tenant: enforçado via repository + constraint

---

## 🛠️ Stack Tecnológica

| Tecnologia        | Versão |
|------------------|--------|
| Java             | 21     |
| Spring Boot      | 3.5.x  |
| Spring Data JPA  | ✓      |
| Hibernate        | 6.x    |
| PostgreSQL       | 16     |
| Flyway           | ✓      |
| Docker           | ✓      |
| Maven Wrapper    | ✓      |

---

## 📦 Entidades

| Entidade        | Tabela            | Descrição                                               |
|----------------|-------------------|---------------------------------------------------------|
| `Tenant`        | `tenants`         | Representa uma oficina/empresa no sistema               |
| `Cliente`       | `clientes`        | Cliente da oficina (dono do carro)                      |
| `Car`           | `cars`            | Veículo do cliente com FK para `tenants` e `clientes`   |
| `ServiceHistory`| `service_history` | Registro de serviços realizados no veículo              |
| `Usuario`       | `usuarios`        | Operador do sistema (mecânico, recepcionista, gerente)  |

---

## 🔎 Repositories

Cada entidade tem um repository dedicado que estende `JpaRepository`, fornecendo CRUD completo + queries específicas por domínio.

### `TenantRepository`

Responsável pelas operações de acesso à tabela `tenants`.

| Método                               | Descrição                                              |
|--------------------------------------|--------------------------------------------------------|
| `findBySlug(slug)`                   | Busca tenant pelo identificador de URL amigável        |
| `existsBySlug(slug)`                 | Valida unicidade de slug antes de criar tenant         |
| `findByName(name)`                   | Busca tenant pelo nome exato                           |
| `findByNameContainingIgnoreCase(…)`  | Busca tenants por parte do nome (case-insensitive)     |
| `existsByName(name)`                 | Verifica se já existe tenant com aquele nome           |
| `findAllOrderedByName()`             | Lista todos os tenants ordenados A→Z                   |

---

### `UsuarioRepository`

Responsável pelos operadores que acessam o sistema. Suporta duas roles:

- **`LEITOR`** → Somente visualização de dados
- **`ESCRITOR`** → Criação, edição e exclusão de dados

| Método                                        | Descrição                                              |
|-----------------------------------------------|--------------------------------------------------------|
| `findByTenantIdAndEmail(tenantId, email)`      | Login: localiza usuário pelo tenant + e-mail           |
| `existsByTenantIdAndEmail(tenantId, email)`    | Valida unicidade de e-mail dentro do tenant            |
| `findByTenantId(tenantId)`                    | Lista todos os usuários de um tenant                   |
| `findByTenantIdAndAtivoTrue(tenantId)`        | Lista apenas usuários ativos (podem fazer login)       |
| `findByTenantIdAndAtivoFalse(tenantId)`       | Lista usuários inativos (auditoria/reativação)         |
| `findByTenantIdAndRole(tenantId, role)`        | Filtra usuários por role dentro do tenant              |
| `findByTenantIdAndRoleAndAtivoTrue(…)`        | Usuários ativos com role específica                    |
| `existsByIdAndTenantId(id, tenantId)`         | Segurança: garante que o usuário pertence ao tenant    |
| `countActiveByTenantId(tenantId)`             | Conta usuários ativos (billing / limite de plano)      |
| `findByTenantIdAndNomeContainingIgnoreCase(…)` | Busca usuário por nome parcial no painel admin        |

---

### `ClienteRepository`

Gerencia os clientes das oficinas.

| Método                                           | Descrição                                      |
|--------------------------------------------------|------------------------------------------------|
| `findByTenantId(tenantId)`                       | Lista todos os clientes do tenant              |
| `findByTenantIdAndCpf(tenantId, cpf)`            | Busca cliente pelo CPF dentro do tenant        |
| `existsByCpf(cpf)`                               | Verifica existência por CPF                    |
| `findByTenantIdAndNomeContainingIgnoreCase(…)`   | Busca cliente por nome parcial                 |

---

### `CarRepository`

Gerencia os veículos, com FK para `clientes` e `tenants`.

| Método                                       | Descrição                                        |
|----------------------------------------------|--------------------------------------------------|
| `findByTenantId(tenantId)`                   | Lista todos os carros do tenant                  |
| `findByClienteId(clienteId)`                 | Lista todos os carros de um cliente              |
| `findByTenantIdAndPlate(tenantId, plate)`    | Busca carro pela placa dentro do tenant          |
| `existsByTenantIdAndPlate(tenantId, plate)`  | Valida unicidade de placa antes de cadastrar     |
| `findByTenantIdAndBrand(tenantId, brand)`    | Filtra carros por marca no tenant                |
| `findByTenantIdAndYear(tenantId, year)`      | Filtra carros por ano no tenant                  |
| `findByClienteIdOrderByCreatedAtDesc(…)`     | Histórico de carros do cliente (mais recente)    |

---

### `ServiceHistoryRepository`

Gerencia o histórico de serviços realizados em cada veículo.

| Método                                           | Descrição                                          |
|--------------------------------------------------|----------------------------------------------------|
| `findByCarIdOrderByServiceDateDesc(carId)`       | Histórico completo do carro (mais recente primeiro)|
| `findByCarIdAndServiceDateBetween(…)`            | Serviços de um carro em um intervalo de datas      |
| `findByCarIdAndServiceType(carId, type)`         | Filtra serviços por tipo (ex: "troca de óleo")     |
| `findByTenantIdOrderByServiceDateDesc(tenantId)` | Todos os serviços de um tenant ordenados por data  |
| `findTop5ByCarIdOrderByServiceDateDesc(carId)`   | Últimos 5 serviços de um veículo                   |

---

## 🗄️ Banco de Dados

### Conceitos aplicados

- UUID como chave primária
- Integridade referencial forte
- Índices estratégicos
- Constraint única composta `(tenant_id, cpf)` e `(tenant_id, plate)`
- Versionamento controlado por migrations

### Flyway Migrations

Localização:

```
src/main/resources/db/migration
```

| Migration                        | O que cria                    |
|----------------------------------|-------------------------------|
| `V1__create_tenants.sql`         | Tabela `tenants`              |
| `V2__create_clientes.sql`        | Tabela `clientes`             |
| `V3__create_cars.sql`            | Tabela `cars`                 |
| `V4__create_service_history.sql` | Tabela `service_history`      |
| `V5__create_usuarios.sql`        | Tabela `usuarios`             |

O banco é versionado automaticamente ao subir a aplicação.

---

## 🔐 Configuração

As configurações sensíveis devem ser definidas via variáveis de ambiente.

Crie um arquivo `.env` baseado no modelo:

```
DB_URL=
DB_USERNAME=
DB_PASSWORD=
```

Não versionar o arquivo `.env`.

---

## 🐳 Executando com Docker

### Subir banco + PgAdmin

```
docker-compose up -d
```

### Derrubar ambiente (removendo volume)

```
docker-compose down -v
```

### Ver containers ativos

```
docker ps
```

### Acessar banco manualmente

```
docker exec -it autohub-db psql -U autohub -d autohub
```

### PgAdmin

Acesse `http://localhost:5050` com as credenciais configuradas no `.env`.  
Crie um server apontando para `autohub-db:5432`.

---

## 🚀 Rodando a aplicação

### Via Maven Wrapper

Linux / Mac:

```
./mvnw spring-boot:run
```

Windows:

```
.\mvnw spring-boot:run
```

Aplicação sobe em:

```
http://localhost:8080
```

---

## 🧠 Conceitos Aplicados

- Multi-tenancy real por `tenant_id`
- Separação clara de responsabilidades
- Validação em múltiplas camadas (Bean Validation + SQL constraints)
- Versionamento de banco controlado
- Ambiente local reproduzível
- Estrutura preparada para JWT
- Estrutura pronta para DTO + Mapper
- Base preparada para CI/CD

---

## 📂 Estrutura do Projeto

```
com.autohub_api
 ├── controller       (vazio — próxima etapa)
 ├── service          (vazio — próxima etapa)
 ├── repository       ✅ implementado
 │    ├── TenantRepository
 │    ├── ClienteRepository
 │    ├── CarRepository
 │    ├── ServiceHistoryRepository
 │    └── UsuarioRepository
 ├── model
 │    ├── entity       ✅ implementado
 │    │    ├── Tenant
 │    │    ├── Cliente
 │    │    ├── Car
 │    │    ├── ServiceHistory
 │    │    └── Usuario
 │    └── enums
 │         └── UserRole (LEITOR / ESCRITOR)
 ├── config           (vazio — próxima etapa)
 ├── exception        (vazio — próxima etapa)
 └── validation
```

---

## 📌 Roadmap

- [x] Estrutura de entidades (Tenant, Cliente, Car, ServiceHistory, Usuario)
- [x] Migrations Flyway (V1 → V5)
- [x] Repositories com queries por domínio
- [x] Multi-tenancy por tenant_id
- [x] Docker + PgAdmin
- [ ] DTOs (Request / Response)
- [ ] Service layer (lógica de negócio)
- [ ] Controllers REST
- [ ] Tratamento global de exceções
- [ ] Autenticação JWT
- [ ] Filtro automático por tenant
- [ ] Testes com Testcontainers
- [ ] Deploy em ambiente cloud

---

## 👨‍💻 Autor

Desenvolvido como projeto de estudo para construção de um SaaS real de gestão automotiva com foco em arquitetura escalável.

---

## 🏁 Status

✔ Banco versionado  
✔ Multi-tenancy implementado  
✔ Entidades mapeadas  
✔ Repositories implementados  
🚧 Service layer em construção
