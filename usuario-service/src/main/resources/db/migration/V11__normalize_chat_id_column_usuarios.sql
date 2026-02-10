-- Normaliza o campo de chatId (Telegram) para o formato esperado pelo Hibernate.
-- Situações cobertas:
-- - V9 criou `chatId` sem aspas -> Postgres criou `chatid` (varchar) com default booleano
-- - a aplicação espera `chat_id` (BIGINT)
-- - ambientes onde nenhuma das colunas existe ainda
DO $$
BEGIN
    -- Se existir `chatid` (errado) e não existir `chat_id` (certo), converte e renomeia.
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'usuarios'
          AND column_name = 'chatid'
    ) AND NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'usuarios'
          AND column_name = 'chat_id'
    ) THEN
        -- remove default esquisito (ex.: false)
        BEGIN
            EXECUTE 'ALTER TABLE usuarios ALTER COLUMN chatid DROP DEFAULT';
        EXCEPTION WHEN others THEN
            -- ignore
        END;

        -- normaliza valores não numéricos para NULL (ex.: '' / 'false' / qualquer lixo)
        EXECUTE $SQL$
            UPDATE usuarios
               SET chatid = NULL
             WHERE chatid IS NULL
                OR chatid !~ '^[0-9]+$'
        $SQL$;

        -- converte para BIGINT
        EXECUTE 'ALTER TABLE usuarios ALTER COLUMN chatid TYPE BIGINT USING chatid::bigint';

        -- renomeia para snake_case que o Hibernate espera
        EXECUTE 'ALTER TABLE usuarios RENAME COLUMN chatid TO chat_id';
    END IF;

    -- Se nenhuma coluna existir, cria a correta.
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'usuarios'
          AND column_name = 'chat_id'
    ) THEN
        EXECUTE 'ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS chat_id BIGINT';
    END IF;

    -- Se existir `chat_id`, garante que não tenha default.
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'usuarios'
          AND column_name = 'chat_id'
    ) THEN
        BEGIN
            EXECUTE 'ALTER TABLE usuarios ALTER COLUMN chat_id DROP DEFAULT';
        EXCEPTION WHEN others THEN
            -- ignore
        END;
    END IF;
END $$;

