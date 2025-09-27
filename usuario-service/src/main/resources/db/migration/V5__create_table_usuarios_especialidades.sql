-- Tabela de junção Many-to-Many entre usuarios e especialidade

CREATE TABLE IF NOT EXISTS usuarios_especialidades (
    usuario_id BIGINT NOT NULL,
    especialidade_id BIGINT NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_usuarios_especialidades PRIMARY KEY (usuario_id, especialidade_id),
    CONSTRAINT fk_ue_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE,
    CONSTRAINT fk_ue_especialidade FOREIGN KEY (especialidade_id) REFERENCES especialidade (id) ON DELETE CASCADE
);
