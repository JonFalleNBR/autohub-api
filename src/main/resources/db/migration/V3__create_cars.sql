CREATE TABLE cars (
  id uuid PRIMARY KEY,
  tenant_id uuid NOT NULL REFERENCES tenants(id),
  cliente_id uuid NOT NULL REFERENCES clientes(id),
  plate varchar(10) NOT NULL,
  model varchar(120) NOT NULL,
  brand varchar(120) NOT NULL,
  color varchar(50) NOT NULL,
  year integer NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (tenant_id, plate)
);

CREATE INDEX idx_cars_tenant ON cars(tenant_id);
CREATE INDEX idx_cars_cliente ON cars(cliente_id);