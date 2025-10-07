-- V7: Inserir dados iniciais de usuários, endereços e vínculo de especialidade para usuário MEDICO
-- Gera hashes de senha usando pgcrypto (bcrypt via crypt/gen_salt)
CREATE EXTENSION IF NOT EXISTS pgcrypto;
-- Idempotente: só insere quando não existir registro com o mesmo CPF (para usuários)

-- Inserir usuários (um para cada perfil: ADMIN, MEDICO, PACIENTE, ENFERMEIRO)
INSERT INTO usuarios (id, nome, cpf, data_nascimento, telefone, email, senha, login, ativo, data_criacao, perfil_id)
VALUES
    (100, 'Administrador Exemplo', '00000000000', '1980-01-01', '111111111', 'admin@example.com', crypt('senha123', gen_salt('bf', 10)), 'admin', TRUE, CURRENT_TIMESTAMP, 0),
    (101, 'Medico Exemplo', '11111111111', '1980-02-02', '222222222', 'medico@example.com', crypt('senha123', gen_salt('bf', 10)), 'medico', TRUE, CURRENT_TIMESTAMP, 1),
    (102, 'Paciente Exemplo', '22222222222', '1990-03-03', '333333333', 'paciente@example.com', crypt('senha123', gen_salt('bf', 10)), 'paciente', TRUE, CURRENT_TIMESTAMP, 2),
    (103, 'Enfermeiro Exemplo', '33333333333', '1985-04-04', '444444444', 'enfermeiro@example.com', crypt('senha123', gen_salt('bf', 10)), 'enfermeiro', TRUE, CURRENT_TIMESTAMP, 3)
ON CONFLICT (cpf) DO NOTHING;

-- Ajustar sequência da tabela usuarios para não colidir com IDs inseridos manualmente
DO $$
BEGIN
    PERFORM setval(
        pg_get_serial_sequence('usuarios','id'),
        COALESCE((SELECT MAX(id) FROM usuarios), 1),
        true
    );
EXCEPTION WHEN undefined_function OR undefined_table THEN
    NULL;
END $$;

-- Inserir um endereço para cada usuário (se não existir endereço para o usuário)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM endereco WHERE usuario_id = 100) THEN
        INSERT INTO endereco (rua, cep, numero, bairro, cidade, usuario_id, data_criacao)
        VALUES ('Rua do Administrador, 100', '00000-000', '100', 'Bairro Admin', 'Cidade A', 100, CURRENT_TIMESTAMP);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM endereco WHERE usuario_id = 101) THEN
        INSERT INTO endereco (rua, cep, numero, bairro, cidade, usuario_id, data_criacao)
        VALUES ('Av. do Médico, 101', '11111-111', '101', 'Bairro Med', 'Cidade B', 101, CURRENT_TIMESTAMP);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM endereco WHERE usuario_id = 102) THEN
        INSERT INTO endereco (rua, cep, numero, bairro, cidade, usuario_id, data_criacao)
        VALUES ('Rua do Paciente, 102', '22222-222', '102', 'Bairro Pac', 'Cidade C', 102, CURRENT_TIMESTAMP);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM endereco WHERE usuario_id = 103) THEN
        INSERT INTO endereco (rua, cep, numero, bairro, cidade, usuario_id, data_criacao)
        VALUES ('Rua do Enfermeiro, 103', '33333-333', '103', 'Bairro Enf', 'Cidade D', 103, CURRENT_TIMESTAMP);
    END IF;
EXCEPTION WHEN undefined_table THEN
    -- se a tabela endereco não existir no ambiente, ignora-se a inserção
    NULL;
END $$;

-- Ajustar sequência da tabela endereco
DO $$
BEGIN
    PERFORM setval(
        pg_get_serial_sequence('endereco','id'),
        COALESCE((SELECT MAX(id) FROM endereco), 1),
        true
    );
EXCEPTION WHEN undefined_function OR undefined_table THEN
    NULL;
END $$;

-- Vincular o usuário MEDICO (id 101) a uma especialidade existente (ex.: CARDIOLOGIA)
DO $$
DECLARE
    esp_id BIGINT;
BEGIN
    SELECT id INTO esp_id FROM especialidade WHERE nome = 'CARDIOLOGIA' LIMIT 1;
    IF esp_id IS NOT NULL THEN
        INSERT INTO usuarios_especialidades (usuario_id, especialidade_id)
        VALUES (101, esp_id)
        ON CONFLICT DO NOTHING;
    END IF;
EXCEPTION WHEN undefined_table THEN
    -- ignora caso as tabelas não existam
    NULL;
END $$;
