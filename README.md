# gerenciamento-backend

API REST em Spring Boot para o cadastro/listagem de usuários consumido pelo `projeto-angular`.

## Rodando localmente (sem depender do Neon)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Sobe com H2 em memória em `http://localhost:8080`, console H2 em `/h2-console`.

## Endpoints

- `POST /api/usuarios` — cria usuário (`nome`, `email`, `cpf`, `senha`)
- `GET /api/usuarios` — lista usuários
- `GET /api/usuarios/{id}` — busca por id
- `PUT /api/usuarios/{id}` — atualiza
- `DELETE /api/usuarios/{id}` — remove
- `GET /api/health` — health check (usado pelo Fly.io)

Senhas são armazenadas com hash BCrypt e nunca retornadas pela API.

## Deploy em produção

O Vercel não roda Java, então o Angular vai para o Vercel e esta API vai para um host que suporta containers (Fly.io).

### 1. Banco de dados (Neon)

1. Crie um projeto em https://neon.tech (free tier).
2. Copie a connection string (formato `postgresql://user:pass@host/db?sslmode=require`).
3. Converta para JDBC trocando o prefixo: `jdbc:postgresql://host/db?sslmode=require`.

### 2. Deploy da API (Fly.io)

```bash
# instale o flyctl: https://fly.io/docs/flyctl/install/
fly auth login
fly launch --no-deploy   # usa o fly.toml deste repo, ajuste o app name se pedir
fly secrets set DATABASE_URL="jdbc:postgresql://<host>/<db>?sslmode=require"
fly secrets set DATABASE_USERNAME="<usuario-neon>"
fly secrets set DATABASE_PASSWORD="<senha-neon>"
fly secrets set CORS_ALLOWED_ORIGINS="https://<seu-app>.vercel.app"
fly deploy
```

Após o deploy, a API fica em `https://<app>.fly.dev`.

### 3. Aponte o frontend para a API

No repositório do `projeto-angular`, edite `src/environments/environment.prod.ts` com a URL acima e faça o deploy no Vercel.
