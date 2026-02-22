# ✅ ESTRUTURA CORRETA - Cliente e Carro

## 🎯 Problema Resolvido!

Você estava correto! A entidade `Car` agora tem **FK para Cliente** (dono do carro).

---

## 📊 Modelo de Dados CORRETO

```
Tenant (Oficina/Empresa)
    │
    ├── tem vários → Clientes
    │
    └── tem vários → Cars (para controle geral)

Cliente (Dono do carro)
    │
    └── tem vários → Cars

Car (Carro)
    ├── pertence a UM → Cliente (dono)  ✅
    ├── pertence a UM → Tenant (oficina) ✅
    └── tem vários → ServiceHistory (histórico)
```

---

## 🗄️ Tabelas Criadas

### 1. `tenants` (Oficina/Empresa)
```sql
- id (UUID PK)
- name (nome da oficina)
- slug (identificador único)
- created_at
```

### 2. `clientes` (Donos dos carros) ⭐ NOVO!
```sql
- id (UUID PK)
- tenant_id (FK → tenants)  // Qual oficina cadastrou
- nome
- cpf
- telefone
- email
- endereco
- created_at
- updated_at
```

### 3. `cars` (Carros) ✅ CORRIGIDO!
```sql
- id (UUID PK)
- tenant_id (FK → tenants)     // Qual oficina gerencia
- cliente_id (FK → clientes)   // ⭐ Quem é o DONO do carro
- plate (placa)
- model (modelo)
- brand (marca)
- color (cor)
- year (ano)
- created_at
```

### 4. `service_history` (Histórico de serviços)
```sql
- id (UUID PK)
- car_id (FK → cars)           // ⭐ Qual carro recebeu o serviço
- tenant_id (FK → tenants)     // Qual oficina prestou
- service_type
- description
- service_date
- cost
- mileage
- technician_name
- notes
- created_at
- updated_at
```

---

## 🔑 Relacionamentos

### 1. Cliente → Car (1:N)
```java
// Na entidade Car
@ManyToOne
@JoinColumn(name = "cliente_id", nullable = false)
private Cliente cliente;  // ✅ DONO do carro
```

**Exemplo:**
- João da Silva (Cliente) → tem 3 carros
- Maria Santos (Cliente) → tem 1 carro

### 2. Tenant → Cliente (1:N)
```java
// Na entidade Cliente
@ManyToOne
@JoinColumn(name = "tenant_id", nullable = false)
private Tenant tenant;  // ✅ Qual oficina cadastrou
```

**Exemplo:**
- Oficina ABC → cadastrou 50 clientes

### 3. Tenant → Car (1:N)
```java
// Na entidade Car
@ManyToOne
@JoinColumn(name = "tenant_id", nullable = false)
private Tenant tenant;  // ✅ Qual oficina gerencia
```

**Exemplo:**
- Oficina ABC → gerencia 100 carros de seus clientes

### 4. Car → ServiceHistory (1:N)
```java
// Na entidade ServiceHistory
@ManyToOne
@JoinColumn(name = "car_id", nullable = false)
private Car car;  // ✅ Qual carro recebeu o serviço
```

**Exemplo:**
- Fusca ABC-1234 → teve 10 serviços feitos

---

## 📦 Entidades Criadas

### ✅ Car (já existente - atualizada)
```java
com.autohub_api.model.entity.Car
- tenant (FK)
- cliente (FK) ⭐ ADICIONADO!
- plate, model, brand, color, year
```

### ✅ Cliente (nova)
```java
com.autohub_api.model.entity.Cliente
- tenant (FK)
- nome, cpf, telefone, email, endereco
```

### ✅ Tenant (já existente)
```java
com.autohub_api.model.entity.Tenant
- name, slug
```

### ✅ ServiceHistory (já existente - atualizada)
```java
com.autohub_api.model.entity.ServiceHistory
- car (FK)
- tenant (FK)
- service_type, description, service_date, cost, etc.
```

---

## 🗂️ Repositories Criados

### ✅ ClienteRepository (novo)
```java
// Buscar clientes de uma oficina
List<Cliente> findByTenantId(UUID tenantId);

// Buscar cliente por CPF
Optional<Cliente> findByCpf(String cpf);

// Buscar por nome (busca parcial)
List<Cliente> findByTenantIdAndNomeContainingIgnoreCase(UUID tenantId, String nome);
```

### ✅ CarRepository (atualizado)
```java
// Buscar carros de uma oficina
List<Car> findByTenantId(UUID tenantId);

// ⭐ Buscar carros de um cliente
List<Car> findByClienteId(UUID clienteId);

// Buscar carros de um cliente (ordenados)
List<Car> findByClienteIdOrderByCreatedAtDesc(UUID clienteId);
```

---

## 💻 Exemplos de Uso

### Cadastrar um Cliente
```java
@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    
    public Cliente cadastrar(UUID tenantId, String nome, String cpf) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
            
        Cliente cliente = new Cliente(tenant, nome);
        cliente.setCpf(cpf);
        
        return clienteRepository.save(cliente);
    }
}
```

### Cadastrar um Carro para um Cliente
```java
@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;
    
    public Car cadastrar(UUID tenantId, UUID clienteId, String plate, String model) {
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));
            
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente not found"));
            
        Car car = new Car(tenant, cliente, plate, model, "Honda", "Prata", 2020);
        
        return carRepository.save(car);
    }
}
```

### Listar Carros de um Cliente
```java
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    private CarRepository carRepository;
    
    @GetMapping("/{clienteId}/cars")
    public List<Car> getCars(@PathVariable UUID clienteId) {
        // Retorna todos os carros do cliente
        return carRepository.findByClienteIdOrderByCreatedAtDesc(clienteId);
    }
}
```

### Buscar Histórico de Serviços de um Carro
```java
@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private ServiceHistoryRepository serviceHistoryRepository;
    
    @GetMapping("/{carId}/history")
    public List<ServiceHistory> getHistory(@PathVariable UUID carId) {
        return serviceHistoryRepository.findByCarIdOrderByServiceDateDesc(carId);
    }
}
```

---

## 📁 Migrations (Ordem Correta)

```
resources/db/migration/
├── V1__create_tenants.sql        ✅
├── V2__create_clientes.sql       ✅ CRIADO!
├── V3__create_cars.sql           ✅ ATUALIZADO (com cliente_id)
└── V4__create_service_history.sql ✅
```

---

## ✅ Status Final

✅ Entidade `Car` tem FK para `Cliente` (dono)  
✅ Entidade `Car` tem FK para `Tenant` (oficina)  
✅ Entidade `Cliente` criada  
✅ Entidade `ServiceHistory` tem FK para `Car`  
✅ Repositories criados/atualizados  
✅ Migrations criadas na ordem correta  

**Pronto para executar a aplicação! 🚀**

---

## 🎯 Resumo da Hierarquia

```
Tenant (Oficina)
    ↓
Cliente (Dono) ← cada cliente pertence a uma oficina
    ↓
Car (Carro) ← cada carro pertence a um cliente E uma oficina
    ↓
ServiceHistory ← cada serviço é feito em um carro
```

**Exatamente como você pediu: "todo carro tem um cliente e um cliente pode ter vários carros" ✅**

