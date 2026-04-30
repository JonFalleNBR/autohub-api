# 🤖 GitHub Copilot — Instruções Permanentes do Projeto

Este arquivo é lido automaticamente pelo agente do GitHub Copilot em toda interação.
Siga **todas** as instruções abaixo sem exceção antes de gerar ou alterar qualquer código.

---

## 🧭 Identidade do Projeto

- **Nome:** Autohub API
- **Tipo:** Backend SaaS multi-tenant para gestão de oficinas automotivas
- **Stack:** Java 21 · Spring Boot 3.5.x · Spring Data JPA · PostgreSQL 16 · Flyway · Docker
- **Arquitetura:** Layered Architecture — Controller → Service → Repository
- **Build:** Maven Wrapper (`.\mvnw` Windows / `./mvnw` Linux/Mac)
- **Mapa completo do projeto:** leia `AGENTS.md` na raiz antes de qualquer mudança

---

## ✅ Antes de Escrever Qualquer Código

1. Leia `AGENTS.md` para entender o estado atual do projeto e os TODOs pendentes
2. Verifique os pacotes `service/`, `controller/`, `exception/` — podem estar vazios
3. Nunca recriar arquivos que já existem (entities, repositories, migrations)
4. Confirme qual é a próxima migration disponível antes de criar uma nova (`V6__`, `V7__`...)

---

## 🏗️ Padrões de Geração de Código

### Geral
- Linguagem de código: **Java 21**
- Idioma dos comentários e Javadoc: **Português**
- Idioma das mensagens de erro/validação: **Português**
- Idioma de nomes de métodos, variáveis e classes: **Inglês** (padrão Java)
- Sempre gerar **Javadoc** em interfaces e métodos públicos relevantes

---

## 🧱 SOLID — Princípios Obrigatórios

Todo código gerado neste projeto **deve respeitar os 5 princípios SOLID**:

### S — Single Responsibility Principle (Responsabilidade Única)
- Cada classe tem **uma única razão para mudar**
- `Controller` → apenas recebe requisição e delega
- `Service` → apenas executa lógica de negócio
- `Repository` → apenas acessa banco de dados
- Nunca misturar responsabilidades entre camadas

```java
// ✅ certo — Service tem responsabilidade única
@Service
public class ClienteServiceImpl implements ClienteService {
    public ClienteResponse criar(ClienteRequest request) { ... }
}

// ❌ errado — Controller executando lógica de negócio
@RestController
public class ClienteController {
    public ResponseEntity<?> criar(...) {
        if (repository.existsByCpf(cpf)) throw ...; // regra de negócio no controller!
    }
}
```

### O — Open/Closed Principle (Aberto/Fechado)
- Classes abertas para **extensão**, fechadas para **modificação**
- Usar interfaces para Services — nunca depender da implementação concreta
- Novos comportamentos via novas classes, não alterando as existentes

```java
// ✅ certo — Controller depende da interface, não da implementação
private final ClienteService clienteService; // interface
// não: private final ClienteServiceImpl ...
```

### L — Liskov Substitution Principle (Substituição de Liskov)
- Implementações (`ServiceImpl`) devem ser substituíveis pelas suas interfaces (`Service`)
- Nunca lançar exceções inesperadas ou alterar contratos definidos na interface

### I — Interface Segregation Principle (Segregação de Interface)
- Interfaces pequenas e específicas por domínio
- Não criar uma interface gigante `CrudService` genérica para todos os serviços
- Cada `Service` declara apenas os métodos que seu domínio precisa

```java
// ✅ certo — interface coesa
public interface UsuarioService {
    UsuarioResponse criar(UsuarioRequest request);
    UsuarioResponse buscarPorId(UUID id, UUID tenantId);
    List<UsuarioResponse> listarPorTenant(UUID tenantId);
    UsuarioResponse alterarRole(UUID id, UUID tenantId, UserRole novaRole);
    void ativar(UUID id, UUID tenantId);
    void desativar(UUID id, UUID tenantId);
    void deletar(UUID id, UUID tenantId);
}
```

### D — Dependency Inversion Principle (Inversão de Dependência)
- Módulos de alto nível (Service) não dependem de módulos de baixo nível (Repository diretamente)
- Ambos dependem de **abstrações** (interfaces)
- Injeção de dependência sempre via **construtor** (não `@Autowired` em campo)

```java
// ✅ certo — injeção via construtor
@Service
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepository;
    private final TenantRepository tenantRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository,
                               TenantRepository tenantRepository) {
        this.clienteRepository = clienteRepository;
        this.tenantRepository = tenantRepository;
    }
}

// ❌ evitar — injeção por campo dificulta testes
@Autowired
private ClienteRepository clienteRepository;
```

