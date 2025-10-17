package com.game.rpgbackend.config;

import com.game.rpgbackend.domain.*;
import com.game.rpgbackend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(
            ClassRepository classRepository,
            ContentRepository contentRepository,
            QuestionRepository questionRepository,
            MonsterRepository monsterRepository,
            ItemRepository itemRepository,
            LojaRepository lojaRepository,
            ItemLojaRepository itemLojaRepository
    ) {
        return args -> {
            System.out.println("Iniciando o processo de seed...");
            System.out.println("Iniciando o processo de limpeza...");

            // 1. Limpar tabelas de junção primeiro
            itemLojaRepository.deleteAll();
            System.out.println("Tabela ItemLoja limpa.");

            // 2. Limpar tabelas principais
            lojaRepository.deleteAll();
            itemRepository.deleteAll();
            questionRepository.deleteAll();
            contentRepository.deleteAll();
            monsterRepository.deleteAll();
            classRepository.deleteAll();
            System.out.println("Tabelas principais limpas.");

            // Criar classes
            List<GameClass> classes = new ArrayList<>();

            GameClass tank = new GameClass();
            tank.setName("tank");
            tank.setHp(150);
            tank.setStamina(12);
            tank.setStrength(15);
            tank.setIntelligence(2);
            classes.add(tank);

            GameClass mago = new GameClass();
            mago.setName("mago");
            mago.setHp(80);
            mago.setStamina(12);
            mago.setStrength(2);
            mago.setIntelligence(25);
            classes.add(mago);

            GameClass lutador = new GameClass();
            lutador.setName("lutador");
            lutador.setHp(120);
            lutador.setStamina(12);
            lutador.setStrength(20);
            lutador.setIntelligence(2);
            classes.add(lutador);

            GameClass ladino = new GameClass();
            ladino.setName("ladino");
            ladino.setHp(90);
            ladino.setStamina(12);
            ladino.setStrength(20);
            ladino.setIntelligence(9);
            classes.add(ladino);

            GameClass paladino = new GameClass();
            paladino.setName("paladino");
            paladino.setHp(130);
            paladino.setStamina(12);
            paladino.setStrength(18);
            paladino.setIntelligence(5);
            classes.add(paladino);

            GameClass bardo = new GameClass();
            bardo.setName("bardo");
            bardo.setHp(85);
            bardo.setStamina(12);
            bardo.setStrength(5);
            bardo.setIntelligence(15);
            classes.add(bardo);

            classRepository.saveAll(classes);
            System.out.println(classes.size() + " classes criadas.");

            // Criar conteúdos
            List<Content> contents = new ArrayList<>();

            Content verboToBe = new Content();
            verboToBe.setNome("Verbo To Be");
            verboToBe.setDescricao("O verbo mais básico e importante do inglês");
            verboToBe.setLevelMinimo(1);
            contents.add(verboToBe);

            Content presenteSimples = new Content();
            presenteSimples.setNome("Presente Simples");
            presenteSimples.setDescricao("Usado para falar de rotinas e verdades universais");
            presenteSimples.setLevelMinimo(2);
            contents.add(presenteSimples);

            Content vocabularioBasico = new Content();
            vocabularioBasico.setNome("Vocabulário Básico");
            vocabularioBasico.setDescricao("Palavras essenciais do dia a dia");
            vocabularioBasico.setLevelMinimo(1);
            contents.add(vocabularioBasico);

            Content presentePerfeito = new Content();
            presentePerfeito.setNome("Presente Perfeito");
            presentePerfeito.setDescricao("Usado para conectar o passado com o presente");
            presentePerfeito.setLevelMinimo(5);
            contents.add(presentePerfeito);

            Content expressoesIdiomaticas = new Content();
            expressoesIdiomaticas.setNome("Expressões Idiomáticas");
            expressoesIdiomaticas.setDescricao("Frases com significados especiais na cultura");
            expressoesIdiomaticas.setLevelMinimo(7);
            contents.add(expressoesIdiomaticas);

            contentRepository.saveAll(contents);
            System.out.println(contents.size() + " conteúdos criados.");

            // Criar questões
            List<Question> questions = new ArrayList<>();

            // Perguntas fáceis do Verbo To Be
            questions.add(createQuestion("Complete a frase: \"She ___ a student.\"", "is", "are", "am", "is", "Facil", verboToBe, 1));
            questions.add(createQuestion("Qual o verbo \"to be\" para \"you\"?", "is", "am", "are", "are", "Facil", verboToBe, 1));
            questions.add(createQuestion("Complete a frase: \"They ___ playing\".", "is", "am", "are", "are", "Facil", verboToBe, 1));
            questions.add(createQuestion("Complete a frase: \"I ___ a boy\".", "am", "is", "are", "am", "Facil", verboToBe, 1));
            questions.add(createQuestion("Complete a frase: \"The sun ___ hot\".", "are", "am", "is", "is", "Facil", verboToBe, 1));

            // Perguntas de Vocabulário Básico
            questions.add(createQuestion("Traduza: \"A dog\".", "Um gato", "Um cachorro", "Um pássaro", "Um cachorro", "Facil", vocabularioBasico, 1));
            questions.add(createQuestion("O plural de \"car\" é:", "cars", "caros", "carss", "cars", "Facil", vocabularioBasico, 1));
            questions.add(createQuestion("Qual a tradução de \"hello\"?", "Adeus", "Olá", "Obrigado", "Olá", "Facil", vocabularioBasico, 1));
            questions.add(createQuestion("O antônimo de \"big\" é:", "small", "tall", "short", "small", "Facil", vocabularioBasico, 1));
            questions.add(createQuestion("Qual a cor \"red\"?", "Azul", "Vermelho", "Verde", "Vermelho", "Facil", vocabularioBasico, 1));

            // Presente Simples
            questions.add(createQuestion("Traduza: \"I like to read\".", "Eu gosto de comer", "Eu gosto de correr", "Eu gosto de ler", "Eu gosto de ler", "Medio", presenteSimples, 2));
            questions.add(createQuestion("Complete a frase: \"She ___ to school every day\".", "go", "goes", "going", "goes", "Medio", presenteSimples, 2));
            questions.add(createQuestion("Traduza: \"He works at a bank\".", "Ele trabalhou no banco", "Ele trabalha em um banco", "Ele vai trabalhar no banco", "Ele trabalha em um banco", "Medio", presenteSimples, 2));

            // Presente Perfeito
            questions.add(createQuestion("Qual a forma correta do Present Perfect de \"to be\"?", "was/were", "have been/has been", "had been", "have been/has been", "Dificil", presentePerfeito, 5));
            questions.add(createQuestion("Complete a frase: \"I have ___ this movie twice\".", "see", "saw", "seen", "seen", "Dificil", presentePerfeito, 5));
            questions.add(createQuestion("Qual a forma correta do Present Perfect de \"to go\"?", "went", "have gone", "have goed", "have gone", "Dificil", presentePerfeito, 5));

            // Expressões Idiomáticas
            questions.add(createQuestion("O que significa \"break a leg\"?", "Quebre a perna", "Boa sorte", "Desastre", "Boa sorte", "Medio", expressoesIdiomaticas, 7));
            questions.add(createQuestion("O que significa \"it's raining cats and dogs\"?", "Está chovendo muito", "Está chovendo gatos e cachorros", "A tempestade é forte", "Está chovendo muito", "Dificil", expressoesIdiomaticas, 7));
            questions.add(createQuestion("O que significa \"break the bank\"?", "Quebrar o banco", "Gastar muito dinheiro", "Assaltar um banco", "Gastar muito dinheiro", "Dificil", expressoesIdiomaticas, 7));

            questionRepository.saveAll(questions);
            System.out.println(questions.size() + " questões criadas.");

            // Criar monstros
            List<Monster> monsters = new ArrayList<>();

            Monster diabrete = new Monster();
            diabrete.setNome("Diabrete Errôneo");
            diabrete.setHp(150);
            diabrete.setDano(10);
            monsters.add(diabrete);

            Monster harpia = new Monster();
            harpia.setNome("Harpia Indagada");
            harpia.setHp(120);
            harpia.setDano(15);
            monsters.add(harpia);

            Monster zumbi = new Monster();
            zumbi.setNome("Zumbi Demente");
            zumbi.setHp(250);
            zumbi.setDano(8);
            monsters.add(zumbi);

            Monster centauro = new Monster();
            centauro.setNome("Centauro Questionador");
            centauro.setHp(200);
            centauro.setDano(12);
            monsters.add(centauro);

            Monster esqueleto = new Monster();
            esqueleto.setNome("Esqueleto da Sintaxe");
            esqueleto.setHp(500);
            esqueleto.setDano(20);
            monsters.add(esqueleto);

            Monster lexicografo = new Monster();
            lexicografo.setNome("Lexicógrafo, o Guardião do Vazio");
            lexicografo.setHp(1500);
            lexicografo.setDano(40);
            monsters.add(lexicografo);

            Monster malak = new Monster();
            malak.setNome("Malak, O Silenciador");
            malak.setHp(5000);
            malak.setDano(100);
            monsters.add(malak);

            monsterRepository.saveAll(monsters);
            System.out.println(monsters.size() + " monstros criados.");

            // Criar itens
            List<Item> items = new ArrayList<>();

            Item pocaoVida = new Item();
            pocaoVida.setName("Poção de Vida Pequena");
            pocaoVida.setType("potion");
            pocaoVida.setValue(25);
            pocaoVida.setDescription("Um frasco com líquido vermelho que restaura 50 pontos de vida.");
            items.add(pocaoVida);

            Item pocaoMana = new Item();
            pocaoMana.setName("Poção de Mana Pequena");
            pocaoMana.setType("potion");
            pocaoMana.setValue(30);
            pocaoMana.setDescription("Líquido azul cintilante que restaura 30 pontos de mana.");
            items.add(pocaoMana);

            Item espadaCurta = new Item();
            espadaCurta.setName("Espada Curta de Ferro");
            espadaCurta.setType("weapon");
            espadaCurta.setValue(80);
            espadaCurta.setDescription("Uma espada confiável para qualquer aventureiro iniciante.");
            items.add(espadaCurta);

            Item arco = new Item();
            arco.setName("Arco Simples");
            arco.setType("weapon");
            arco.setValue(75);
            arco.setDescription("Feito de madeira envergada, bom para ataques à distância.");
            items.add(arco);

            Item cajado = new Item();
            cajado.setName("Cajado de Aprendiz");
            cajado.setType("weapon");
            cajado.setValue(90);
            cajado.setDescription("Canaliza magias simples. Um brilho fraco emana de sua ponta.");
            items.add(cajado);

            Item machado = new Item();
            machado.setName("Machado de Batalha");
            machado.setType("weapon");
            machado.setValue(120);
            machado.setDescription("Pesado e lento, mas com um golpe devastador.");
            items.add(machado);

            Item tunica = new Item();
            tunica.setName("Túnica de Couro");
            tunica.setType("armor");
            tunica.setValue(60);
            tunica.setDescription("Oferece proteção básica sem restringir os movimentos.");
            items.add(tunica);

            Item manto = new Item();
            manto.setName("Manto do Conjurador");
            manto.setType("armor");
            manto.setValue(70);
            manto.setDescription("Roupas encantadas que melhoram o fluxo de mana.");
            items.add(manto);

            Item botas = new Item();
            botas.setName("Botas de Viagem Simples");
            botas.setType("armor");
            botas.setValue(35);
            botas.setDescription("Botas resistentes que facilitam longas caminhadas.");
            items.add(botas);

            Item gema = new Item();
            gema.setName("Gema Bruta");
            gema.setType("treasure");
            gema.setValue(200);
            gema.setDescription("Uma pedra preciosa que pode ser vendida por um bom preço.");
            items.add(gema);

            Item moeda = new Item();
            moeda.setName("Moeda de Ouro");
            moeda.setType("currency");
            moeda.setValue(1);
            moeda.setDescription("Moeda utilizada para comprar itens.");
            items.add(moeda);

            itemRepository.saveAll(items);
            System.out.println(items.size() + " itens criados.");

            // Criar loja e adicionar itens
            Loja loja = new Loja();
            loja = lojaRepository.save(loja);

            List<ItemLoja> itensLoja = new ArrayList<>();
            for (Item item : items) {
                if (!item.getName().equals("Gema Bruta") && !item.getName().equals("Moeda de Ouro")) {
                    ItemLoja itemLoja = new ItemLoja();
                    itemLoja.setLojaId(loja.getId());
                    itemLoja.setItemId(item.getId());
                    itemLoja.setLoja(loja);
                    itemLoja.setItem(item);
                    itemLoja.setPreco((int) Math.round((item.getValue() != null ? item.getValue() : 0) * 1.20));
                    itemLoja.setQuantidade(10);
                    itensLoja.add(itemLoja);
                }
            }

            itemLojaRepository.saveAll(itensLoja);
            System.out.println(itensLoja.size() + " itens adicionados ao estoque da loja.");
            System.out.println("Seeding finalizado com sucesso!");
        };
    }

    private Question createQuestion(String texto, String opcaoA, String opcaoB, String opcaoC,
                                     String resposta, String dificuldade, Content content, int levelMinimo) {
        Question q = new Question();
        q.setTextoPergunta(texto);
        q.setOpcaoA(opcaoA);
        q.setOpcaoB(opcaoB);
        q.setOpcaoC(opcaoC);
        q.setRespostaCorreta(resposta);
        q.setDificuldade(dificuldade);
        q.setConteudo(content.getNome().toLowerCase().replace(" ", "_"));
        q.setContent(content);
        q.setLevelMinimo(levelMinimo);
        return q;
    }
}
