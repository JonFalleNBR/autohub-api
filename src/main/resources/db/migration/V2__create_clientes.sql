CREATE TABLE clientes (
  id uuid PRIMARY KEY,
  tenant_id uuid NOT NULL REFERENCES tenants(id),

  nome varchar(120) NOT NULL,
  cpf varchar(14) NOT NULL,
  telefone varchar(20),
  email varchar(120),
  endereco TEXT,

  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX idx_clientes_tenant ON clientes(tenant_id);
CREATE UNIQUE INDEX uq_clientes_tenant_cpf ON clientes(tenant_id, cpf);