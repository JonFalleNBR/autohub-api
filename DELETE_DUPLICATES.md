# ⚠️ ATENÇÃO - DUPLICATAS DETECTADAS

## ❌ Pastas Duplicadas que DEVEM ser DELETADAS:

### 1. Deletar pasta `com.autohub_api/db/`
```
📁 src/main/java/com/autohub_api/db/  ← DELETAR ESTA PASTA INTEIRA
   └── migration/
       ├── V1__create_tenants.sql
       └── V2__create_cars.sql
```

### 2. Deletar pasta `com.autohub_api/model/`
```
📁 src/main/java/com/autohub_api/model/  ← DELETAR ESTA PASTA INTEIRA
   └── entity/
       ├── Car.java
       ├── Tenant.java
       └── ServiceHistory.java
```

---

## ✅ Pastas CORRETAS (MANTER):

### 1. MANTER: `model/Entity/`
```
📁 src/main/java/model/Entity/  ← MANTER!
   ├── Cars.java
   ├── Tenant.java
   └── ServiceHistory.java
```

### 2. MANTER: `resources/db/migration/`
```
📁 src/main/resources/db/migration/  ← MANTER!
   ├── V1__create_tenants.sql
   ├── V2__create_cars.sql
   └── V3__create_service_history.sql
```

---

## 🔧 Como Deletar (3 Opções)

### Opção 1: Via PowerShell (RECOMENDADO)
```powershell
# Execute este script que criei para você:
.\remove-duplicates.ps1
```

### Opção 2: Via Explorer
1. Abra a pasta do projeto
2. Navegue até `src/main/java/com/autohub_api/`
3. Delete a pasta `db`
4. Delete a pasta `model`

### Opção 3: Via Terminal (comandos manuais)
```powershell
Remove-Item -Recurse -Force "C:\autohub-project\autohub-api\src\main\java\com\autohub_api\db"
Remove-Item -Recurse -Force "C:\autohub-project\autohub-api\src\main\java\com\autohub_api\model"
```

---

## ✅ Após Deletar

A estrutura ficará assim (CORRETA):

```
src/main/
├── java/
│   ├── com/autohub_api/
│   │   ├── AutohubApiApplication.java
│   │   └── repository/
│   │       ├── CarRepository.java
│   │       ├── TenantRepository.java
│   │       └── ServiceHistoryRepository.java
│   │
│   └── model/Entity/  ← ÚNICO local correto
│       ├── Cars.java
│       ├── Tenant.java
│       └── ServiceHistory.java
│
└── resources/
    └── db/migration/  ← ÚNICO local correto
        ├── V1__create_tenants.sql
        ├── V2__create_cars.sql
        └── V3__create_service_history.sql
```

---

## 🎯 Resumo

❌ **DELETAR:**
- `com/autohub_api/db/` (pasta inteira)
- `com/autohub_api/model/` (pasta inteira)

✅ **MANTER:**
- `model/Entity/` (entidades)
- `resources/db/migration/` (migrations)
- `com/autohub_api/repository/` (repositories)

---

**Execute o script `remove-duplicates.ps1` para deletar automaticamente!**

