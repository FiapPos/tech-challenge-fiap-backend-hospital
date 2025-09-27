CREATE TABLE especialidade (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inserir especialidades padrão (idempotente)
INSERT INTO especialidade (nome, descricao, ativo, data_criacao, data_atualizacao) VALUES 
        ('CARDIOLOGIA', 'Especialidade médica que se ocupa do diagnóstico e tratamento das doenças que acometem o coração', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        ('DERMATOLOGIA', 'Especialidade médica que se ocupa do diagnóstico, prevenção e tratamento de doenças e afecções relacionadas à pele', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        ('NEUROLOGIA', 'Especialidade médica que trata dos distúrbios estruturais do sistema nervoso', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        ('PEDIATRIA', 'Especialidade médica dedicada à assistência à criança e ao adolescente', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        ('ORTOPEDIA', 'Especialidade médica que cuida da saúde relacionada aos elementos do aparelho locomotor', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        ('GINECOLOGIA', 'Especialidade médica que trata de doenças do sistema reprodutor feminino', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        ('UROLOGIA', 'Especialidade médica que trata do trato urinário de homens e mulheres', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
        ('OFTALMOLOGIA', 'Especialidade médica que se dedica ao estudo e tratamento das doenças relacionadas ao olho', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) 
ON CONFLICT DO NOTHING;