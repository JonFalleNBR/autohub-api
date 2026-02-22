# 🎯 GUIA FINAL - Como Executar o Projeto

## ✅ O QUE FOI CORRIGIDO

Você estava **100% correto**! A entidade `Car` agora tem FK para `Cliente` (dono do carro).

---

## 🔧 PASSOS PARA EXECUTAR

### Passo 1: Limpar Migrations Duplicadas
```powershell
.\clean-migrations.ps1
```

### Passo 2: Deletar Duplicatas de Entidades (IMPORTANTE!)
```powershell
.\remove-duplicates.ps1
```

**OU delete manualmente:**
1. Delete a pasta: `src/main/java/com/autohub_api/db/`
2. Delete a pasta: `src/main/java/com/autohub_api/model/` (se existir outra além das entidades que criamos)

### Passo 3: Resetar o Banco de Dados
```powershell
.\reset-db.ps1
```

### Passo 4: Executar a Aplicação
- Execute via IntelliJ ou:
```powershell
.\mvnw spring-boot:run
```

---

## 📊 ESTRUTURA FINAL CORRETA

### Entidades (com.autohub_api.model.entity/)
```
✅ Tenant.java      - Oficina/Empresa
✅ Cliente.java     - Dono do carro (⭐ NOVO!)
✅ Car.java         - Carro (COM FK para Cliente ✅)
✅ ServiceHistory.java - Histórico de serviços
```

### Repositories (com.autohub_api.repository/)
```
✅ TenantRepository.java
✅ ClienteRepository.java (⭐ NOVO!)
✅ CarRepository.java (atualizado com métodos para buscar por cliente)
✅ ServiceHistoryRepository.java
```

### Migrations (resources/db/migration/)
```
✅ V1__create_tenants.sql
✅ V2__create_clientes.sql (⭐ NOVO!)
✅ V3__create_cars.sql (COM cliente_id ✅)
✅ V4__create_service_history.sql
```

---

## 🔑 RELACIONAMENTOS CORRETOS

```
Tenant (1) ----< (N) Cliente
Cliente (1) ----< (N) Car      ← ⭐ CORRETO!
Car (1) ----< (N) ServiceHistory
```

**Explicação:**
- ✅ Todo carro TEM um cliente (dono)
- ✅ Um cliente PODE TER vários carros
- ✅ Um carro pertence a uma oficina (tenant) também
- ✅ Um carro tem histórico de serviços

---

## 💻 EXEMPLO DE USO

### 1. Cadastrar Cliente
```java
Cliente joao = new Cliente(tenant, "João Silva");
joao.setCpf("123.456.789-00");
clienteRepository.save(joao);
```

### 2. Cadastrar Carro do Cliente
```java
Car fusca = new Car(tenant, joao, "ABC-1234", "Fusca", "VW", "Azul", 1975);
carRepository.save(fusca);
```

### 3. Buscar Carros de um Cliente
```java
List<Car> carrosDoJoao = carRepository.findByClienteId(joao.getId());
// Retorna todos os carros do João
```

### 4. Registrar Serviço
```java
ServiceHistory service = new ServiceHistory(fusca, tenant, "Troca de óleo", now());
service.setCost(new BigDecimal("150.00"));
serviceHistoryRepository.save(service);
```

---

## ⚠️ IMPORTANTE - ANTES DE EXECUTAR

### 1. Limpe migrations duplicadas:
```powershell
.\clean-migrations.ps1
```

### 2. Delete as pastas duplicadas:
- ❌ `src/main/java/com/autohub_api/db/`
- ❌ `src/main/java/model/Entity/` (se existir - manter apenas `com.autohub_api.model.entity`)

### 3. Resete o banco:
```powershell
.\reset-db.ps1
```

---

## ✅ VERIFICAÇÃO

Após executar, você deve ter:

### Tabelas criadas:
```sql
✅ tenants
✅ clientes        (com FK tenant_id)
✅ cars            (com FK cliente_id E tenant_id)
✅ service_history (com FK car_id)
```

### Queries disponíveis:
```java
// Buscar carros de um cliente
carRepository.findByClienteId(clienteId);

// Buscar clientes de uma oficina
clienteRepository.findByTenantId(tenantId);

// Buscar histórico de um carro
serviceHistoryRepository.findByCarIdOrderByServiceDateDesc(carId);
```

---

## 📚 DOCUMENTAÇÃO

- **ESTRUTURA_CORRETA_CLIENTE_CARRO.md** - Explicação completa do modelo
- **clean-migrations.ps1** - Limpa migrations duplicadas
- **remove-duplicates.ps1** - Remove pastas duplicadas

---

## 🎉 STATUS FINAL

✅ **Entidade Car TEM FK para Cliente** (conforme você pediu!)  
✅ **Estrutura: Cliente (1) → (N) Carros**  
✅ **Migrations criadas corretamente**  
✅ **Repositories prontos com queries úteis**  

**Exatamente como você solicitou: "todo carro tem um cliente e um cliente pode ter vários carros" ✅**

---

## 🚀 EXECUTE AGORA!

1. Execute: `.\clean-migrations.ps1`
2. Execute: `.\remove-duplicates.ps1`
3. Execute: `.\reset-db.ps1`
4. Execute a aplicação Spring Boot

**Tudo pronto! 🎉**

