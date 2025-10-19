# RPG Educativo - Backend

API REST para o jogo RPG Educativo desenvolvida com Spring Boot.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.5.6
- PostgreSQL 14
- Docker & Docker Compose
- Maven

## ⚙️ Configuração Local

### 1. Pré-requisitos

- Java 17+
- Docker e Docker Compose
- Maven (opcional, o projeto inclui Maven Wrapper)

### 2. Configurar variáveis de ambiente

Copie o arquivo de exemplo e configure suas credenciais:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Edite o arquivo `application.properties` e configure:
- **spring.datasource.url**: URL do seu banco PostgreSQL
- **spring.datasource.username**: Usuário do banco
- **spring.datasource.password**: Senha do banco
- **jwt.secret**: Uma chave secreta forte (mínimo 32 caracteres)

### 3. Executar com Docker

```bash
# Construir e iniciar os containers
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Parar os containers
docker-compose down

# Parar e remover volumes (limpa o banco de dados)
docker-compose down -v
```

A aplicação estará disponível em: `http://localhost:8000`

### 4. Executar localmente (sem Docker)

```bash
# Compilar
./mvnw clean package -DskipTests

# Executar
java -jar target/rpg-backend-0.0.1-SNAPSHOT.jar
```

## 🐳 Deploy no Render

### 1. Preparar a imagem Docker

```bash
# Build da imagem
docker build -t seu-usuario/rpg-backend:latest .

# Login no Docker Hub
docker login

# Push da imagem
docker push seu-usuario/rpg-backend:latest
```

### 2. Configurar no Render

1. Crie um banco PostgreSQL no Render
2. Crie um Web Service apontando para sua imagem Docker
3. Configure as variáveis de ambiente:
   - `SPRING_DATASOURCE_URL`: URL do banco PostgreSQL do Render
   - `SPRING_DATASOURCE_USERNAME`: Usuário do banco
   - `SPRING_DATASOURCE_PASSWORD`: Senha do banco
   - `JWT_SECRET`: Sua chave JWT secreta
   - `SPRING_PROFILES_ACTIVE`: `docker` ou `prod`

## 📝 Variáveis de Ambiente

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `SPRING_DATASOURCE_URL` | URL de conexão com PostgreSQL | `jdbc:postgresql://localhost:5432/rpg_game_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `sua_senha_segura` |
| `JWT_SECRET` | Chave secreta para JWT | `uma_chave_muito_secreta_32chars` |
| `SERVER_PORT` | Porta da aplicação | `8000` |

## 🔒 Segurança

⚠️ **IMPORTANTE**: Nunca commite o arquivo `application.properties` com credenciais reais!

- O arquivo `application.properties` está no `.gitignore`
- Use `application.properties.example` como template
- Configure variáveis de ambiente em produção

## 📚 Endpoints Principais

- `POST /api/auth/register` - Registro de usuário
- `POST /api/auth/login` - Login
- `GET /api/classes` - Listar classes disponíveis
- `GET /api/characters` - Listar personagens (requer autenticação)
- E muitos outros...

## 🤝 Contribuindo

1. Copie o `application.properties.example` para `application.properties`
2. Configure suas credenciais locais
3. Nunca commite o `application.properties` original

