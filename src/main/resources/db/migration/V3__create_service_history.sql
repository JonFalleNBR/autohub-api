create table service_history (
  id uuid primary key,
  car_id uuid not null references cars(id),
  tenant_id uuid not null references tenants(id),
  service_type varchar(100) not null,
  description text,
  service_date timestamptz not null,
  cost decimal(10, 2),
  mileage integer,
  technician_name varchar(120),
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index idx_service_history_car on service_history(car_id);
create index idx_service_history_tenant on service_history(tenant_id);
create index idx_service_history_date on service_history(service_date);

