# RPG Educativo - Backend Spring Boot

## 📋 Descrição

Backend completo do RPG Educativo migrado de TypeScript/Node.js para Java 17 + Spring Boot 3.

Sistema de RPG educacional baseado em perguntas e respostas com sistema de batalhas, progressão de personagens, e hub interativo.

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA / Hibernate**
- **Spring Security + JWT**
- **PostgreSQL**
- **Lombok**
- **Maven**

## 📦 Estrutura do Projeto

```
rpg-backend/
├── src/main/java/com/game/rpgbackend/
│   ├── config/              # Configurações (Security, Game)
│   ├── controller/          # Controllers REST
│   ├── domain/              # Entidades JPA
│   ├── dto/                 # DTOs de Request/Response
│   ├── enums/               # Enumerações
│   ├── exception/           # Exceções customizadas
│   ├── repository/          # Repositórios JPA
│   ├── security/            # JWT e filtros de segurança
│   └── service/             # Lógica de negócio
└── src/main/resources/
    └── application.properties
```

## ⚙️ Configuração

### 1. Banco de Dados

Configure as credenciais do PostgreSQL em `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rpg_game_db
spring.datasource.username=postgres
spring.datasource.password=RPGedu
```

### 2. JWT Secret

Configure uma chave secreta forte para JWT:

```properties
jwt.secret=SUA_CHAVE_SECRETA_AQUI
jwt.expiration=36000000
```

### 3. Executar o Projeto

```bash
mvn clean install
mvn spring-boot:run
```

O servidor estará disponível em: `http://localhost:8080`

## 🔐 Autenticação

