DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'ci_user') THEN
CREATE ROLE unidesk_user WITH LOGIN PASSWORD 'ci_password';
ALTER ROLE unidesk_user CREATEDB;
END IF;
END $$;
