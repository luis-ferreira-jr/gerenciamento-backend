# Sistema de Gerenciamento de Usuários — API

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?logo=springboot&logoColor=white)
![Postgres](https://img.shields.io/badge/Postgres-Neon-4169E1?logo=postgresql&logoColor=white)
![Render](https://img.shields.io/badge/Deploy-Render-46E3B7?logo=render&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-informational)

API REST em Spring Boot para cadastro, listagem, edição e exclusão de usuários, consumida pelo frontend em Angular.

**🔗 API em produção:** https://gerenciamento-backend-b3k8.onrender.com/api/health

> Repositório do frontend: [ProjetoAngular](https://github.com/luis-ferreira-jr/ProjetoAngular) · Demo: https://projeto-angular-jade.vercel.app

## Stack e decisões técnicas

- **Spring Boot 3.5** (Web, Data JPA, Validation)
- **Postgres** em produção ([Neon](https://neon.tech), serverless), **H2** em memória para desenvolvimento local
- **BCrypt** (`spring-security-crypto`) para hash de senha — nunca armazenada nem retornada em texto puro
- **DTOs separados** de request/response/update: a senha nunca sai da API (`UsuarioResponseDTO`), e a atualização (`UsuarioUpdateDTO`) não exige senha nova
- Validação de **CPF** com dígito verificador real (`@CPF` custom validator), não só formato
- **CORS** configurável por variável de ambiente, sem hardcode do domínio do frontend
- Deploy via **Docker** (multi-stage build) — funciona em qualquer host com suporte a containers

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/usuarios` | Cria usuário (`nome`, `email`, `cpf`, `senha`) |
| `GET` | `/api/usuarios` | Lista usuários |
| `GET` | `/api/usuarios/{id}` | Busca usuário por id |
| `PUT` | `/api/usuarios/{id}` | Atualiza usuário (`senha` opcional — omitida mantém a atual) |
| `DELETE` | `/api/usuarios/{id}` | Remove usuário |
| `GET` | `/api/health` | Health check |

Erros de validação retornam `400` com um mapa `{campo: mensagem}`; recurso não encontrado retorna `404 {"error": "..."}`.

## Rodando localmente (sem depender do Neon)

Pré-requisito: Java 17+ e Maven.

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Sobe com H2 em memória em `http://localhost:8080`, console H2 em `/h2-console`.

## Arquitetura de deploy

```mermaid
flowchart LR
    A[Angular · Vercel] -- HTTPS/JSON --> B[Spring Boot · Render]
    B -- JDBC/SSL --> C[(Postgres · Neon)]
```

## Deploy em produção

O Vercel não roda Java, então esta API vai para um host com suporte a containers (Render, free tier sem cartão de crédito).

### 1. Banco de dados (Neon)

1. Crie um projeto em https://neon.tech (free tier).
2. Copie a connection string (formato `postgresql://user:pass@host/db?sslmode=require`).
3. Converta para JDBC trocando o prefixo: `jdbc:postgresql://host/db?sslmode=require`.

### 2. Deploy da API (Render)

1. Acesse https://render.com e crie conta (pode logar com GitHub).
2. No dashboard, **New** → **Web Service** → conecte este repositório.
3. O Render detecta o `render.yaml` automaticamente (runtime Docker, plano free).
4. Preencha as variáveis de ambiente pedidas (veja tabela abaixo).
5. **Create Web Service** — o Render builda a imagem Docker e sobe.

Após o deploy, a API fica em `https://<app>.onrender.com`. No plano free o serviço "dorme" após 15 minutos sem requisições — a primeira chamada depois disso demora ~30-50s pra acordar.

### Variáveis de ambiente

| Variável | Exemplo | Descrição |
|---|---|---|
| `DATABASE_URL` | `jdbc:postgresql://ep-xxx.neon.tech/neondb?sslmode=require` | Connection string JDBC do Neon |
| `DATABASE_USERNAME` | `neondb_owner` | Usuário do Neon |
| `DATABASE_PASSWORD` | `••••••••` | Senha do Neon |
| `CORS_ALLOWED_ORIGINS` | `https://seu-app.vercel.app,http://localhost:4200` | Domínios liberados por CORS, separados por vírgula |

### 3. Aponte o frontend para a API

No repositório do `ProjetoAngular`, edite `src/environments/environment.prod.ts` com a URL do passo 2 e faça o deploy no Vercel.

## Estrutura

```
src/main/java/com/gerenciamento/
├── common/       # exceções de negócio + handler global + health check
├── config/       # CORS e bean do PasswordEncoder
├── usuario/      # entidade, repositório, service, controller
│   └── dto/       # request / update / response
└── validation/   # validador de CPF customizado
```

## Autor

**Luis Carlos** — [github.com/luis-ferreira-jr](https://github.com/luis-ferreira-jr)
