-- Corrige o campo de chatId (Telegram) criado com nome/tipo incorretos.
-- Problema observado: a migration V9 adicionou `chatId` sem aspas, o que vira `chatid` no Postgres,
-- e ainda como VARCHAR com default `false`. O Hibernate (SpringPhysicalNamingStrategy) tenta ler `chat_id`.
--
-- Objetivo:
-- - Garantir a coluna `chat_id` como BIGINT (nullable), sem default booleano.
-- - Migrar dados existentes quando possível.

DO $$
BEGIN
    -- Caso mais comum: coluna está como `chatid` (varchar), precisamos normalizar.
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
        EXECUTE 'ALTER TABLE usuarios ALTER COLUMN chatid DROP DEFAULT';

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

    -- Se por algum motivo já existir `chat_id`, tenta garantir o tipo (sem quebrar ambientes antigos).
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

