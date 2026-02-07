-- Adiciona campos de prioridade na tabela de usuarios
-- Prioridade para redirecionamento de consultas: PCD (3) > Idoso (2) > Gestante (1)

ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS idoso BOOLEAN DEFAULT FALSE;
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS gestante BOOLEAN DEFAULT FALSE;
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS pcd BOOLEAN DEFAULT FALSE;

COMMENT ON COLUMN usuarios.idoso IS 'Indica se o usuário é idoso (prioridade 2)';
COMMENT ON COLUMN usuarios.gestante IS 'Indica se o usuário é gestante (prioridade 1)';
COMMENT ON COLUMN usuarios.pcd IS 'Indica se o usuário é PCD - Pessoa com Deficiência (prioridade 3)';
