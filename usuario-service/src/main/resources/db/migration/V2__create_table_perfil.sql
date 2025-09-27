-- Criar tabela de perfil (sem FK inválida)
CREATE TABLE IF NOT EXISTS perfil (
        id BIGSERIAL PRIMARY KEY,
        nome_perfil VARCHAR(100) NOT NULL UNIQUE,
        descricao VARCHAR(255)
);

-- Caso a tabela já exista sem a coluna descricao, garante a criação
ALTER TABLE perfil ADD COLUMN IF NOT EXISTS descricao VARCHAR(255);

-- Inserir perfis padrão com IDs fixos (idempotente)
INSERT INTO perfil (id, nome_perfil, descricao) VALUES 
        (0, 'ADMIN', 'Administrador'),
        (1, 'MEDICO', 'Médico'),
        (2, 'PACIENTE', 'Paciente'),
        (3, 'ENFERMEIRO', 'Enfermeiro')
ON CONFLICT DO NOTHING;

-- Ajustar a sequência (se existir) para o maior ID presente
DO $$
BEGIN
    PERFORM setval(
        pg_get_serial_sequence('perfil','id'),
        COALESCE((SELECT MAX(id) FROM perfil), 1),
        true
    );
EXCEPTION WHEN undefined_function OR undefined_table THEN
    -- ignora caso a sequência não exista
    NULL;
END $$;

