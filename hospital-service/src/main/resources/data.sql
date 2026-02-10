-- Inserir hospitais de exemplo (executado automaticamente pelo Spring Boot)
-- Este arquivo é executado após a criação das tabelas pelo Hibernate (ddl-auto: update)

-- Inserir hospitais somente se a tabela estiver vazia
INSERT INTO hospitais (id, nome, endereco, telefone, email, especialidades, ativo, criado_em)
SELECT 1, 'Hospital São Paulo', 'Rua Napoleão de Barros, 715 - Vila Clementino, São Paulo - SP, 04024-002', '(11) 5576-4000', 'contato@hospitalsaopaulo.org.br', 'Cardiologia,Neurologia,Ortopedia,Pediatria,Oncologia', TRUE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM hospitais WHERE id = 1);

INSERT INTO hospitais (id, nome, endereco, telefone, email, especialidades, ativo, criado_em)
SELECT 2, 'Hospital das Clínicas', 'Av. Dr. Enéas Carvalho de Aguiar, 255 - Cerqueira César, São Paulo - SP, 05403-000', '(11) 2661-0000', 'contato@hc.fm.usp.br', 'Cardiologia,Neurologia,Oncologia,Transplantes,Cirurgia Geral', TRUE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM hospitais WHERE id = 2);

INSERT INTO hospitais (id, nome, endereco, telefone, email, especialidades, ativo, criado_em)
SELECT 3, 'Hospital Albert Einstein', 'Av. Albert Einstein, 627 - Morumbi, São Paulo - SP, 05652-900', '(11) 2151-1233', 'contato@einstein.br', 'Cardiologia,Oncologia,Neurologia,Ortopedia,Medicina Preventiva', TRUE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM hospitais WHERE id = 3);

INSERT INTO hospitais (id, nome, endereco, telefone, email, especialidades, ativo, criado_em)
SELECT 4, 'Hospital Sírio-Libanês', 'Rua Dona Adma Jafet, 91 - Bela Vista, São Paulo - SP, 01308-050', '(11) 3394-0200', 'contato@hsl.org.br', 'Oncologia,Cardiologia,Neurologia,Transplantes,Medicina Intensiva', TRUE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM hospitais WHERE id = 4);

INSERT INTO hospitais (id, nome, endereco, telefone, email, especialidades, ativo, criado_em)
SELECT 5, 'Santa Casa de São Paulo', 'Rua Dr. Cesário Motta Jr., 112 - Vila Buarque, São Paulo - SP, 01221-020', '(11) 2176-7000', 'contato@santacasasp.org.br', 'Clínica Geral,Pediatria,Ginecologia,Ortopedia,Emergência', TRUE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM hospitais WHERE id = 5);