---

## 🧼 Clean Code — Boas Práticas Obrigatórias

### Nomenclatura
- Nomes de variáveis e métodos devem **revelar intenção** — sem abreviações obscuras
- Evitar nomes genéricos: `data`, `obj`, `temp`, `result` → use `clienteEncontrado`, `carrosDoTenant`
- Métodos devem ser **verbos**: `buscarCliente`, `validarCpf`, `calcularCusto`
- Classes devem ser **substantivos**: `ClienteService`, `TenantRepository`
- Booleanos com prefixo `is`, `has`, `exists`: `isAtivo`, `hasPermissao`, `existsByEmail`

### Funções / Métodos
- Cada método faz **uma coisa apenas**
- Máximo de **3 parâmetros** por método — se precisar de mais, encapsular em objeto/DTO
- Evitar flags booleanas como parâmetro (`processarCliente(true)` — o que significa `true`?)
- Métodos curtos: idealmente **até 20 linhas**

```java
// ✅ certo — método com responsabilidade única e nome claro
private void validarCpfUnicoNoTenant(String cpf, UUID tenantId) {
    if (clienteRepository.existsByTenantIdAndCpf(tenantId, cpf)) {
        throw new BusinessException("CPF já cadastrado para este tenant");
    }
}

// ❌ evitar — método longo fazendo múltiplas coisas
public ClienteResponse criar(ClienteRequest req) {
    // 50 linhas misturando validação, busca, conversão, persistência...
}
```

### Comentários
- **Código deve se auto-documentar** — comentário é sinal de código confuso
- Comentários em **Javadoc** para interfaces e métodos públicos de Service
- Nunca comentar código morto — deletar, não comentar
- Comentários de bloco (`// ────`) são aceitos para separar seções em repositories

### Tratamento de Erros
- Nunca retornar `null` — use `Optional<T>` ou lance exceção
- Nunca engolir exceções com catch vazio
- Sempre lançar exceções de domínio (`ResourceNotFoundException`, `BusinessException`)

```java
// ✅ certo
return clienteRepository.findByIdAndTenantId(id, tenantId)
    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

// ❌ errado
Cliente c = clienteRepository.findById(id).orElse(null);
if (c == null) return null;
```

### Estrutura de Classes
- **Ordem padrão** dentro de uma classe Service:
  1. Constante / campos finais
  2. Construtor
  3. Métodos públicos (CRUD principal)
  4. Métodos privados de validação / conversão
- Não misturar lógica pública com helpers privados sem separação visual clara

### Entidades JPA
- Nunca usar `ddl-auto: create` ou `update` — sempre `validate`
- UUID como `@Id` com `@GeneratedValue(strategy = GenerationType.UUID)`
- Timestamps: `@CreationTimestamp` / `@UpdateTimestamp` do Hibernate, ou `@PrePersist` / `@PreUpdate`
- Relacionamentos `@ManyToOne` devem usar `fetch = FetchType.LAZY`
- Toda nova entidade precisa de uma migration Flyway correspondente

### DTOs
- Usar `record` do Java 21 sempre que o DTO for imutável (Request e Response)
- Campos obrigatórios anotados com `@NotBlank`, `@NotNull`, `@Valid`
- **Nunca** incluir o campo `senha` em DTOs de Response
- Pacote: `com.autohub_api.model.dto.request` e `com.autohub_api.model.dto.response`

### Repositories
- Estender `JpaRepository<Entidade, UUID>`
- Anotar com `@Repository`
- Métodos de busca por ID com isolamento de tenant: `findByIdAndTenantId(UUID id, UUID tenantId)`
- Métodos `existsBy...` para validações de unicidade antes de criar/atualizar
- Usar `@Query` com JPQL apenas quando Derived Query não for suficiente

### Services
- Criar **interface** + **implementação** (`ServiceImpl`) por entidade
- Anotar implementação com `@Service` e `@Transactional` onde necessário
- Toda lógica de negócio fica no Service — nunca no Controller
- Regras de unicidade (CPF, placa, e-mail) validadas **antes** de persistir
- Para busca por ID, usar sempre `findByIdAndTenantId` e lançar `ResourceNotFoundException` se não encontrar
- **Nunca salvar senha em texto puro** — aguardar BCrypt (TODO 5)

### Controllers
- Anotar com `@RestController`, `@RequestMapping`, `@Validated`
- Retornar sempre `ResponseEntity<T>`
- POST → `ResponseEntity.status(HttpStatus.CREATED).body(response)`
- DELETE → `ResponseEntity.noContent().build()`
- GET não encontrado → lançar `ResourceNotFoundException` no Service (não no Controller)
- Prefixo de URL: `/api/v1`

