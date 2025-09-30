-- Adiciona a FK de usuarios(perfil_id) -> perfil(id)
-- Importante: essa migration pressupõe que V1 (usuarios) e V2 (perfil) já foram aplicadas.

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints tc
        WHERE tc.constraint_type = 'FOREIGN KEY'
          AND tc.table_name = 'usuarios'
          AND tc.constraint_name = 'fk_usuarios_perfil'
    ) THEN
        ALTER TABLE usuarios
            ADD CONSTRAINT fk_usuarios_perfil
            FOREIGN KEY (perfil_id) REFERENCES perfil (id);
    END IF;
END $$;