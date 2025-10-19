# 🚀 Guia de Deploy Automatizado - RPG Educativo Backend

Este documento explica como configurar o deploy automático da aplicação no Render usando GitHub Actions.

## 📋 Pré-requisitos

- Conta no GitHub
- Conta no Docker Hub
- Conta no Render
- Repositório Git configurado

## 🔧 Configuração do Deploy Automático

### Passo 1: Configurar Secrets no GitHub

Acesse seu repositório no GitHub e configure os seguintes secrets:

1. **Ir para Settings → Secrets and variables → Actions**
2. **Clicar em "New repository secret"**
3. **Adicionar os seguintes secrets:**

| Nome | Descrição | Onde obter |
|------|-----------|------------|
| `DOCKER_USERNAME` | Seu usuário do Docker Hub | `murilofurtado` |
| `DOCKER_PASSWORD` | Sua senha ou token do Docker Hub | Docker Hub → Account Settings → Security → New Access Token |
| `RENDER_SERVICE_ID` | ID do serviço no Render | URL do serviço: `srv-d3q3a28di3ps73bkichg` |
| `RENDER_API_KEY` | Chave de API do Render | Render Dashboard → Account Settings → API Keys → Create API Key |

### Passo 2: Obter o Service ID do Render

1. Acesse seu serviço `rpg-backend-api` no Render
2. Olhe a URL do navegador, será algo como:
   ```
   https://dashboard.render.com/web/srv-XXXXXXXXXXXXX
   ```
3. Copie o ID que começa com `srv-` (exemplo: `srv-d3q3a28di3ps73bkichg`)

### Passo 3: Gerar API Key do Render

1. No Render Dashboard, clique no seu avatar (canto superior direito)
2. Clique em **"Account Settings"**
3. No menu lateral, clique em **"API Keys"**
4. Clique em **"Create API Key"**
5. Dê um nome (exemplo: "GitHub Actions Deploy")
6. **Copie a chave gerada** (só aparece uma vez!)

### Passo 4: Gerar Docker Hub Access Token (opcional, mais seguro que senha)

1. Acesse: https://hub.docker.com/settings/security
2. Clique em **"New Access Token"**
3. Dê um nome: "GitHub Actions"
4. Permissões: **Read, Write, Delete**
5. **Copie o token gerado**

## 🔄 Como Funciona o Deploy Automático

Após a configuração, o fluxo será:

1. **Você faz alterações no código localmente**
2. **Commita e faz push para a branch `main`:**
   ```bash
   git add .
   git commit -m "feat: nova funcionalidade"
   git push origin main
   ```

3. **GitHub Actions detecta o push e automaticamente:**
   - ✅ Faz checkout do código
   - ✅ Configura o Docker Buildx
   - ✅ Faz login no Docker Hub
   - ✅ Builda a imagem Docker
   - ✅ Envia a imagem para o Docker Hub
   - ✅ Aciona o deploy no Render

4. **Render recebe o trigger e:**
   - ✅ Puxa a nova imagem do Docker Hub
   - ✅ Reinicia o serviço com a nova versão
   - ✅ Sua aplicação fica atualizada!

## 📊 Monitorando o Deploy

### No GitHub:
1. Acesse seu repositório
2. Clique na aba **"Actions"**
3. Veja o status do workflow em tempo real
4. Verde ✅ = Deploy bem-sucedido
5. Vermelho ❌ = Erro (clique para ver os logs)

### No Render:
1. Acesse o dashboard do Render
2. Clique no serviço `rpg-backend-api`
3. Veja os logs na aba **"Logs"**
4. Acompanhe o deploy em tempo real

## 🎯 Deploy Manual (quando necessário)

Você também pode acionar o deploy manualmente:

### Opção 1: Via GitHub Actions
1. Vá para a aba **"Actions"**
2. Clique no workflow **"Deploy to Render"**
3. Clique em **"Run workflow"**
4. Selecione a branch `main`
5. Clique em **"Run workflow"**

### Opção 2: Via Render Dashboard
1. Acesse o serviço no Render
2. Clique em **"Manual Deploy"**
3. Selecione **"Clear build cache & deploy"** (se houver problemas)

## 🔒 Segurança

- ✅ Nunca commite senhas ou tokens no código
- ✅ Use sempre GitHub Secrets para credenciais
- ✅ Use Access Tokens em vez de senhas quando possível
- ✅ Revogue tokens antigos se suspeitar de comprometimento

## 🐛 Troubleshooting

### Erro: "Invalid credentials" no Docker Hub
- Verifique se `DOCKER_USERNAME` e `DOCKER_PASSWORD` estão corretos
- Se usar token, certifique-se que tem permissões de Write

### Erro: "Service not found" no Render
- Verifique se `RENDER_SERVICE_ID` está correto
- Confirme que o Service ID começa com `srv-`

### Erro: "Unauthorized" no Render
- Verifique se `RENDER_API_KEY` está válida
- Gere uma nova API Key se necessário

### Deploy não inicia automaticamente
- Verifique se o workflow está na branch `main`
- Confirme que o arquivo está em `.github/workflows/deploy.yml`
- Veja a aba Actions para mensagens de erro

## 📝 Versionamento

O workflow cria duas tags para cada build:
- `murilofurtado/rpg-backend:latest` - Sempre aponta para a versão mais recente
- `murilofurtado/rpg-backend:<commit-sha>` - Tag específica do commit (útil para rollback)

## 🔄 Rollback (reverter para versão anterior)

Se algo der errado, você pode reverter:

### Opção 1: Via Git
```bash
git revert HEAD
git push origin main
```

### Opção 2: No Render Dashboard
1. Acesse o serviço
2. Clique em **"Events"**
3. Encontre o deploy anterior bem-sucedido
4. Clique em **"Rollback"**

## 📚 Recursos Adicionais

- [Documentação do GitHub Actions](https://docs.github.com/actions)
- [Documentação do Render](https://render.com/docs)
- [Docker Hub](https://hub.docker.com)

## ✅ Checklist de Configuração

- [ ] Secrets configurados no GitHub
- [ ] Service ID do Render copiado
- [ ] API Key do Render gerada
- [ ] Docker Hub Access Token criado (opcional)
- [ ] Primeiro push testado
- [ ] Deploy automático funcionando
- [ ] Logs monitorados no GitHub Actions
- [ ] Aplicação acessível no Render

---

**Pronto!** Agora você tem um pipeline de CI/CD completo. Cada push na branch `main` automaticamente atualiza sua aplicação em produção! 🚀

