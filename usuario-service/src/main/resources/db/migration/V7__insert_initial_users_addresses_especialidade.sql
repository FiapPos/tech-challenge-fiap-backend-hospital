-- V7: Inserir dados iniciais de usuários, endereços e vínculo de especialidade para usuário PROFESSOR
-- Gera hashes de senha usando pgcrypto (bcrypt via crypt/gen_salt)
CREATE EXTENSION IF NOT EXISTS pgcrypto;
-- Idempotente: só insere quando não existir registro com o mesmo CPF (para usuários)

-- Inserir usuários (um para cada perfil: ADMIN, PROFESSOR, ESTUDANTE, COORDENADOR)
INSERT INTO usuarios (id, nome, cpf, data_nascimento, telefone, email, senha, login, ativo, data_criacao, perfil_id)
VALUES
    (100, 'Administrador Sistema', '00000000000', '1980-01-01', '111111111', 'admin@fiap.edu.br', crypt('senha123', gen_salt('bf', 10)), 'admin', TRUE, CURRENT_TIMESTAMP, 0),
    (101, 'Professor João Silva', '11111111111', '1980-02-02', '222222222', 'professor@fiap.edu.br', crypt('senha123', gen_salt('bf', 10)), 'professor', TRUE, CURRENT_TIMESTAMP, 1),
    (102, 'Estudante Maria Santos', '22222222222', '1990-03-03', '333333333', 'estudante@fiap.edu.br', crypt('senha123', gen_salt('bf', 10)), 'estudante', TRUE, CURRENT_TIMESTAMP, 2),
    (103, 'Coordenador Carlos Souza', '33333333333', '1985-04-04', '444444444', 'coordenador@fiap.edu.br', crypt('senha123', gen_salt('bf', 10)), 'coordenador', TRUE, CURRENT_TIMESTAMP, 3)
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
        VALUES ('Av. Paulista, 1000', '00000-000', '1000', 'Bela Vista', 'São Paulo', 100, CURRENT_TIMESTAMP);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM endereco WHERE usuario_id = 101) THEN
        INSERT INTO endereco (rua, cep, numero, bairro, cidade, usuario_id, data_criacao)
        VALUES ('Rua dos Professores, 101', '11111-111', '101', 'Centro', 'São Paulo', 101, CURRENT_TIMESTAMP);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM endereco WHERE usuario_id = 102) THEN
        INSERT INTO endereco (rua, cep, numero, bairro, cidade, usuario_id, data_criacao)
        VALUES ('Rua dos Estudantes, 102', '22222-222', '102', 'Jardins', 'São Paulo', 102, CURRENT_TIMESTAMP);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM endereco WHERE usuario_id = 103) THEN
        INSERT INTO endereco (rua, cep, numero, bairro, cidade, usuario_id, data_criacao)
        VALUES ('Av. da Coordenação, 103', '33333-333', '103', 'Vila Mariana', 'São Paulo', 103, CURRENT_TIMESTAMP);
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

-- Vincular o usuário PROFESSOR (id 101) a uma especialidade existente (ex.: CARDIOLOGIA)
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
