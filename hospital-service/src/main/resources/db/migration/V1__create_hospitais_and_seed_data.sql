-- V1: Criar tabela de hospitais e inserir dados iniciais

-- Criar tabela se não existir (compatibilidade com ddl-auto)
CREATE TABLE IF NOT EXISTS hospitais (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    endereco VARCHAR(300) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    especialidades TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP
);

-- Inserir hospitais de exemplo (idempotente)
INSERT INTO hospitais (id, nome, endereco, telefone, email, especialidades, ativo, criado_em)
VALUES
    (1, 'Hospital São Paulo', 'Rua Napoleão de Barros, 715 - Vila Clementino, São Paulo - SP, 04024-002', '(11) 5576-4000', 'contato@hospitalsaopaulo.org.br', 'Cardiologia,Neurologia,Ortopedia,Pediatria,Oncologia', TRUE, CURRENT_TIMESTAMP),
    (2, 'Hospital das Clínicas', 'Av. Dr. Enéas Carvalho de Aguiar, 255 - Cerqueira César, São Paulo - SP, 05403-000', '(11) 2661-0000', 'contato@hc.fm.usp.br', 'Cardiologia,Neurologia,Oncologia,Transplantes,Cirurgia Geral', TRUE, CURRENT_TIMESTAMP),
    (3, 'Hospital Albert Einstein', 'Av. Albert Einstein, 627 - Morumbi, São Paulo - SP, 05652-900', '(11) 2151-1233', 'contato@einstein.br', 'Cardiologia,Oncologia,Neurologia,Ortopedia,Medicina Preventiva', TRUE, CURRENT_TIMESTAMP),
    (4, 'Hospital Sírio-Libanês', 'Rua Dona Adma Jafet, 91 - Bela Vista, São Paulo - SP, 01308-050', '(11) 3394-0200', 'contato@hsl.org.br', 'Oncologia,Cardiologia,Neurologia,Transplantes,Medicina Intensiva', TRUE, CURRENT_TIMESTAMP),
    (5, 'Santa Casa de São Paulo', 'Rua Dr. Cesário Motta Jr., 112 - Vila Buarque, São Paulo - SP, 01221-020', '(11) 2176-7000', 'contato@santacasasp.org.br', 'Clínica Geral,Pediatria,Ginecologia,Ortopedia,Emergência', TRUE, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Ajustar sequência para não colidir com IDs inseridos
SELECT setval(pg_get_serial_sequence('hospitais', 'id'), COALESCE((SELECT MAX(id) FROM hospitais), 1) + 1, false);
