-- Atualizar tabela cars para referenciar clientes ao invés de tenants
create table cars (
  id uuid primary key,
  tenant_id uuid not null references tenants(id),
  cliente_id uuid not null references clientes(id),
  plate varchar(10) not null,
  model varchar(120) not null,
  brand varchar(120) not null,
  color varchar(50) not null,
  year integer not null,
  created_at timestamptz not null default now(),
  unique (tenant_id, plate)
);

create index idx_cars_tenant on cars(tenant_id);
create index idx_cars_cliente on cars(cliente_id);

