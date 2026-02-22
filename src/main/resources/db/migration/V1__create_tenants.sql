create table tenants (
  id uuid primary key,
  name varchar(120) not null,
  slug varchar(60) not null unique,
  created_at timestamptz not null default now()
);

