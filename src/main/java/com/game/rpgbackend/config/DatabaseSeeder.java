package com.game.rpgbackend.config;

import com.game.rpgbackend.domain.*;
import com.game.rpgbackend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(
            ClassRepository classRepository,
            ContentRepository contentRepository,
            QuestionRepository questionRepository,
            MonsterRepository monsterRepository,
            ItemRepository itemRepository,
            StoreRepository storeRepository,
            ItemLojaRepository itemLojaRepository
    ) {
        return args -> {
            System.out.println("--- INICIANDO O DATABASE SEEDER INTELIGENTE ---");

            // Executa o seed para cada entidade de forma organizada
            seedClasses(classRepository);
            Map<String, Content> contentMap = seedContent(contentRepository);
            seedQuestions(questionRepository, contentMap);
            seedMonsters(monsterRepository);
            Map<String, Item> itemMap = seedItems(itemRepository);
            seedLoja(storeRepository, itemLojaRepository, itemMap);

            System.out.println("--- DATABASE SEEDER FINALIZADO ---");
        };
    }

    // Método para popular a tabela de Classes
    private void seedClasses(ClassRepository classRepository) {
        // Busca classes existentes pelo nome para evitar duplicatas
        Map<String, GameClass> existingClasses = classRepository.findAll().stream()
                .collect(Collectors.toMap(GameClass::getName, Function.identity()));

        List<GameClass> desiredClasses = new ArrayList<>();
        if (!existingClasses.containsKey("tank")) {
            GameClass tank = new GameClass();
            tank.setName("tank");
            tank.setHp(150);
            tank.setStamina(12);
            tank.setStrength(15);
            tank.setIntelligence(2);
            desiredClasses.add(tank);
        }
        if (!existingClasses.containsKey("mago")) {
            GameClass mago = new GameClass();
            mago.setName("mago");
            mago.setHp(80);
            mago.setStamina(12);
            mago.setStrength(2);
            mago.setIntelligence(25);
            desiredClasses.add(mago);
        }
        if (!existingClasses.containsKey("lutador")) {
            GameClass lutador = new GameClass();
            lutador.setName("lutador");
            lutador.setHp(120);
            lutador.setStamina(12);
            lutador.setStrength(20);
            lutador.setIntelligence(2);
            desiredClasses.add(lutador);
        }
        if (!existingClasses.containsKey("ladino")) {
            GameClass ladino = new GameClass();
            ladino.setName("ladino");
            ladino.setHp(90);
            ladino.setStamina(12);
            ladino.setStrength(20);
            ladino.setIntelligence(9);
            desiredClasses.add(ladino);
        }
        if (!existingClasses.containsKey("paladino")) {
            GameClass paladino = new GameClass();
            paladino.setName("paladino");
            paladino.setHp(130);
            paladino.setStamina(12);
            paladino.setStrength(18);
            paladino.setIntelligence(5);
            desiredClasses.add(paladino);
        }
        if (!existingClasses.containsKey("bardo")) {
            GameClass bardo = new GameClass();
            bardo.setName("bardo");
            bardo.setHp(85);
            bardo.setStamina(12);
            bardo.setStrength(5);
            bardo.setIntelligence(15);
            desiredClasses.add(bardo);
        }


        if (!desiredClasses.isEmpty()) {
            System.out.println("Inserindo " + desiredClasses.size() + " novas classes...");
            classRepository.saveAll(desiredClasses);
        } else {
            System.out.println("Tabela 'class' já está atualizada.");
        }
    }

    // Método para popular a tabela de Conteúdos
    private Map<String, Content> seedContent(ContentRepository contentRepository) {
        Map<String, Content> existingContent = contentRepository.findAll().stream()
                .collect(Collectors.toMap(Content::getContentName, Function.identity()));

        List<Content> desiredContent = new ArrayList<>();
        if (!existingContent.containsKey("Verbo To Be")) {
            Content verboToBe = new Content();
            verboToBe.setContentName("Verbo To Be");
            verboToBe.setDescription("O verbo mais básico e importante do inglês");
            verboToBe.setMinLevel(1);
            desiredContent.add(verboToBe);
        }
        if (!existingContent.containsKey("Presente Simples")) {
            Content presenteSimples = new Content();
            presenteSimples.setContentName("Presente Simples");
            presenteSimples.setDescription("Usado para falar de rotinas e verdades universais");
            presenteSimples.setMinLevel(2);
            desiredContent.add(presenteSimples);
        }
        if (!existingContent.containsKey("Vocabulário Básico")) {
            Content vocabularioBasico = new Content();
            vocabularioBasico.setContentName("Vocabulário Básico");
            vocabularioBasico.setDescription("Palavras essenciais do dia a dia");
            vocabularioBasico.setMinLevel(1);
            desiredContent.add(vocabularioBasico);
        }
        if (!existingContent.containsKey("Presente Perfeito")) {
            Content presentePerfeito = new Content();
            presentePerfeito.setContentName("Presente Perfeito");
            presentePerfeito.setDescription("Usado para conectar o passado com o presente");
            presentePerfeito.setMinLevel(5);
            desiredContent.add(presentePerfeito);
        }
        if (!existingContent.containsKey("Expressões Idiomáticas")) {
            Content expressoesIdiomaticas = new Content();
            expressoesIdiomaticas.setContentName("Expressões Idiomáticas");
            expressoesIdiomaticas.setDescription("Frases com significados especiais na cultura");
            expressoesIdiomaticas.setMinLevel(7);
            desiredContent.add(expressoesIdiomaticas);
        }

        if (!desiredContent.isEmpty()) {
            System.out.println("Inserindo " + desiredContent.size() + " novos conteúdos...");
            contentRepository.saveAll(desiredContent);
        } else {
            System.out.println("Tabela 'content' já está atualizada.");
        }
        // Retorna todos os conteúdos (antigos e novos) para uso posterior
        return contentRepository.findAll().stream()
                .collect(Collectors.toMap(Content::getContentName, Function.identity()));
    }

    // Método para popular a tabela de Monstros
    private void seedMonsters(MonsterRepository monsterRepository) {
        Map<String, Monster> existingMonsters = monsterRepository.findAll().stream()
                .collect(Collectors.toMap(Monster::getMonsterName, Function.identity()));

        List<Monster> desiredMonsters = new ArrayList<>();
        if (!existingMonsters.containsKey("Diabrete Errôneo")) {
            Monster diabrete = new Monster();
            diabrete.setMonsterName("Diabrete Errôneo");
            diabrete.setHp(150);
            diabrete.setMonsterDamage(10);
            desiredMonsters.add(diabrete);
        }
        if (!existingMonsters.containsKey("Harpia Indagada")) {
            Monster harpia = new Monster();
            harpia.setMonsterName("Harpia Indagada");
            harpia.setHp(120);
            harpia.setMonsterDamage(15);
            desiredMonsters.add(harpia);
        }
        if (!existingMonsters.containsKey("Zumbi Demente")) {
            Monster zumbi = new Monster();
            zumbi.setMonsterName("Zumbi Demente");
            zumbi.setHp(250);
            zumbi.setMonsterDamage(8);
            desiredMonsters.add(zumbi);
        }
        if (!existingMonsters.containsKey("Centauro Questionador")) {
            Monster centauro = new Monster();
            centauro.setMonsterName("Centauro Questionador");
            centauro.setHp(200);
            centauro.setMonsterDamage(12);
            desiredMonsters.add(centauro);
        }
        if (!existingMonsters.containsKey("Esqueleto da Sintaxe")) {
            Monster esqueleto = new Monster();
            esqueleto.setMonsterName("Esqueleto da Sintaxe");
            esqueleto.setHp(500);
            esqueleto.setMonsterDamage(20);
            desiredMonsters.add(esqueleto);
        }
        if (!existingMonsters.containsKey("Lexicógrafo, o Guardião do Vazio")) {
            Monster lexicografo = new Monster();
            lexicografo.setMonsterName("Lexicógrafo, o Guardião do Vazio");
            lexicografo.setHp(1500);
            lexicografo.setMonsterDamage(40);
            desiredMonsters.add(lexicografo);
        }
        if (!existingMonsters.containsKey("Malak, O Silenciador")) {
            Monster malak = new Monster();
            malak.setMonsterName("Malak, O Silenciador");
            malak.setHp(5000);
            malak.setMonsterDamage(100);
            desiredMonsters.add(malak);
        }

        if (!desiredMonsters.isEmpty()) {
            System.out.println("Inserindo " + desiredMonsters.size() + " novos monstros...");
            monsterRepository.saveAll(desiredMonsters);
        } else {
            System.out.println("Tabela 'monster' já está atualizada.");
        }
    }

    // Método para popular a tabela de Itens
    private Map<String, Item> seedItems(ItemRepository itemRepository) {
        Map<String, Item> existingItems = itemRepository.findAll().stream()
                .collect(Collectors.toMap(Item::getName, Function.identity()));

        List<Item> desiredItems = new ArrayList<>();

        if (!existingItems.containsKey("Poção de Vida Pequena")) {
            Item pocaoVida = new Item();
            pocaoVida.setName("Poção de Vida Pequena");
            pocaoVida.setType("potion");
            pocaoVida.setValue(25);
            pocaoVida.setDescription("Um frasco com líquido vermelho que restaura 50 pontos de vida.");
            desiredItems.add(pocaoVida);
        }
        if (!existingItems.containsKey("Poção de Mana Pequena")) {
            Item pocaoMana = new Item();
            pocaoMana.setName("Poção de Mana Pequena");
            pocaoMana.setType("potion");
            pocaoMana.setValue(30);
            pocaoMana.setDescription("Líquido azul cintilante que restaura 30 pontos de mana.");
            desiredItems.add(pocaoMana);
        }
        if (!existingItems.containsKey("Espada Curta de Ferro")) {
            Item espadaCurta = new Item();
            espadaCurta.setName("Espada Curta de Ferro");
            espadaCurta.setType("weapon");
            espadaCurta.setValue(80);
            espadaCurta.setDescription("Uma espada confiável para qualquer aventureiro iniciante.");
            desiredItems.add(espadaCurta);
        }
        if (!existingItems.containsKey("Arco Simples")) {
            Item arco = new Item();
            arco.setName("Arco Simples");
            arco.setType("weapon");
            arco.setValue(75);
            arco.setDescription("Feito de madeira envergada, bom para ataques à distância.");
            desiredItems.add(arco);
        }
        if (!existingItems.containsKey("Cajado de Aprendiz")) {
            Item cajado = new Item();
            cajado.setName("Cajado de Aprendiz");
            cajado.setType("weapon");
            cajado.setValue(90);
            cajado.setDescription("Canaliza magias simples. Um brilho fraco emana de sua ponta.");
            desiredItems.add(cajado);
        }
        if (!existingItems.containsKey("Machado de Batalha")) {
            Item machado = new Item();
            machado.setName("Machado de Batalha");
            machado.setType("weapon");
            machado.setValue(120);
            machado.setDescription("Pesado e lento, mas com um golpe devastador.");
            desiredItems.add(machado);
        }
        if (!existingItems.containsKey("Túnica de Couro")) {
            Item tunica = new Item();
            tunica.setName("Túnica de Couro");
            tunica.setType("armor");
            tunica.setValue(60);
            tunica.setDescription("Oferece proteção básica sem restringir os movimentos.");
            desiredItems.add(tunica);
        }
        if (!existingItems.containsKey("Manto do Conjurador")) {
            Item manto = new Item();
            manto.setName("Manto do Conjurador");
            manto.setType("armor");
            manto.setValue(70);
            manto.setDescription("Roupas encantadas que melhoram o fluxo de mana.");
            desiredItems.add(manto);
        }
        if (!existingItems.containsKey("Botas de Viagem Simples")) {
            Item botas = new Item();
            botas.setName("Botas de Viagem Simples");
            botas.setType("armor");
            botas.setValue(35);
            botas.setDescription("Botas resistentes que facilitam longas caminhadas.");
            desiredItems.add(botas);
        }
        if (!existingItems.containsKey("Gema Bruta")) {
            Item gema = new Item();
            gema.setName("Gema Bruta");
            gema.setType("treasure");
            gema.setValue(200);
            gema.setDescription("Uma pedra preciosa que pode ser vendida por um bom preço.");
            desiredItems.add(gema);
        }
        if (!existingItems.containsKey("Moeda de Ouro")) {
            Item moeda = new Item();
            moeda.setName("Moeda de Ouro");
            moeda.setType("currency");
            moeda.setValue(1);
            moeda.setDescription("Moeda utilizada para comprar itens.");
            desiredItems.add(moeda);
        }

        if (!desiredItems.isEmpty()) {
            System.out.println("Inserindo " + desiredItems.size() + " novos itens...");
            itemRepository.saveAll(desiredItems);
        } else {
            System.out.println("Tabela 'item' já está atualizada.");
        }
        return itemRepository.findAll().stream()
                .collect(Collectors.toMap(Item::getName, Function.identity()));
    }

    // Método para popular a tabela de Questões
    private void seedQuestions(QuestionRepository questionRepository, Map<String, Content> contentMap) {
        Map<String, Question> existingQuestions = questionRepository.findAll().stream()
                .collect(Collectors.toMap(Question::getQuestionText, Function.identity()));

        List<Question> desiredQuestions = new ArrayList<>();

        // Helper variables for content for readability
        Content verboToBe = contentMap.get("Verbo To Be");
        Content vocabularioBasico = contentMap.get("Vocabulário Básico");
        Content presenteSimples = contentMap.get("Presente Simples");
        Content presentePerfeito = contentMap.get("Presente Perfeito");
        Content expressoesIdiomaticas = contentMap.get("Expressões Idiomáticas");

        // --- INÍCIO DA ADIÇÃO DE PERGUNTAS ---

        // ==============================================
        // 1. Verbo To Be (Total: 50)
        // ==============================================

        // Perguntas existentes
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"She ___ a student.\"", "is", "are", "am", "is", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual o verbo \"to be\" para \"you\"?", "is", "am", "are", "are", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"They ___ playing\".", "is", "am", "are", "are", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"I ___ a boy\".", "am", "is", "are", "am", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"The sun ___ hot\".", "are", "am", "is", "is", "Facil", verboToBe, 1);

        // Novas 45 perguntas
        // Presente - Positivo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ a doctor.\"", "is", "are", "am", "is", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ friends.\"", "is", "are", "am", "are", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"It ___ cold today.\"", "is", "are", "am", "is", "Facil", verboToBe, 1);
        // Presente - Negativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ not tired.\"", "is", "are", "am", "am", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ not from Spain.\"", "is", "are", "am", "is", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ not here.\"", "is", "are", "am", "are", "Facil", verboToBe, 1);
        // Presente - Interrogativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ you happy?\"", "Is", "Are", "Am", "Are", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ he your brother?\"", "Is", "Are", "Am", "Is", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ I late?\"", "Is", "Are", "Am", "Am", "Facil", verboToBe, 1);
        // Passado - Positivo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ sick yesterday.\"", "was", "were", "is", "was", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ at the park.\"", "was", "were", "are", "were", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ a child.\"", "was", "were", "am", "was", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ at home last night.\"", "was", "were", "is", "was", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ happy with the gift.\"", "was", "were", "are", "were", "Medio", verboToBe, 2);
        // Passado - Negativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"It ___ not cold.\"", "was", "were", "is", "was", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"You ___ not listening.\"", "was", "were", "are", "were", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ not a teacher.\"", "was", "were", "is", "was", "Medio", verboToBe, 2);
        // Passado - Interrogativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ she at the party?\"", "Was", "Were", "Is", "Was", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ they friends?\"", "Was", "Were", "Are", "Were", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ it good?\"", "Was", "Were", "Is", "Was", "Medio", verboToBe, 2);
        // Futuro - Positivo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ be there soon.\"", "am", "will", "was", "will", "Dificil", verboToBe, 4);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ be famous one day.\"", "is", "will", "was", "will", "Dificil", verboToBe, 4);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ be happy to see you.\"", "are", "will", "were", "will", "Dificil", verboToBe, 4);
        // Futuro - Negativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ not be late.\"", "is", "will", "was", "will", "Dificil", verboToBe, 4);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Forma contraída de \"will not\":", "willn't", "wan't", "won't", "won't", "Dificil", verboToBe, 4);
        // Formas com Modais
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"You should ___ more careful.\"", "is", "are", "be", "be", "Dificil", verboToBe, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"It can ___ dangerous.\"", "is", "be", "was", "be", "Dificil", verboToBe, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He must ___ joking.\"", "is", "be", "are", "be", "Dificil", verboToBe, 5);
        // Misturadas
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"What ___ your name?\"", "is", "are", "am", "is", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"How old ___ you?\"", "is", "are", "am", "are", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Where ___ she from?\"", "is", "are", "am", "is", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ hungry right now.\"", "is", "are", "am", "am", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"My parents ___ on vacation.\"", "is", "are", "am", "are", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The book ___ on the table.\"", "is", "are", "am", "is", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The cats ___ sleeping.\"", "is", "are", "am", "are", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Last year, she ___ 10.\"", "is", "was", "were", "was", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The shoes ___ new.\"", "is", "are", "am", "are", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Why ___ they late yesterday?\"", "was", "were", "are", "were", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"This ___ not my key.\"", "is", "are", "am", "is", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"These ___ my friends.\"", "is", "are", "am", "are", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ it raining outside?\"", "Is", "Are", "Am", "Is", "Facil", verboToBe, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The movie ___ boring.\"", "was", "were", "is", "was", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ not at the meeting.\"", "was", "were", "are", "were", "Medio", verboToBe, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I think it ___ be okay.\"", "is", "will", "was", "will", "Dificil", verboToBe, 4);

        // ==============================================
        // 2. Vocabulário Básico (Total: 50)
        // ==============================================

        // Perguntas existentes
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"A dog\".", "Um gato", "Um cachorro", "Um pássaro", "Um cachorro", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"car\" é:", "cars", "caros", "carss", "cars", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a tradução de \"hello\"?", "Adeus", "Olá", "Obrigado", "Olá", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O antônimo de \"big\" é:", "small", "tall", "short", "small", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"red\"?", "Azul", "Vermelho", "Verde", "Vermelho", "Facil", vocabularioBasico, 1);

        // Novas 45 perguntas
        // Cores
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"blue\"?", "Azul", "Amarelo", "Preto", "Azul", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"green\"?", "Cinza", "Verde", "Branco", "Verde", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"yellow\"?", "Laranja", "Roxo", "Amarelo", "Amarelo", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"black\"?", "Preto", "Branco", "Marrom", "Preto", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"white\"?", "Rosa", "Branco", "Dourado", "Branco", "Facil", vocabularioBasico, 1);
        // Números
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Como se escreve \"3\" em inglês?", "Tree", "Three", "Thi", "Three", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Como se escreve \"10\" em inglês?", "Ten", "Tin", "Tan", "Ten", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Como se escreve \"5\" em inglês?", "Five", "Fife", "Vife", "Five", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual número vem depois de \"six\"?", "Seven", "Eight", "Five", "Seven", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual número é \"one\"?", "Dois", "Um", "Zero", "Um", "Facil", vocabularioBasico, 1);
        // Animais
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Cat\".", "Gato", "Rato", "Cavalo", "Gato", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Bird\".", "Peixe", "Pássaro", "Ovelha", "Pássaro", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Fish\".", "Peixe", "Sapo", "Pato", "Peixe", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Mouse\".", "Boi", "Rato", "Leão", "Rato", "Facil", vocabularioBasico, 1);
        // Comida
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Apple\".", "Banana", "Laranja", "Maçã", "Maçã", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Water\".", "Suco", "Água", "Leite", "Água", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Bread\".", "Pão", "Queijo", "Arroz", "Pão", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Milk\".", "Leite", "Café", "Chá", "Leite", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Rice\".", "Feijão", "Arroz", "Carne", "Arroz", "Medio", vocabularioBasico, 2);
        // Família
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Mother\".", "Pai", "Irmão", "Mãe", "Mãe", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Father\".", "Pai", "Tio", "Avô", "Pai", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Sister\".", "Prima", "Irmã", "Tia", "Irmã", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Brother\".", "Irmão", "Filho", "Sobrinho", "Irmão", "Facil", vocabularioBasico, 1);
        // Verbos Comuns
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To eat\".", "Beber", "Comer", "Dormir", "Comer", "Medio", vocabularioBasico, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To sleep\".", "Dormir", "Acordar", "Correr", "Dormir", "Medio", vocabularioBasico, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To walk\".", "Pular", "Sentar", "Andar", "Andar", "Medio", vocabularioBasico, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To run\".", "Correr", "Falar", "Ouvir", "Correr", "Medio", vocabularioBasico, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To read\".", "Ler", "Escrever", "Desenhar", "Ler", "Medio", vocabularioBasico, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To write\".", "Apagar", "Escrever", "Digitar", "Escrever", "Medio", vocabularioBasico, 2);
        // Adjetivos Comuns
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Happy\".", "Triste", "Feliz", "Cansado", "Feliz", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Sad\".", "Feliz", "Zangado", "Triste", "Triste", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Tall\".", "Baixo", "Alto", "Gordo", "Alto", "Medio", vocabularioBasico, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Short\".", "Longo", "Pequeno", "Baixo", "Baixo", "Medio", vocabularioBasico, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Hot\".", "Quente", "Frio", "Morno", "Quente", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Cold\".", "Frio", "Quente", "Gelado", "Frio", "Facil", vocabularioBasico, 1);
        // Plurais Irregulares
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"man\" é:", "mans", "men", "manes", "men", "Dificil", vocabularioBasico, 4);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"woman\" é:", "womans", "womenses", "women", "women", "Dificil", vocabularioBasico, 4);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"child\" é:", "childs", "children", "childes", "children", "Dificil", vocabularioBasico, 4);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"tooth\" é:", "tooths", "teeth", "teethes", "teeth", "Dificil", vocabularioBasico, 4);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"foot\" é:", "foots", "feets", "feet", "feet", "Dificil", vocabularioBasico, 4);
        // Outros
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Good morning\".", "Boa noite", "Boa tarde", "Bom dia", "Bom dia", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Good night\".", "Boa noite", "Boa tarde", "Bom dia", "Boa noite", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Thank you\".", "Por favor", "De nada", "Obrigado", "Obrigado", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Please\".", "Com licença", "Por favor", "Desculpe", "Por favor", "Facil", vocabularioBasico, 1);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Goodbye\".", "Olá", "Adeus", "Até logo", "Adeus", "Facil", vocabularioBasico, 1);

        // ==============================================
        // 3. Presente Simples (Total: 50)
        // ==============================================

        // Perguntas existentes
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"I like to read\".", "Eu gosto de comer", "Eu gosto de correr", "Eu gosto de ler", "Eu gosto de ler", "Medio", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"She ___ to school every day\".", "go", "goes", "going", "goes", "Medio", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"He works at a bank\".", "Ele trabalhou no banco", "Ele trabalha em um banco", "Ele vai trabalhar no banco", "Ele trabalha em um banco", "Medio", presenteSimples, 2);

        // Novas 47 perguntas
        // Positivo (I/You/We/They)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ soccer on weekends.\"", "play", "plays", "playing", "play", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ pizza.\"", "love", "loves", "loving", "love", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"You ___ English very well.\"", "speak", "speaks", "speaking", "speak", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ in a small apartment.\"", "live", "lives", "living", "live", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"My parents ___ in Canada.\"", "live", "lives", "living", "live", "Facil", presenteSimples, 2);
        // Positivo (He/She/It)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ TV in the evening.\"", "watch", "watches", "watching", "watches", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The cat ___ on the sofa.\"", "sleep", "sleeps", "sleeping", "sleeps", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"My brother ___ fast.\"", "run", "runs", "running", "runs", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"It ___ a lot in winter.\"", "rain", "rains", "raining", "rains", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ her homework after school.\"", "do", "does", "doing", "does", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ to fix cars.\"", "try", "trys", "tries", "tries", "Medio", presenteSimples, 3);
        // Negativo (don't)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ like coffee.\"", "don't", "doesn't", "am not", "don't", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ go to the beach often.\"", "don't", "doesn't", "aren't", "don't", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ understand the problem.\"", "don't", "doesn't", "aren't", "don't", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"You ___ need a ticket.\"", "don't", "doesn't", "aren't", "don't", "Facil", presenteSimples, 2);
        // Negativo (doesn't)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ live here anymore.\"", "don't", "doesn't", "isn't", "doesn't", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ have a car.\"", "don't", "doesn't", "isn't", "doesn't", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"It ___ work properly.\"", "don't", "doesn't", "isn't", "doesn't", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"My sister ___ eat meat.\"", "don't", "doesn't", "isn't", "doesn't", "Medio", presenteSimples, 3);
        // Interrogativo (Do)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ you speak Spanish?\"", "Do", "Does", "Are", "Do", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ they live nearby?\"", "Do", "Does", "Are", "Do", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Where ___ you work?\"", "do", "does", "are", "do", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"What ___ they want?\"", "do", "does", "are", "do", "Facil", presenteSimples, 2);
        // Interrogativo (Does)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ she like music?\"", "Do", "Does", "Is", "Does", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ he play the guitar?\"", "Do", "Does", "Is", "Does", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"When ___ the train arrive?\"", "do", "does", "is", "does", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Why ___ it happen?\"", "do", "does", "is", "does", "Medio", presenteSimples, 3);
        // Advérbios de Frequência
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a posição correta: \"I ___ go to the gym.\"", "always", "go always", "I always go", "I always go", "Dificil", presenteSimples, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a posição correta: \"He is ___ late.\"", "never", "never is", "is never", "is never", "Dificil", presenteSimples, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ visits her grandma.\"", "sometimes", "sometimes visits", "visits sometimes", "sometimes visits", "Dificil", presenteSimples, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ eat fast food.\"", "rarely", "eat rarely", "rarely eat", "rarely eat", "Dificil", presenteSimples, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ watch movies.\"", "often", "often watch", "watch often", "often watch", "Dificil", presenteSimples, 5);
        // Misturadas
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"The sun rises in the east.\"", "O sol se põe no leste", "O sol nasce no leste", "O sol está no leste", "O sol nasce no leste", "Dificil", presenteSimples, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Water ___ at 100 degrees Celsius.\"", "boil", "boils", "boiling", "boils", "Dificil", presenteSimples, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"I don't think so.\"", "Eu não acho", "Eu não penso", "Eu acho que não", "Eu acho que não", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Do you know him?\"", "Você sabe dele?", "Você conhece ele?", "Você gosta dele?", "Você conhece ele?", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"It doesn't matter.\"", "Isso não importa", "Isso não é matéria", "Não tem problema", "Isso não importa", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"What time ___ the bank open?\"", "do", "does", "is", "does", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Cats ___ fish.\"", "like", "likes", "liking", "like", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"A cat ___ fish.\"", "like", "likes", "liking", "likes", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"I work from home.\"", "Eu ando para casa", "Eu trabalho de casa", "Eu vou para casa", "Eu trabalho de casa", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ his teeth every morning.\"", "brush", "brushes", "brushing", "brushes", "Medio", presenteSimples, 3);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ in a big house.\"", "live", "lives", "living", "live", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ not understand.\"", "do", "does", "am", "do", "Facil", presenteSimples, 2);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ not play tennis.\"", "do", "does", "is", "does", "Medio", presenteSimples, 3);

        // ==============================================
        // 4. Presente Perfeito (Total: 50)
        // ==============================================

        // Perguntas existentes
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a forma correta do Present Perfect de \"to be\"?", "was/were", "have been/has been", "had been", "have been/has been", "Dificil", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"I have ___ this movie twice\".", "see", "saw", "seen", "seen", "Dificil", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a forma correta do Present Perfect de \"to go\"?", "went", "have gone", "have goed", "have gone", "Dificil", presentePerfeito, 5);

        // Novas 47 perguntas
        // Positivo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She has ___ her keys.\"", "lose", "lost", "losing", "lost", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They have ___ a new car.\"", "buy", "bought", "buyed", "bought", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He has ___ to Brazil.\"", "been", "went", "gone", "been", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I have ___ my homework.\"", "do", "did", "done", "done", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We have ___ lunch.\"", "eat", "ate", "eaten", "eaten", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She has ___ a book.\"", "write", "wrote", "written", "written", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They have ___ the window.\"", "break", "broke", "broken", "broken", "Medio", presentePerfeito, 5);
        // Negativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I haven't ___ him today.\"", "see", "saw", "seen", "seen", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He hasn't ___ yet.\"", "arrive", "arrived", "arriving", "arrived", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We haven't ___ to them.\"", "speak", "spoke", "spoken", "spoken", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She hasn't ___ her breakfast.\"", "finish", "finished", "finishing", "finished", "Medio", presentePerfeito, 5);
        // Interrogativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ you ever ___ sushi?\"", "Have / eat", "Have / eaten", "Did / eat", "Have / eaten", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ he ___ the email?\"", "Has / send", "Has / sent", "Did / send", "Has / sent", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Where ___ they gone?\"", "have", "has", "did", "have", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"What ___ she done?\"", "have", "has", "did", "has", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ you finished the report?\"", "Have", "Has", "Did", "Have", "Medio", presentePerfeito, 5);
        // For vs Since
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I have lived here ___ 10 years.\"", "for", "since", "ago", "for", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She has been a teacher ___ 2010.\"", "for", "since", "ago", "since", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We have known each other ___ a long time.\"", "for", "since", "ago", "for", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He hasn't eaten ___ this morning.\"", "for", "since", "ago", "since", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They have been married ___ 3 months.\"", "for", "since", "ago", "for", "Dificil", presentePerfeito, 6);
        // Yet vs Already
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Have you finished ___?\"", "already", "yet", "since", "yet", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I have ___ seen that movie.\"", "already", "yet", "for", "already", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The train hasn't arrived ___.\"", "already", "yet", "ago", "yet", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She has ___ left.\"", "already", "yet", "for", "already", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He's only 20, but he has ___ started his own company.\"", "already", "yet", "since", "already", "Dificil", presentePerfeito, 6);
        // Past Simple vs Present Perfect
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual está correto para uma ação terminada no passado?", "I have seen him yesterday.", "I saw him yesterday.", "I seen him yesterday.", "I saw him yesterday.", "Dificil", presentePerfeito, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual está correto para uma experiência de vida?", "Did you ever eat sushi?", "Have you ever eaten sushi?", "Have you ever ate sushi?", "Have you ever eaten sushi?", "Dificil", presentePerfeito, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ to Paris last year.\"", "has gone", "went", "has been", "went", "Dificil", presentePerfeito, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ my keys. I can't find them.\"", "lost", "have lost", "did lose", "have lost", "Dificil", presentePerfeito, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Einstein ___ the theory of relativity.\"", "has developed", "developed", "develop", "developed", "Dificil", presentePerfeito, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ four emails so far this morning.\"", "wrote", "have written", "write", "have written", "Dificil", presentePerfeito, 7);
        // Misturadas
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to break\":", "broke", "broken", "breaked", "broken", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to choose\":", "chose", "choosed", "chosen", "chosen", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to speak\":", "spoke", "spoken", "speaked", "spoken", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to take\":", "toke", "taked", "taken", "taken", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to drive\":", "drove", "driven", "drived", "driven", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to find\":", "found", "finded", "faund", "found", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to give\":", "gave", "gived", "given", "given", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to know\":", "knew", "known", "knowed", "known", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to swim\":", "swam", "swum", "swimmed", "swum", "Medio", presentePerfeito, 5);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He hasn't worked here ___ long.\"", "for", "since", "ago", "for", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I have known him ___ we were children.\"", "for", "since", "ago", "since", "Dificil", presentePerfeito, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Have you done your homework ___?\"", "already", "yet", "since", "yet", "Dificil", presentePerfeito, 6);

        // ==============================================
        // 5. Expressões Idiomáticas (Total: 50)
        // ==============================================

        // Perguntas existentes
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"break a leg\"?", "Quebre a perna", "Boa sorte", "Desastre", "Boa sorte", "Medio", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"it's raining cats and dogs\"?", "Está chovendo muito", "Está chovendo gatos e cachorros", "A tempestade é forte", "Está chovendo muito", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"break the bank\"?", "Quebrar o banco", "Gastar muito dinheiro", "Assaltar um banco", "Gastar muito dinheiro", "Dificil", expressoesIdiomaticas, 7);

        // Novas 47 perguntas
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"a piece of cake\"?", "Um pedaço de bolo", "Algo muito difícil", "Algo muito fácil", "Algo muito fácil", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"once in a blue moon\"?", "Toda noite de lua azul", "Algo muito raro", "Algo muito comum", "Algo muito raro", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"under the weather\"?", "Abaixo do tempo", "Estar indisposto/doente", "Estar feliz", "Estar indisposto/doente", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to cost an arm and a leg\"?", "Custar um braço e uma perna", "Ser muito barato", "Ser muito caro", "Ser muito caro", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to hit the nail on the head\"?", "Acertar o prego na cabeça", "Estar exatamente correto", "Estar completamente errado", "Estar exatamente correto", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to bite the bullet\"?", "Morder a bala", "Enfrentar algo difícil com coragem", "Desistir", "Enfrentar algo difícil com coragem", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to spill the beans\"?", "Derrubar os feijões", "Contar um segredo", "Fazer o jantar", "Contar um segredo", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to bark up the wrong tree\"?", "Latir para a árvore errada", "Estar enganado / procurando no lugar errado", "Estar no caminho certo", "Estar enganado / procurando no lugar errado", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"the ball is in your court\"?", "A bola está na sua quadra", "É sua vez de tomar uma atitude", "Você perdeu o jogo", "É sua vez de tomar uma atitude", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to let the cat out of the bag\"?", "Deixar o gato sair da bolsa", "Revelar um segredo acidentalmente", "Adotar um gato", "Revelar um segredo acidentalmente", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to get a taste of your own medicine\"?", "Provar o seu próprio remédio", "Receber o mesmo tratamento ruim que deu aos outros", "Ser um farmacêutico", "Receber o mesmo tratamento ruim que deu aos outros", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to jump on the bandwagon\"?", "Pular na carroça da banda", "Seguir uma moda ou tendência", "Ir a um desfile", "Seguir uma moda ou tendência", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"the last straw\"?", "O último canudo", "O limite da paciência / o último problema", "A bebida final", "O limite da paciência / o último problema", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to beat around the bush\"?", "Bater ao redor do arbusto", "Evitar falar sobre o assunto principal / enrolar", "Ser direto", "Evitar falar sobre o assunto principal / enrolar", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to bite off more than you can chew\"?", "Morder mais do que consegue mastigar", "Tentar fazer mais do que se é capaz", "Estar com muita fome", "Tentar fazer mais do que se é capaz", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to make matters worse\"?", "Fazer a matéria piorar", "Piorar uma situação que já está ruim", "Melhorar uma situação", "Piorar uma situação que já está ruim", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to see eye to eye\"?", "Ver olho no olho", "Concordar completamente com alguém", "Ter boa visão", "Concordar completamente com alguém", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"when pigs fly\"?", "Quando porcos voarem", "Algo que nunca vai acontecer", "Um milagre", "Algo que nunca vai acontecer", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"speak of the devil\"?", "Falar do diabo", "Falar de alguém que aparece em seguida", "Estar com problemas", "Falar de alguém que aparece em seguida", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"the elephant in the room\"?", "O elefante na sala", "Um problema óbvio que ninguém quer discutir", "Um animal de estimação grande", "Um problema óbvio que ninguém quer discutir", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to cut corners\"?", "Cortar cantos", "Fazer algo de forma mais fácil/barata, sacrificando a qualidade", "Ser um bom motorista", "Fazer algo de forma mais fácil/barata, sacrificando a qualidade", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to add insult to injury\"?", "Adicionar insulto à injúria", "Piorar uma situação ruim com mais uma coisa ruim", "Ser rude com alguém machucado", "Piorar uma situação ruim com mais uma coisa ruim", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"don't cry over spilled milk\"?", "Não chore sobre o leite derramado", "Não adianta lamentar por algo que já aconteceu", "Tenha cuidado ao fazer compras", "Não adianta lamentar por algo que já aconteceu", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"every cloud has a silver lining\"?", "Toda nuvem tem um forro de prata", "Toda situação ruim tem um lado positivo", "O tempo vai melhorar", "Toda situação ruim tem um lado positivo", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to get cold feet\"?", "Ficar com pés frios", "Ficar nervoso e desistir de algo", "Estar com frio", "Ficar nervoso e desistir de algo", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to get out of hand\"?", "Sair da mão", "Ficar fora de controle", "Lavar as mãos", "Ficar fora de controle", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to hit the books\"?", "Bater nos livros", "Estudar com afinco", "Organizar a estante", "Estudar com afinco", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to hit the sack\" / \"to hit the hay\"?", "Bater no saco / Bater no feno", "Ir dormir", "Trabalhar na fazenda", "Ir dormir", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to keep an eye on someone/something\"?", "Manter um olho em alguém/algo", "Vigiar ou cuidar de alguém/algo", "Ser vesgo", "Vigiar ou cuidar de alguém/algo", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to be on the ball\"?", "Estar na bola", "Estar alerta, rápido e eficiente", "Jogar futebol", "Estar alerta, rápido e eficiente", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"(to be) on thin ice\"?", "Estar em gelo fino", "Estar em uma situação arriscada", "Patinar no gelo", "Estar em uma situação arriscada", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to ring a bell\"?", "Tocar um sino", "Soar familiar / lembrar vagamente", "Chamar atenção", "Soar familiar / lembrar vagamente", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to sit on the fence\"?", "Sentar na cerca", "Estar indeciso / não querer tomar partido", "Descansar", "Estar indeciso / não querer tomar partido", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"straight from the horse's mouth\"?", "Direto da boca do cavalo", "Da fonte original ou mais confiável", "Uma informação falsa", "Da fonte original ou mais confiável", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"the best of both worlds\"?", "O melhor de ambos os mundos", "Ter as vantagens de duas situações diferentes", "Estar em dois lugares ao mesmo tempo", "Ter as vantagens de duas situações diferentes", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to twist someone's arm\"?", "Torcer o braço de alguém", "Convencer alguém com insistência / persuadir", "Machucar alguém", "Convencer alguém com insistência / persuadir", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to be up in the air\"?", "Estar no ar", "Estar incerto / ainda não decidido", "Estar voando", "Estar incerto / ainda não decidido", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to call it a day\"?", "Chamar isso de um dia", "Decidir parar de trabalhar por hoje", "Agendar um dia", "Decidir parar de trabalhar por hoje", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to be glad to see the back of someone\"?", "Feliz em ver as costas de alguém", "Ficar feliz quando alguém vai embora", "Sentir falta de alguém", "Ficar feliz quando alguém vai embora", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"against the clock\"?", "Contra o relógio", "Fazer algo muito rápido / com prazo apertado", "Trabalhar à noite", "Fazer algo muito rápido / com prazo apertado", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"(to have) a chip on your shoulder\"?", "Ter um pedaço no ombro", "Estar ressentido / guardar rancor por algo", "Estar com fome", "Estar ressentido / guardar rancor por algo", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"a blessing in disguise\"?", "Uma bênção disfarçada", "Algo que parece ruim no início, mas se revela bom", "Um presente ruim", "Algo que parece ruim no início, mas se revela bom", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to burn the midnight oil\"?", "Queimar o óleo da meia-noite", "Trabalhar ou estudar até tarde da noite", "Causar um incêndio", "Trabalhar ou estudar até tarde da noite", "Dificil", expressoesIdiomaticas, 7);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"don't put all your eggs in one basket\"?", "Não coloque todos os ovos em uma cesta", "Não concentre todos os seus recursos ou esperanças em uma só coisa", "Tenha cuidado ao fazer compras", "Não concentre todos os seus recursos ou esperanças em uma só coisa", "Medio", expressoesIdiomaticas, 6);
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to face the music\"?", "Encarar a música", "Aceitar as consequências de suas ações", "Ir a um concerto", "Aceitar as consequências de suas ações", "Medio", expressoesIdiomaticas, 6);

        // --- FIM DA ADIÇÃO DE PERGUNTAS ---

        if (!desiredQuestions.isEmpty()) {
            System.out.println("Inserindo " + desiredQuestions.size() + " novas questões...");
            questionRepository.saveAll(desiredQuestions);
        } else {
            System.out.println("Tabela 'question' já está atualizada.");
        }
    }

    // Método para popular a Loja e seus itens
    private void seedLoja(StoreRepository storeRepository, ItemLojaRepository itemLojaRepository, Map<String, Item> itemMap) {
        // Garante que exista pelo menos uma loja
        Store store;
        if (storeRepository.count() == 0) {
            System.out.println("Criando a loja principal...");
            store = storeRepository.save(new Store());
        } else {
            store = storeRepository.findAll().get(0); // Pega a primeira loja que encontrar
        }

        // Busca os itens que já estão na loja para não duplicar
        Map<Integer, ItemStore> existingShopItems = itemLojaRepository.findByStoreId(store.getId()).stream()
                .collect(Collectors.toMap(ItemStore::getItemId, Function.identity()));

        List<ItemStore> newShopItems = new ArrayList<>();
        for (Item item : itemMap.values()) {
            // Pula itens sem id (não persistidos)
            if (item.getId() == null) {
                System.err.println("Aviso: item '" + item.getName() + "' possui id nulo e será ignorado no seed da loja.");
                continue;
            }

            // Se o item NÃO está na lista de exceções E NÃO está na loja ainda...
            if (!item.getName().equals("Gema Bruta") && !item.getName().equals("Moeda de Ouro") && !existingShopItems.containsKey(item.getId())) {
                ItemStore itemStore = new ItemStore();
                // Preencher as chaves compostas explicitamente para evitar ids nulos
                itemStore.setStoreId(store.getId());
                itemStore.setItemId(item.getId());
                itemStore.setStore(store);
                itemStore.setItem(item);
                itemStore.setPrice((int) Math.round((item.getValue() != null ? item.getValue() : 0) * 1.20));
                itemStore.setQuantity(10);
                newShopItems.add(itemStore);
            }
        }

        if (!newShopItems.isEmpty()) {
            System.out.println("Adicionando " + newShopItems.size() + " novos itens ao estoque da loja...");
            itemLojaRepository.saveAll(newShopItems);
        } else {
            System.out.println("Estoque da loja já está atualizado.");
        }
    }

    // Método helper para criar questões, evitando duplicatas
    private void addQuestionIfNotExists(List<Question> desiredQuestions, Map<String, Question> existingQuestions, String texto, String opcaoA, String opcaoB, String opcaoC,
                                        String resposta, String dificuldade, Content content, int levelMinimo) {
        if (content == null) {
            System.err.println("Atenção: Conteúdo nulo para a pergunta: '" + texto + "'. Pergunta não será adicionada.");
            return;
        }
        if (!existingQuestions.containsKey(texto)) {
            Question q = new Question();
            q.setQuestionText(texto);
            q.setOptionA(opcaoA);
            q.setOptionB(opcaoB);
            q.setOptionC(opcaoC);
            q.setCorrectAnswer(resposta);
            q.setDifficulty(dificuldade);
            q.setQuestionContent(content.getContentName().toLowerCase().replace(" ", "_"));
            q.setContent(content);
            q.setMinLevel(levelMinimo);
            desiredQuestions.add(q);
        }
    }
}

