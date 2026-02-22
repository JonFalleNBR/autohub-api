CREATE TABLE tenants (
  id uuid PRIMARY KEY,
  name varchar(120) NOT NULL,
  slug varchar(60) NOT NULL UNIQUE,
  created_at timestamptz NOT NULL DEFAULT now()
);