# Guia de Deploy no Render

## Pré-requisitos

1. ✅ Conta no Render (https://render.com)
2. ✅ Conta no Docker Hub com a imagem: `murilofurtado/rpg-backend-java-api:latest`
3. ✅ Banco de dados PostgreSQL criado no Render

---

## Passo 1: Criar Banco de Dados PostgreSQL no Render

1. Acesse: https://dashboard.render.com
2. Clique em **"New +"** → **"PostgreSQL"**
3. Configure:
   - **Name**: `rpg-game-db`
   - **Database**: `rpg_game_db`
   - **User**: `rpg_game_db_user` (gerado automaticamente)
   - **Region**: Escolha a região mais próxima (ex: Oregon - US West)
   - **Instance Type**: Free (para teste)
4. Clique em **"Create Database"**
5. Aguarde a criação (1-2 minutos)
6. **IMPORTANTE**: Copie a **Internal Database URL** (formato: `postgresql://user:password@host/database`)
   - Exemplo: `postgresql://rpg_game_db_user:S1VZbJgRFXuB9UZbGV5rEPKmigsyTIAs@dpg-xxx-a/rpg_game_db`

---

## Passo 2: Criar Web Service no Render

1. Acesse: https://dashboard.render.com
2. Clique em **"New +"** → **"Web Service"**
3. Selecione **"Deploy from Docker image"**
4. Configure:

### Configuração Básica:
- **Image URL**: `murilofurtado/rpg-backend-java-api:latest`
- **Name**: `rpg-backend-api`
- **Region**: Mesma região do banco de dados
- **Instance Type**: Free

### Configuração Avançada:

#### Environment Variables (Variáveis de Ambiente):
Adicione as seguintes variáveis:

| Key | Value | Descrição |
|-----|-------|-----------|
| `DATABASE_URL` | `postgresql://rpg_game_db_user:senha@host/rpg_game_db` | URL INTERNA do banco (copie do Passo 1) |
| `JWT_SECRET` | `UMA_FRASE_MUITO_SECRETA_E_DIFICIL_DE_ADIVINHAR_12345` | Chave secreta do JWT |
| `SPRING_PROFILES_ACTIVE` | `render` | Perfil do Spring Boot para usar |
| `PORT` | `8000` | Porta da aplicação |

**⚠️ IMPORTANTE**: Use a **Internal Database URL** (não a External URL) para melhor performance e segurança.

#### Health Check:
- **Health Check Path**: `/actuator/health` (se habilitado) ou deixe vazio

---

## Passo 3: Deploy

1. Clique em **"Create Web Service"**
2. O Render irá:
   - Fazer pull da imagem do Docker Hub
   - Configurar as variáveis de ambiente
   - Iniciar a aplicação
3. Aguarde o deploy (3-5 minutos)
4. Quando o status ficar **"Live"**, sua API estará no ar!

---

## Passo 4: Testar a API

Sua API estará disponível em: `https://rpg-backend-api.onrender.com`

### Endpoints para testar:

#### 1. Health Check (se configurado):
```bash
GET https://rpg-backend-api.onrender.com/actuator/health
```

#### 2. Registro de usuário:
```bash
POST https://rpg-backend-api.onrender.com/api/auth/register
Content-Type: application/json

{
  "username": "teste",
  "email": "teste@email.com",
  "password": "senha123"
}
```

#### 3. Login:
```bash
POST https://rpg-backend-api.onrender.com/api/auth/login
Content-Type: application/json

{
  "username": "teste",
  "password": "senha123"
}
```

---

## Passo 5: Atualizar a Imagem (Quando necessário)

Quando você fizer alterações no código e quiser atualizar:

1. **Build e Push da nova imagem**:
```bash
# Build da nova versão
docker build -t murilofurtado/rpg-backend-java-api:1.2 -t murilofurtado/rpg-backend-java-api:latest .

# Push para Docker Hub
docker push murilofurtado/rpg-backend-java-api:1.2
docker push murilofurtado/rpg-backend-java-api:latest
```

2. **No Render Dashboard**:
   - Vá até seu serviço `rpg-backend-api`
   - Clique em **"Manual Deploy"** → **"Deploy latest commit"**
   - Ou configure **Auto-Deploy** para atualizar automaticamente quando houver push no Docker Hub

---

## Configurações Adicionais (Opcional)

### Auto-Deploy do Docker Hub:
1. No Render Dashboard, vá em seu Web Service
2. Settings → **Deploy Hook**
3. Configure um webhook no Docker Hub para acionar deploy automático

### Logs:
- Acesse: Dashboard → Seu serviço → **"Logs"**
- Veja logs em tempo real da aplicação

### Domínio Customizado:
- Settings → **Custom Domain**
- Adicione seu domínio personalizado

---

## Troubleshooting

### Erro: "Application failed to start"
- Verifique os logs no Render Dashboard
- Confirme que `DATABASE_URL` está correta
- Verifique se o banco de dados está ativo

### Erro: "Connection refused"
- Certifique-se de usar a **Internal Database URL**
- Verifique se o banco e a API estão na mesma região

### Erro: "Port already in use"
- Certifique-se que a variável `PORT` está configurada como `8000`
- O Render pode usar sua própria porta, por isso use `${PORT:8000}` no application.properties

---

## Variáveis de Ambiente - Resumo

```env
DATABASE_URL=postgresql://rpg_game_db_user:SENHA_AQUI@dpg-xxx-a.oregon-postgres.render.com/rpg_game_db
JWT_SECRET=UMA_FRASE_MUITO_SECRETA_E_DIFICIL_DE_ADIVINHAR_12345
SPRING_PROFILES_ACTIVE=render
PORT=8000
```

---

## URLs Importantes

- **Render Dashboard**: https://dashboard.render.com
- **Docker Hub**: https://hub.docker.com/r/murilofurtado/rpg-backend-java-api
- **API URL**: https://rpg-backend-api.onrender.com (após deploy)

---

## Observações Finais

1. ✅ O plano **Free** do Render tem algumas limitações:
   - A aplicação "dorme" após 15 minutos de inatividade
   - Leva ~30 segundos para "acordar" na primeira requisição
   - 750 horas gratuitas por mês

2. ✅ Para produção, considere:
   - Upgrade para plano pago (mais estável)
   - Usar variável de ambiente para senhas (não hardcoded)
   - Habilitar SSL/TLS (já vem habilitado no Render)

3. ✅ Backup do banco:
   - Configure backups automáticos no painel do PostgreSQL
   - Exporte dados importantes periodicamente

---

**Dúvidas?** Consulte a documentação oficial: https://render.com/docs