### Exceptions
- `ResourceNotFoundException` → HTTP 404
- `BusinessException` → HTTP 409 (conflito) ou 422 (regra de negócio)
- `GlobalExceptionHandler` com `@RestControllerAdvice` captura tudo

### Banco de Dados / Migrations
- **Toda** alteração de schema via Flyway — nunca via `ddl-auto`
- Nome do arquivo: `V{numero}__{descricao_snake_case}.sql`
- Usar `gen_random_uuid()` para UUIDs no SQL
- Sempre criar índices para FKs e colunas usadas em WHERE frequente
- Constraints de unicidade compostas como índice: `CREATE UNIQUE INDEX uq_... ON tabela(col1, col2)`

---

## 🔒 Segurança — Regras Absolutas

| Regra | Detalhe |
|-------|---------|
| Nunca expor `senha` | Jamais incluir o campo `senha` em qualquer DTO de Response |
| Nunca hardcodar credenciais | Toda senha/usuário/URL vem de variáveis de ambiente do `.env` |
| Sempre isolar por tenant | Todo `findById` deve ser `findByIdAndTenantId` |
| Senha sempre com hash | Usar `BCryptPasswordEncoder` ao persistir senha (após TODO 5) |
| `.env` não versionado | Nunca commitar o arquivo `.env` — usar `.env.example` |

---

## 🚦 Comandos Padrão — Execute ao Validar Mudanças

```bash
# 1. Compilar e verificar erros
.\mvnw compile

# 2. Rodar testes
.\mvnw test

# 3. Subir banco (se necessário)
docker-compose up -d

# 4. Rodar aplicação localmente
.\mvnw spring-boot:run

# 5. Verificar se migrations foram aplicadas corretamente
docker exec -it autohub-db psql -U autohub -d autohub -c "SELECT version, description, success FROM flyway_schema_history ORDER BY installed_rank;"

# 6. Listar tabelas do banco
docker exec -it autohub-db psql -U autohub -d autohub -c "\dt"

# 7. Ver logs do banco
docker logs autohub-db --tail=50
```

---

## 📋 Checklist Antes de Finalizar Qualquer Tarefa

- [ ] O código compila sem erros (`.\mvnw compile`)
- [ ] Nenhuma credencial está hardcoded
- [ ] Toda nova tabela tem migration Flyway
- [ ] DTOs de Response não expõem `senha`
- [ ] Métodos de busca por ID usam `findByIdAndTenantId`
- [ ] Javadoc adicionado em interfaces e métodos públicos relevantes
- [ ] `AGENTS.md` atualizado se o estado do projeto mudou (✅ novos arquivos implementados)

---

## 🗂️ Variáveis de Ambiente Disponíveis (`.env`)

| Variável | Valor padrão | Uso |
|----------|-------------|-----|
| `DB_URL` | `jdbc:postgresql://localhost:5433/autohub` | JDBC URL da aplicação |
| `DB_USERNAME` | `autohub` | Usuário do banco (aplicação) |
| `DB_PASSWORD` | `autohub123` | Senha do banco (aplicação) |
| `POSTGRES_DB` | `autohub` | Nome do banco no Docker |
| `POSTGRES_USER` | `autohub` | Usuário no container |
| `POSTGRES_PASSWORD` | `autohub123` | Senha no container |
| `POSTGRES_PORT` | `5433` | Porta exposta no host |
| `PGADMIN_EMAIL` | `admin@autohub.com` | Login do PgAdmin (`localhost:5050`) |
| `PGADMIN_PASSWORD` | `admin123` | Senha do PgAdmin |

---

## 📦 Estado Atual dos Pacotes

```
com.autohub_api
 ├── controller/        ⬜ VAZIO
 ├── service/           ⬜ VAZIO
 ├── repository/        ✅ 5 repositories implementados
 ├── model/
 │    ├── entity/       ✅ 5 entidades (Tenant, Cliente, Car, ServiceHistory, Usuario)
 │    ├── enums/        ✅ UserRole (LEITOR / ESCRITOR)
 │    └── dto/          ✅ implementado
 │         ├── request/ ✅ TenantRequest, ClienteRequest, CarRequest,
 │         │              ServiceHistoryRequest, UsuarioRequest
 │         └── response/✅ TenantResponse, ClienteResponse, CarResponse,
 │                         ServiceHistoryResponse, UsuarioResponse
 ├── config/            ⬜ VAZIO
 ├── exception/         ⬜ VAZIO — TODO 2
 └── validation/        ⬜ VAZIO
```

Próxima migration Flyway disponível: **`V6__`**