### Registrar Usuário
```http
POST /api/auth/register
Content-Type: application/json

{
  "nome_usuario": "jogador123",
  "email": "jogador@email.com",
  "senha": "senha123"
}
```

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "nome_usuario": "jogador123",
  "senha": "senha123"
}
```

**Resposta:**
```json
{
  "message": "Login bem-sucedido!",
  "user": {
    "id": 1,
    "nome_usuario": "jogador123",
    "email": "jogador@email.com"
  },
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

## 📚 APIs Disponíveis

### 🎭 Classes

#### Listar todas as classes
```http
GET /api/classes
```

#### Buscar classe por ID
```http
GET /api/classes/{id}
```

### 👤 Personagens

#### Criar personagem
```http
POST /api/characters
Authorization: Bearer {token}
Content-Type: application/json

{
  "classe": "Mago"
}
```

#### Buscar personagem
```http
GET /api/characters/{id}
Authorization: Bearer {token}
```

#### Salvar progresso
```http
PUT /api/characters/{id}/progress?xp=100&hp=80
Authorization: Bearer {token}
```

### ⚔️ Batalha

#### Iniciar batalha
```http
POST /api/battle/start
Authorization: Bearer {token}
Content-Type: application/json

{
  "monsterId": 1,
  "difficulty": "Médio"
}
```

#### Responder pergunta
```http
POST /api/battle/answer
Authorization: Bearer {token}
Content-Type: application/json

{
  "battleId": 1234567890,
  "questionId": 5,
  "answer": "A"
}
```

#### Executar ação (Atacar/Defender/Habilidade)
```http
POST /api/battle/action
Authorization: Bearer {token}
Content-Type: application/json

{
  "action": "ATTACK"
}
```

**Ações disponíveis:** `ATTACK`, `DEFEND`, `ABILITY`

### 🎯 Hub

#### Torre do Conhecimento

```http
# Listar skills disponíveis
GET /api/hub/tower/skills
Authorization: Bearer {token}

# Listar conteúdos disponíveis
GET /api/hub/tower/content?level=5
Authorization: Bearer {token}

# Buscar conteúdo específico
GET /api/hub/tower/content/{id}?level=5
Authorization: Bearer {token}

# Comprar skill
POST /api/hub/tower/skills/purchase
Authorization: Bearer {token}
Content-Type: application/json

{
  "skillId": 1
}
```

#### Biblioteca Silenciosa

```http
# Listar livros
GET /api/hub/library/books
Authorization: Bearer {token}

# Detalhes do livro
GET /api/hub/library/books/{id}
Authorization: Bearer {token}
```

#### Palco da Retórica

```http
# Listar professores/NPCs
GET /api/hub/stage/professors
Authorization: Bearer {token}

# Diálogos do professor
GET /api/hub/stage/professors/{id}/dialogues
Authorization: Bearer {token}
```

#### Sebo da Linguística (Loja)

```http
# Listar lojas
GET /api/hub/store/shops
Authorization: Bearer {token}

# Comprar item
POST /api/hub/store/purchase
Authorization: Bearer {token}
Content-Type: application/json

{
  "itemId": 1
}
```

#### Estatísticas do Jogador

```http
# Ver estatísticas
GET /api/hub/player/stats
Authorization: Bearer {token}

# Atualizar estatísticas
PUT /api/hub/player/stats
Authorization: Bearer {token}
Content-Type: application/json

{
  "gold": 100,
  "experience": 500
}

# Ver conquistas
GET /api/hub/player/achievements
Authorization: Bearer {token}

# Histórico de batalhas
GET /api/hub/player/battle-history
Authorization: Bearer {token}

# Rankings
GET /api/hub/player/rankings
```

### 💾 Saves

#### Salvar jogo
```http
POST /api/saves
Authorization: Bearer {token}
Content-Type: application/json

{
  "characterId": 1,
  "slotName": "save1",
  "characterState": "{\"hp\":80,\"xp\":100}"
}
```

#### Listar saves
```http
GET /api/saves
Authorization: Bearer {token}
```

### ❓ Perguntas

```http
GET /api/questions/random?difficulty=Médio&level=5&contentId=1
Authorization: Bearer {token}
```

### 💬 Diálogos

```http
# Buscar diálogos por nível
GET /api/dialogs?level=5&contentId=1
Authorization: Bearer {token}

# Buscar diálogo específico
GET /api/dialogs/{id}?level=5
Authorization: Bearer {token}
```

## 🎮 Sistema de Batalha

### Mecânicas

1. **Custos de Energia:**
   - Ataque: 2 energia
   - Defesa: 1 energia
   - Habilidade: 3 energia

2. **Recuperação:**
   - +1 energia ao acertar uma pergunta

3. **Dificuldades:**
   - Fácil
   - Médio
   - Difícil

### Habilidades por Classe

- **Mago**: Clarividência (remove uma opção errada)
- **Tank**: Eu Aguento! (bloqueia próximo dano)
- **Lutador**: Investida (próximo acerto causa dano extra)
- **Paladino**: Cura (+10 HP)
- **Ladino**: Roubo (fornece uma dica)
- **Bardo**: Lábia (pergunta desafio - tudo ou nada)

## 📊 Sistema de Progressão

### Level Up

- XP base necessário: 100
- Multiplicador: 1.5 por nível
- Recompensa: +1 ponto de habilidade

### Fórmula XP
```
XP_necessário = 100 * (nível_atual ^ 1.5)
```

## 🔒 Segurança

- Autenticação JWT
- Tokens expiram em 10 horas
- Senhas criptografadas com BCrypt
- CORS configurado
- Session Stateless

## 🛠️ Desenvolvimento

### Compilar
```bash
mvn clean compile
```

### Testar
```bash
mvn test
```

### Gerar JAR
```bash
mvn clean package
```

### Executar JAR
```bash
java -jar target/rpg-backend-0.0.1-SNAPSHOT.jar
```

## 📝 Notas

- Sistema de batalha em memória (não persiste entre restarts)
- Todas as rotas (exceto `/api/auth/**` e `/api/classes/**`) requerem autenticação
- O token JWT deve ser enviado no header: `Authorization: Bearer {token}`

## 🐛 Debug

Para ver logs detalhados, o projeto está configurado com nível DEBUG:

```properties
logging.level.com.game.rpgbackend=DEBUG
logging.level.org.springframework.security=DEBUG
```

## 📄 Licença

Este projeto é parte do RPG Educativo.

