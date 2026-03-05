CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'LEITOR',
    ativo BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_usuarios_tenant_email ON usuarios(tenant_id, email);

COMMENT ON TABLE usuarios IS 'Usuários do sistema por tenant';
COMMENT ON COLUMN usuarios.role IS 'LEITOR = apenas visualização, ESCRITOR = pode criar, editar e deletar';

