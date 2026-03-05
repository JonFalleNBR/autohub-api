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

## 🗄️ Banco de Dados

### Entidades principais

- `tenants`
- `clientes`
- `cars`
- `service_history`

### Conceitos aplicados

- UUID como chave primária
- Integridade referencial forte
- Índices estratégicos
- Constraint única composta `(tenant_id, cpf)`
- Versionamento controlado por migrations

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

### Subir banco

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

---

## 🚀 Rodando a aplicação

### Via Maven Wrapper

Linux / Mac:

```
./mvnw spring-boot:run
```

Windows:

```
mvnw spring-boot:run
```

Aplicação sobe em:

```
http://localhost:8080
```

---

## 🔄 Flyway Migrations

Localização:

```
src/main/resources/db/migration
```

Padrão utilizado:

```
V1__create_tenants.sql
V2__create_clientes.sql
V3__create_cars.sql
V4__create_service_history.sql
```

O banco é versionado automaticamente ao subir a aplicação.

---

## 🧠 Conceitos Aplicados

- Multi-tenancy real por `tenant_id`
- Separação clara de responsabilidades
- Validação em múltiplas camadas
- Versionamento de banco controlado
- Ambiente local reproduzível
- Estrutura preparada para JWT
- Estrutura pronta para DTO + Mapper
- Base preparada para CI/CD

---

## 📌 Roadmap

- [ ] Filtro automático por tenant
- [ ] Autenticação JWT
- [ ] DTO + Mapper
- [ ] Testes com Testcontainers
- [ ] OpenAPI / Swagger
- [ ] Deploy em ambiente cloud

---

## 📂 Estrutura do Projeto

```
com.autohub_api
 ├── controller
 ├── service
 ├── repository
 ├── model.entity
 └── config
```

---

## 👨‍💻 Autor

Desenvolvido como projeto de estudo para construção de um SaaS real de gestão automotiva com foco em arquitetura escalável.

---

## 🏁 Status

✔ Banco versionado  
✔ Multi-tenancy implementado  
✔ API funcional  
🚧 Em evolução contínua
