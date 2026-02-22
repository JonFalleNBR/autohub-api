-- Criar tabela de clientes (donos dos carros)
create table clientes (
  id uuid primary key,
  tenant_id uuid not null references tenants(id),
  nome varchar(120) not null,
  cpf varchar(14) unique,
  telefone varchar(20),
  email varchar(120),
  endereco text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index idx_clientes_tenant on clientes(tenant_id);
create index idx_clientes_cpf on clientes(cpf);

