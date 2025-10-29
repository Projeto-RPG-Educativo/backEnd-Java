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
            ItemLojaRepository itemLojaRepository,
            NPCRepository npcRepository,
            DialogueRepository dialogueRepository
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
            seedNpc(npcRepository);
            seedDialogue(dialogueRepository, npcRepository);

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
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"She ___ a student.\"", "is", "are", "am", "is", "Facil", verboToBe, 1, "Terceira pessoa do singular no presente.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual o verbo \"to be\" para \"you\"?", "is", "am", "are", "are", "Facil", verboToBe, 1, "Forma usada para 'você' e plurais.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"They ___ playing\".", "is", "am", "are", "are", "Facil", verboToBe, 1, "Use a forma plural no presente.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"I ___ a boy\".", "am", "is", "are", "am", "Facil", verboToBe, 1, "Forma única usada com 'Eu'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"The sun ___ hot\".", "are", "am", "is", "is", "Facil", verboToBe, 1, "O sol é uma 'coisa' (it).");

        // Novas 45 perguntas
        // Presente - Positivo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ a doctor.\"", "is", "are", "am", "is", "Facil", verboToBe, 1, "Terceira pessoa do singular (masculino).");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ friends.\"", "is", "are", "am", "are", "Facil", verboToBe, 1, "Forma plural para 'nós'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"It ___ cold today.\"", "is", "are", "am", "is", "Facil", verboToBe, 1, "Usado para 'coisas' ou clima.");
        // Presente - Negativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ not tired.\"", "is", "are", "am", "am", "Facil", verboToBe, 1, "Forma negativa para 'Eu'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ not from Spain.\"", "is", "are", "am", "is", "Facil", verboToBe, 1, "Forma negativa para 'Ela'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ not here.\"", "is", "are", "am", "are", "Facil", verboToBe, 1, "Forma negativa plural.");
        // Presente - Interrogativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ you happy?\"", "Is", "Are", "Am", "Are", "Facil", verboToBe, 1, "Inverta a ordem para perguntar com 'you'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ he your brother?\"", "Is", "Are", "Am", "Is", "Facil", verboToBe, 1, "Inverta a ordem para perguntar com 'he'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ I late?\"", "Is", "Are", "Am", "Am", "Facil", verboToBe, 1, "Inverta a ordem para perguntar com 'I'.");
        // Passado - Positivo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ sick yesterday.\"", "was", "were", "is", "was", "Medio", verboToBe, 2, "Passado singular para 'Ela'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ at the park.\"", "was", "were", "are", "were", "Medio", verboToBe, 2, "Passado plural para 'Nós'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ a child.\"", "was", "were", "am", "was", "Medio", verboToBe, 2, "Passado singular para 'Eu'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ at home last night.\"", "was", "were", "is", "was", "Medio", verboToBe, 2, "Passado singular para 'Ele'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ happy with the gift.\"", "was", "were", "are", "were", "Medio", verboToBe, 2, "Passado plural para 'Eles'.");
        // Passado - Negativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"It ___ not cold.\"", "was", "were", "is", "was", "Medio", verboToBe, 2, "Passado singular negativo para 'It'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"You ___ not listening.\"", "was", "were", "are", "were", "Medio", verboToBe, 2, "Passado plural (ou singular formal) negativo para 'You'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ not a teacher.\"", "was", "were", "is", "was", "Medio", verboToBe, 2, "Passado singular negativo para 'He'.");
        // Passado - Interrogativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ she at the party?\"", "Was", "Were", "Is", "Was", "Medio", verboToBe, 2, "Inverta a ordem no passado para 'She'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ they friends?\"", "Was", "Were", "Are", "Were", "Medio", verboToBe, 2, "Inverta a ordem no passado para 'They'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ it good?\"", "Was", "Were", "Is", "Was", "Medio", verboToBe, 2, "Inverta a ordem no passado para 'It'.");
        // Futuro - Positivo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ be there soon.\"", "am", "will", "was", "will", "Dificil", verboToBe, 4, "Verbo auxiliar para indicar futuro simples.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ be famous one day.\"", "is", "will", "was", "will", "Dificil", verboToBe, 4, "Verbo auxiliar para indicar futuro simples.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ be happy to see you.\"", "are", "will", "were", "will", "Dificil", verboToBe, 4, "Verbo auxiliar para indicar futuro simples.");
        // Futuro - Negativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ not be late.\"", "is", "will", "was", "will", "Dificil", verboToBe, 4, "Futuro simples, a negação vem depois.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Forma contraída de \"will not\":", "willn't", "wan't", "won't", "won't", "Dificil", verboToBe, 4, "Contração comum para futuro negativo.");
        // Formas com Modais
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"You should ___ more careful.\"", "is", "are", "be", "be", "Dificil", verboToBe, 5, "Após verbos modais como 'should', 'can', 'must', use a forma base.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"It can ___ dangerous.\"", "is", "be", "was", "be", "Dificil", verboToBe, 5, "Após 'can', use a forma base do verbo.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He must ___ joking.\"", "is", "be", "are", "be", "Dificil", verboToBe, 5, "Após 'must', use a forma base do verbo.");
        // Misturadas
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"What ___ your name?\"", "is", "are", "am", "is", "Facil", verboToBe, 1, "Pergunta comum, singular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"How old ___ you?\"", "is", "are", "am", "are", "Facil", verboToBe, 1, "Pergunta comum, use a forma para 'you'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Where ___ she from?\"", "is", "are", "am", "is", "Facil", verboToBe, 1, "Pergunta sobre origem, terceira pessoa singular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ hungry right now.\"", "is", "are", "am", "am", "Facil", verboToBe, 1, "Estado atual, primeira pessoa singular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"My parents ___ on vacation.\"", "is", "are", "am", "are", "Facil", verboToBe, 1, "Pais = plural ('they').");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The book ___ on the table.\"", "is", "are", "am", "is", "Facil", verboToBe, 1, "O livro = singular ('it').");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The cats ___ sleeping.\"", "is", "are", "am", "are", "Facil", verboToBe, 1, "Gatos = plural ('they').");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Last year, she ___ 10.\"", "is", "was", "were", "was", "Medio", verboToBe, 2, "Ação no passado, singular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The shoes ___ new.\"", "is", "are", "am", "are", "Facil", verboToBe, 1, "Sapatos = plural ('they').");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Why ___ they late yesterday?\"", "was", "were", "are", "were", "Medio", verboToBe, 2, "Pergunta no passado, plural.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"This ___ not my key.\"", "is", "are", "am", "is", "Facil", verboToBe, 1, "'This' = singular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"These ___ my friends.\"", "is", "are", "am", "are", "Facil", verboToBe, 1, "'These' = plural.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ it raining outside?\"", "Is", "Are", "Am", "Is", "Facil", verboToBe, 1, "Pergunta sobre clima ('it').");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The movie ___ boring.\"", "was", "were", "is", "was", "Medio", verboToBe, 2, "Opinião sobre algo no passado, singular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ not at the meeting.\"", "was", "were", "are", "were", "Medio", verboToBe, 2, "Negação no passado, plural.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I think it ___ be okay.\"", "is", "will", "was", "will", "Dificil", verboToBe, 4, "Expressa uma previsão futura.");

        // ==============================================
        // 2. Vocabulário Básico (Total: 50)
        // ==============================================

        // Perguntas existentes
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"A dog\".", "Um gato", "Um cachorro", "Um pássaro", "Um cachorro", "Facil", vocabularioBasico, 1, "Melhor amigo do homem.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"car\" é:", "cars", "caros", "carss", "cars", "Facil", vocabularioBasico, 1, "Regra geral do plural: adicionar 's'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a tradução de \"hello\"?", "Adeus", "Olá", "Obrigado", "Olá", "Facil", vocabularioBasico, 1, "Cumprimento comum ao encontrar alguém.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O antônimo de \"big\" é:", "small", "tall", "short", "small", "Facil", vocabularioBasico, 1, "O oposto de grande.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"red\"?", "Azul", "Vermelho", "Verde", "Vermelho", "Facil", vocabularioBasico, 1, "Cor do sangue, maçãs...");

        // Novas 45 perguntas
        // Cores
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"blue\"?", "Azul", "Amarelo", "Preto", "Azul", "Facil", vocabularioBasico, 1, "Cor do céu em dia claro.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"green\"?", "Cinza", "Verde", "Branco", "Verde", "Facil", vocabularioBasico, 1, "Cor da grama.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"yellow\"?", "Laranja", "Roxo", "Amarelo", "Amarelo", "Facil", vocabularioBasico, 1, "Cor do sol.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"black\"?", "Preto", "Branco", "Marrom", "Preto", "Facil", vocabularioBasico, 1, "Cor da noite escura.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a cor \"white\"?", "Rosa", "Branco", "Dourado", "Branco", "Facil", vocabularioBasico, 1, "Cor da neve.");
        // Números
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Como se escreve \"3\" em inglês?", "Tree", "Three", "Thi", "Three", "Facil", vocabularioBasico, 1, "Começa com 'Thr'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Como se escreve \"10\" em inglês?", "Ten", "Tin", "Tan", "Ten", "Facil", vocabularioBasico, 1, "Número de dedos nas mãos.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Como se escreve \"5\" em inglês?", "Five", "Fife", "Vife", "Five", "Facil", vocabularioBasico, 1, "Número de dedos em uma mão.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual número vem depois de \"six\"?", "Seven", "Eight", "Five", "Seven", "Facil", vocabularioBasico, 1, "É o número 7.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual número é \"one\"?", "Dois", "Um", "Zero", "Um", "Facil", vocabularioBasico, 1, "O primeiro número.");
        // Animais
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Cat\".", "Gato", "Rato", "Cavalo", "Gato", "Facil", vocabularioBasico, 1, "Animal que mia.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Bird\".", "Peixe", "Pássaro", "Ovelha", "Pássaro", "Facil", vocabularioBasico, 1, "Animal que voa.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Fish\".", "Peixe", "Sapo", "Pato", "Peixe", "Facil", vocabularioBasico, 1, "Animal que nada.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Mouse\".", "Boi", "Rato", "Leão", "Rato", "Facil", vocabularioBasico, 1, "Animal pequeno que gosta de queijo.");
        // Comida
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Apple\".", "Banana", "Laranja", "Maçã", "Maçã", "Facil", vocabularioBasico, 1, "Fruta vermelha ou verde, comum em tortas.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Water\".", "Suco", "Água", "Leite", "Água", "Facil", vocabularioBasico, 1, "Líquido essencial para a vida.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Bread\".", "Pão", "Queijo", "Arroz", "Pão", "Facil", vocabularioBasico, 1, "Feito de farinha, água e fermento.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Milk\".", "Leite", "Café", "Chá", "Leite", "Facil", vocabularioBasico, 1, "Bebida branca produzida por vacas.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Rice\".", "Feijão", "Arroz", "Carne", "Arroz", "Medio", vocabularioBasico, 2, "Grão branco comum como acompanhamento.");
        // Família
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Mother\".", "Pai", "Irmão", "Mãe", "Mãe", "Facil", vocabularioBasico, 1, "Figura feminina parental.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Father\".", "Pai", "Tio", "Avô", "Pai", "Facil", vocabularioBasico, 1, "Figura masculina parental.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Sister\".", "Prima", "Irmã", "Tia", "Irmã", "Facil", vocabularioBasico, 1, "Filha dos mesmos pais.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Brother\".", "Irmão", "Filho", "Sobrinho", "Irmão", "Facil", vocabularioBasico, 1, "Filho dos mesmos pais.");
        // Verbos Comuns
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To eat\".", "Beber", "Comer", "Dormir", "Comer", "Medio", vocabularioBasico, 2, "Ação de ingerir alimentos.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To sleep\".", "Dormir", "Acordar", "Correr", "Dormir", "Medio", vocabularioBasico, 2, "Ação de descansar à noite.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To walk\".", "Pular", "Sentar", "Andar", "Andar", "Medio", vocabularioBasico, 2, "Mover-se a pé.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To run\".", "Correr", "Falar", "Ouvir", "Correr", "Medio", vocabularioBasico, 2, "Mover-se rapidamente a pé.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To read\".", "Ler", "Escrever", "Desenhar", "Ler", "Medio", vocabularioBasico, 2, "Ação de decifrar palavras escritas.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"To write\".", "Apagar", "Escrever", "Digitar", "Escrever", "Medio", vocabularioBasico, 2, "Ação de registrar palavras no papel.");
        // Adjetivos Comuns
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Happy\".", "Triste", "Feliz", "Cansado", "Feliz", "Facil", vocabularioBasico, 1, "Sentimento de alegria.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Sad\".", "Feliz", "Zangado", "Triste", "Triste", "Facil", vocabularioBasico, 1, "Sentimento de infelicidade.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Tall\".", "Baixo", "Alto", "Gordo", "Alto", "Medio", vocabularioBasico, 2, "Grande estatura vertical.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Short\".", "Longo", "Pequeno", "Baixo", "Baixo", "Medio", vocabularioBasico, 2, "Pequena estatura vertical ou comprimento.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Hot\".", "Quente", "Frio", "Morno", "Quente", "Facil", vocabularioBasico, 1, "Alta temperatura.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Cold\".", "Frio", "Quente", "Gelado", "Frio", "Facil", vocabularioBasico, 1, "Baixa temperatura.");
        // Plurais Irregulares
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"man\" é:", "mans", "men", "manes", "men", "Dificil", vocabularioBasico, 4, "Troca a vogal 'a' por 'e'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"woman\" é:", "womans", "womenses", "women", "women", "Dificil", vocabularioBasico, 4, "Troca a vogal 'a' por 'e'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"child\" é:", "childs", "children", "childes", "children", "Dificil", vocabularioBasico, 4, "Adiciona 'ren'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"tooth\" é:", "tooths", "teeth", "teethes", "teeth", "Dificil", vocabularioBasico, 4, "Troca 'oo' por 'ee'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O plural de \"foot\" é:", "foots", "feets", "feet", "feet", "Dificil", vocabularioBasico, 4, "Troca 'oo' por 'ee'.");
        // Outros
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Good morning\".", "Boa noite", "Boa tarde", "Bom dia", "Bom dia", "Facil", vocabularioBasico, 1, "Cumprimento usado pela manhã.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Good night\".", "Boa noite", "Boa tarde", "Bom dia", "Boa noite", "Facil", vocabularioBasico, 1, "Despedida usada à noite.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Thank you\".", "Por favor", "De nada", "Obrigado", "Obrigado", "Facil", vocabularioBasico, 1, "Expressão de gratidão.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Please\".", "Com licença", "Por favor", "Desculpe", "Por favor", "Facil", vocabularioBasico, 1, "Usado para pedir algo educadamente.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Goodbye\".", "Olá", "Adeus", "Até logo", "Adeus", "Facil", vocabularioBasico, 1, "Despedida comum.");

        // ==============================================
        // 3. Presente Simples (Total: 50)
        // ==============================================

        // Perguntas existentes
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"I like to read\".", "Eu gosto de comer", "Eu gosto de correr", "Eu gosto de ler", "Eu gosto de ler", "Medio", presenteSimples, 2, "'Read' significa...");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"She ___ to school every day\".", "go", "goes", "going", "goes", "Medio", presenteSimples, 2, "Terceira pessoa do singular (She) precisa de 's' ou 'es'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"He works at a bank\".", "Ele trabalhou no banco", "Ele trabalha em um banco", "Ele vai trabalhar no banco", "Ele trabalha em um banco", "Medio", presenteSimples, 2, "O verbo 'works' está no presente.");

        // Novas 47 perguntas
        // Positivo (I/You/We/They)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ soccer on weekends.\"", "play", "plays", "playing", "play", "Facil", presenteSimples, 2, "Para 'They', use a forma base do verbo.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ pizza.\"", "love", "loves", "loving", "love", "Facil", presenteSimples, 2, "Para 'We', use a forma base.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"You ___ English very well.\"", "speak", "speaks", "speaking", "speak", "Facil", presenteSimples, 2, "Para 'You', use a forma base.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ in a small apartment.\"", "live", "lives", "living", "live", "Facil", presenteSimples, 2, "Para 'I', use a forma base.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"My parents ___ in Canada.\"", "live", "lives", "living", "live", "Facil", presenteSimples, 2, "'My parents' = 'They'. Use a forma base.");
        // Positivo (He/She/It)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ TV in the evening.\"", "watch", "watches", "watching", "watches", "Medio", presenteSimples, 3, "Para 'He', adicione 'es' ao verbo que termina em 'ch'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The cat ___ on the sofa.\"", "sleep", "sleeps", "sleeping", "sleeps", "Medio", presenteSimples, 3, "'The cat' = 'It'. Adicione 's'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"My brother ___ fast.\"", "run", "runs", "running", "runs", "Medio", presenteSimples, 3, "'My brother' = 'He'. Adicione 's'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"It ___ a lot in winter.\"", "rain", "rains", "raining", "rains", "Medio", presenteSimples, 3, "Para 'It', adicione 's'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ her homework after school.\"", "do", "does", "doing", "does", "Medio", presenteSimples, 3, "Para 'She', o verbo 'do' vira 'does'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ to fix cars.\"", "try", "trys", "tries", "tries", "Medio", presenteSimples, 3, "Para 'He', verbos terminados em 'y' precedido de consoante trocam 'y' por 'ies'.");
        // Negativo (don't)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ like coffee.\"", "don't", "doesn't", "am not", "don't", "Facil", presenteSimples, 2, "Negação para 'I': use 'do not'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ go to the beach often.\"", "don't", "doesn't", "aren't", "don't", "Facil", presenteSimples, 2, "Negação para 'We': use 'do not'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ understand the problem.\"", "don't", "doesn't", "aren't", "don't", "Facil", presenteSimples, 2, "Negação para 'They': use 'do not'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"You ___ need a ticket.\"", "don't", "doesn't", "aren't", "don't", "Facil", presenteSimples, 2, "Negação para 'You': use 'do not'.");
        // Negativo (doesn't)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ live here anymore.\"", "don't", "doesn't", "isn't", "doesn't", "Medio", presenteSimples, 3, "Negação para 'She': use 'does not'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ have a car.\"", "don't", "doesn't", "isn't", "doesn't", "Medio", presenteSimples, 3, "Negação para 'He': use 'does not'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"It ___ work properly.\"", "don't", "doesn't", "isn't", "doesn't", "Medio", presenteSimples, 3, "Negação para 'It': use 'does not'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"My sister ___ eat meat.\"", "don't", "doesn't", "isn't", "doesn't", "Medio", presenteSimples, 3, "'My sister' = 'She'. Negação: use 'does not'.");
        // Interrogativo (Do)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ you speak Spanish?\"", "Do", "Does", "Are", "Do", "Facil", presenteSimples, 2, "Pergunta para 'You': comece com o auxiliar.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ they live nearby?\"", "Do", "Does", "Are", "Do", "Facil", presenteSimples, 2, "Pergunta para 'They': comece com o auxiliar.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Where ___ you work?\"", "do", "does", "are", "do", "Facil", presenteSimples, 2, "Pergunta com 'Where' para 'You': use o auxiliar.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"What ___ they want?\"", "do", "does", "are", "do", "Facil", presenteSimples, 2, "Pergunta com 'What' para 'They': use o auxiliar.");
        // Interrogativo (Does)
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ she like music?\"", "Do", "Does", "Is", "Does", "Medio", presenteSimples, 3, "Pergunta para 'She': comece com o auxiliar.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ he play the guitar?\"", "Do", "Does", "Is", "Does", "Medio", presenteSimples, 3, "Pergunta para 'He': comece com o auxiliar.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"When ___ the train arrive?\"", "do", "does", "is", "does", "Medio", presenteSimples, 3, "'The train' = 'It'. Pergunta com 'When': use o auxiliar.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Why ___ it happen?\"", "do", "does", "is", "does", "Medio", presenteSimples, 3, "Pergunta com 'Why' para 'It': use o auxiliar.");
        // Advérbios de Frequência
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a posição correta: \"I ___ go to the gym.\"", "always", "go always", "I always go", "I always go", "Dificil", presenteSimples, 5, "Advérbio de frequência geralmente vem antes do verbo principal.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a posição correta: \"He is ___ late.\"", "never", "never is", "is never", "is never", "Dificil", presenteSimples, 5, "Com o verbo 'to be', o advérbio vem depois.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ visits her grandma.\"", "sometimes", "sometimes visits", "visits sometimes", "sometimes visits", "Dificil", presenteSimples, 5, "'Sometimes' pode vir no início, meio ou fim.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ eat fast food.\"", "rarely", "eat rarely", "rarely eat", "rarely eat", "Dificil", presenteSimples, 5, "Advérbio antes do verbo principal.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We ___ watch movies.\"", "often", "often watch", "watch often", "often watch", "Dificil", presenteSimples, 5, "Advérbio antes do verbo principal.");
        // Misturadas
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"The sun rises in the east.\"", "O sol se põe no leste", "O sol nasce no leste", "O sol está no leste", "O sol nasce no leste", "Dificil", presenteSimples, 5, "'Rises' significa nascer/subir.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Water ___ at 100 degrees Celsius.\"", "boil", "boils", "boiling", "boils", "Dificil", presenteSimples, 5, "'Water' = 'It'. Verdade universal.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"I don't think so.\"", "Eu não acho", "Eu não penso", "Eu acho que não", "Eu acho que não", "Medio", presenteSimples, 3, "Expressão comum para discordar.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"Do you know him?\"", "Você sabe dele?", "Você conhece ele?", "Você gosta dele?", "Você conhece ele?", "Facil", presenteSimples, 2, "'Know' significa saber ou conhecer.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"It doesn't matter.\"", "Isso não importa", "Isso não é matéria", "Não tem problema", "Isso não importa", "Medio", presenteSimples, 3, "'Matter' como verbo significa importar.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"What time ___ the bank open?\"", "do", "does", "is", "does", "Medio", presenteSimples, 3, "'The bank' = 'It'. Pergunta sobre horário.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Cats ___ fish.\"", "like", "likes", "liking", "like", "Facil", presenteSimples, 2, "'Cats' = 'They'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"A cat ___ fish.\"", "like", "likes", "liking", "likes", "Medio", presenteSimples, 3, "'A cat' = 'It'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Traduza: \"I work from home.\"", "Eu ando para casa", "Eu trabalho de casa", "Eu vou para casa", "Eu trabalho de casa", "Facil", presenteSimples, 2, "'Work' = trabalhar.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He ___ his teeth every morning.\"", "brush", "brushes", "brushing", "brushes", "Medio", presenteSimples, 3, "Para 'He', adicione 'es' ao verbo que termina em 'sh'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They ___ in a big house.\"", "live", "lives", "living", "live", "Facil", presenteSimples, 2, "Para 'They', use a forma base.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ not understand.\"", "do", "does", "am", "do", "Facil", presenteSimples, 2, "Auxiliar negativo para 'I'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ not play tennis.\"", "do", "does", "is", "does", "Medio", presenteSimples, 3, "Auxiliar negativo para 'She'.");

        // ==============================================
        // 4. Presente Perfeito (Total: 50)
        // ==============================================

        // Perguntas existentes
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a forma correta do Present Perfect de \"to be\"?", "was/were", "have been/has been", "had been", "have been/has been", "Dificil", presentePerfeito, 5, "Use 'have/has' + particípio passado.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete a frase: \"I have ___ this movie twice\".", "see", "saw", "seen", "seen", "Dificil", presentePerfeito, 5, "Particípio passado de 'see'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual a forma correta do Present Perfect de \"to go\"?", "went", "have gone", "have goed", "have gone", "Dificil", presentePerfeito, 5, "Use 'have/has' + particípio passado de 'go'.");

        // Novas 47 perguntas
        // Positivo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She has ___ her keys.\"", "lose", "lost", "losing", "lost", "Medio", presentePerfeito, 5, "Particípio passado de 'lose'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They have ___ a new car.\"", "buy", "bought", "buyed", "bought", "Medio", presentePerfeito, 5, "Particípio passado irregular de 'buy'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He has ___ to Brazil.\"", "been", "went", "gone", "been", "Dificil", presentePerfeito, 6, "'Been' indica que ele foi E voltou (ou está lá). 'Gone' indica que ele foi e NÃO voltou.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I have ___ my homework.\"", "do", "did", "done", "done", "Medio", presentePerfeito, 5, "Particípio passado de 'do'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We have ___ lunch.\"", "eat", "ate", "eaten", "eaten", "Medio", presentePerfeito, 5, "Particípio passado de 'eat'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She has ___ a book.\"", "write", "wrote", "written", "written", "Medio", presentePerfeito, 5, "Particípio passado de 'write'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They have ___ the window.\"", "break", "broke", "broken", "broken", "Medio", presentePerfeito, 5, "Particípio passado de 'break'.");
        // Negativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I haven't ___ him today.\"", "see", "saw", "seen", "seen", "Medio", presentePerfeito, 5, "Use o particípio passado na negativa.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He hasn't ___ yet.\"", "arrive", "arrived", "arriving", "arrived", "Medio", presentePerfeito, 5, "Use o particípio passado na negativa.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We haven't ___ to them.\"", "speak", "spoke", "spoken", "spoken", "Medio", presentePerfeito, 5, "Use o particípio passado na negativa.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She hasn't ___ her breakfast.\"", "finish", "finished", "finishing", "finished", "Medio", presentePerfeito, 5, "Use o particípio passado na negativa.");
        // Interrogativo
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ you ever ___ sushi?\"", "Have / eat", "Have / eaten", "Did / eat", "Have / eaten", "Medio", presentePerfeito, 5, "Estrutura da pergunta: Auxiliar + Sujeito + Particípio.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ he ___ the email?\"", "Has / send", "Has / sent", "Did / send", "Has / sent", "Medio", presentePerfeito, 5, "Estrutura da pergunta: Auxiliar + Sujeito + Particípio.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Where ___ they gone?\"", "have", "has", "did", "have", "Medio", presentePerfeito, 5, "Pergunta com 'Where': use o auxiliar correto para 'they'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"What ___ she done?\"", "have", "has", "did", "has", "Medio", presentePerfeito, 5, "Pergunta com 'What': use o auxiliar correto para 'she'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"___ you finished the report?\"", "Have", "Has", "Did", "Have", "Medio", presentePerfeito, 5, "Pergunta de sim/não: comece com o auxiliar para 'you'.");
        // For vs Since
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I have lived here ___ 10 years.\"", "for", "since", "ago", "for", "Dificil", presentePerfeito, 6, "'For' é usado para durações de tempo.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She has been a teacher ___ 2010.\"", "for", "since", "ago", "since", "Dificil", presentePerfeito, 6, "'Since' é usado para pontos de partida no tempo.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"We have known each other ___ a long time.\"", "for", "since", "ago", "for", "Dificil", presentePerfeito, 6, "'A long time' é uma duração.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He hasn't eaten ___ this morning.\"", "for", "since", "ago", "since", "Dificil", presentePerfeito, 6, "'This morning' é um ponto de partida.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"They have been married ___ 3 months.\"", "for", "since", "ago", "for", "Dificil", presentePerfeito, 6, "'3 months' é uma duração.");
        // Yet vs Already
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Have you finished ___?\"", "already", "yet", "since", "yet", "Dificil", presentePerfeito, 6, "'Yet' é comum em perguntas e negativas, geralmente no final.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I have ___ seen that movie.\"", "already", "yet", "for", "already", "Dificil", presentePerfeito, 6, "'Already' indica que algo aconteceu antes do esperado, comum em afirmativas.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"The train hasn't arrived ___.\"", "already", "yet", "ago", "yet", "Dificil", presentePerfeito, 6, "'Yet' é comum em negativas, indicando que algo ainda não aconteceu.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She has ___ left.\"", "already", "yet", "for", "already", "Dificil", presentePerfeito, 6, "'Already' pode indicar surpresa ou que aconteceu cedo.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He's only 20, but he has ___ started his own company.\"", "already", "yet", "since", "already", "Dificil", presentePerfeito, 6, "'Already' enfatiza que aconteceu cedo para a idade dele.");
        // Past Simple vs Present Perfect
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual está correto para uma ação terminada no passado?", "I have seen him yesterday.", "I saw him yesterday.", "I seen him yesterday.", "I saw him yesterday.", "Dificil", presentePerfeito, 7, "'Yesterday' indica um tempo específico no passado, use Past Simple.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Qual está correto para uma experiência de vida?", "Did you ever eat sushi?", "Have you ever eaten sushi?", "Have you ever ate sushi?", "Have you ever eaten sushi?", "Dificil", presentePerfeito, 7, "Para experiências de vida sem tempo definido, use Present Perfect.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"She ___ to Paris last year.\"", "has gone", "went", "has been", "went", "Dificil", presentePerfeito, 7, "'Last year' é um tempo específico no passado, use Past Simple.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ my keys. I can't find them.\"", "lost", "have lost", "did lose", "have lost", "Dificil", presentePerfeito, 7, "Ação passada com resultado no presente (não consigo encontrá-las), use Present Perfect.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Einstein ___ the theory of relativity.\"", "has developed", "developed", "develop", "developed", "Dificil", presentePerfeito, 7, "Einstein já morreu, ação completamente no passado, use Past Simple.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I ___ four emails so far this morning.\"", "wrote", "have written", "write", "have written", "Dificil", presentePerfeito, 7, "'So far' (até agora) indica uma ação que continua ou tem relevância no presente, use Present Perfect.");
        // Misturadas
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to break\":", "broke", "broken", "breaked", "broken", "Medio", presentePerfeito, 5, "Verbo irregular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to choose\":", "chose", "choosed", "chosen", "chosen", "Medio", presentePerfeito, 5, "Verbo irregular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to speak\":", "spoke", "spoken", "speaked", "spoken", "Medio", presentePerfeito, 5, "Verbo irregular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to take\":", "toke", "taked", "taken", "taken", "Medio", presentePerfeito, 5, "Verbo irregular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to drive\":", "drove", "driven", "drived", "driven", "Medio", presentePerfeito, 5, "Verbo irregular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to find\":", "found", "finded", "faund", "found", "Medio", presentePerfeito, 5, "Verbo irregular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to give\":", "gave", "gived", "given", "given", "Medio", presentePerfeito, 5, "Verbo irregular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to know\":", "knew", "known", "knowed", "known", "Medio", presentePerfeito, 5, "Verbo irregular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Particípio passado de \"to swim\":", "swam", "swum", "swimmed", "swum", "Medio", presentePerfeito, 5, "Verbo irregular.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"He hasn't worked here ___ long.\"", "for", "since", "ago", "for", "Dificil", presentePerfeito, 6, "'Long' (muito tempo) é uma duração.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"I have known him ___ we were children.\"", "for", "since", "ago", "since", "Dificil", presentePerfeito, 6, "'We were children' é um ponto de partida no tempo.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "Complete: \"Have you done your homework ___?\"", "already", "yet", "since", "yet", "Dificil", presentePerfeito, 6, "Uso comum de 'yet' em perguntas sobre conclusão de tarefas.");

        // ==============================================
        // 5. Expressões Idiomáticas (Total: 50)
        // ==============================================

        // Perguntas existentes
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"break a leg\"?", "Quebre a perna", "Boa sorte", "Desastre", "Boa sorte", "Medio", expressoesIdiomaticas, 7, "Usado no teatro antes de uma apresentação.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"it's raining cats and dogs\"?", "Está chovendo muito", "Está chovendo gatos e cachorros", "A tempestade é forte", "Está chovendo muito", "Dificil", expressoesIdiomaticas, 7, "Expressão para chuva intensa.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"break the bank\"?", "Quebrar o banco", "Gastar muito dinheiro", "Assaltar um banco", "Gastar muito dinheiro", "Dificil", expressoesIdiomaticas, 7, "Gastar todo o dinheiro disponível.");

        // Novas 47 perguntas
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"a piece of cake\"?", "Um pedaço de bolo", "Algo muito difícil", "Algo muito fácil", "Algo muito fácil", "Medio", expressoesIdiomaticas, 6, "Comer bolo é fácil e agradável.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"once in a blue moon\"?", "Toda noite de lua azul", "Algo muito raro", "Algo muito comum", "Algo muito raro", "Medio", expressoesIdiomaticas, 6, "A 'lua azul' (segunda lua cheia no mesmo mês) é rara.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"under the weather\"?", "Abaixo do tempo", "Estar indisposto/doente", "Estar feliz", "Estar indisposto/doente", "Medio", expressoesIdiomaticas, 6, "Antigamente, marinheiros doentes ficavam abaixo do convés, longe do 'tempo'.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to cost an arm and a leg\"?", "Custar um braço e uma perna", "Ser muito barato", "Ser muito caro", "Ser muito caro", "Medio", expressoesIdiomaticas, 6, "Algo tão caro que parece exigir um sacrifício físico.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to hit the nail on the head\"?", "Acertar o prego na cabeça", "Estar exatamente correto", "Estar completamente errado", "Estar exatamente correto", "Dificil", expressoesIdiomaticas, 7, "Acertar o ponto exato.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to bite the bullet\"?", "Morder a bala", "Enfrentar algo difícil com coragem", "Desistir", "Enfrentar algo difícil com coragem", "Dificil", expressoesIdiomaticas, 7, "Antigamente, pacientes mordiam balas durante cirurgias sem anestesia.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to spill the beans\"?", "Derrubar os feijões", "Contar um segredo", "Fazer o jantar", "Contar um segredo", "Medio", expressoesIdiomaticas, 6, "Como se deixasse escapar algo que não devia.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to bark up the wrong tree\"?", "Latir para a árvore errada", "Estar enganado / procurando no lugar errado", "Estar no caminho certo", "Estar enganado / procurando no lugar errado", "Dificil", expressoesIdiomaticas, 7, "Imagine um cão de caça a latir para uma árvore onde a presa não está.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"the ball is in your court\"?", "A bola está na sua quadra", "É sua vez de tomar uma atitude", "Você perdeu o jogo", "É sua vez de tomar uma atitude", "Medio", expressoesIdiomaticas, 6, "Analogia com desportos como ténis.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to let the cat out of the bag\"?", "Deixar o gato sair da bolsa", "Revelar um segredo acidentalmente", "Adotar um gato", "Revelar um segredo acidentalmente", "Dificil", expressoesIdiomaticas, 7, "Como se algo escondido fosse revelado.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to get a taste of your own medicine\"?", "Provar o seu próprio remédio", "Receber o mesmo tratamento ruim que deu aos outros", "Ser um farmacêutico", "Receber o mesmo tratamento ruim que deu aos outros", "Dificil", expressoesIdiomaticas, 7, "Experimentar as consequências negativas das próprias ações.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to jump on the bandwagon\"?", "Pular na carroça da banda", "Seguir uma moda ou tendência", "Ir a um desfile", "Seguir uma moda ou tendência", "Dificil", expressoesIdiomaticas, 7, "Antigamente, carroças com bandas atraíam multidões em campanhas políticas.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"the last straw\"?", "O último canudo", "O limite da paciência / o último problema", "A bebida final", "O limite da paciência / o último problema", "Dificil", expressoesIdiomaticas, 7, "Refere-se à fábula do camelo que quebrou as costas com o último canudo.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to beat around the bush\"?", "Bater ao redor do arbusto", "Evitar falar sobre o assunto principal / enrolar", "Ser direto", "Evitar falar sobre o assunto principal / enrolar", "Medio", expressoesIdiomaticas, 6, "Em caçadas antigas, batedores atingiam os arbustos para espantar a presa, sem ir direto a ela.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to bite off more than you can chew\"?", "Morder mais do que consegue mastigar", "Tentar fazer mais do que se é capaz", "Estar com muita fome", "Tentar fazer mais do que se é capaz", "Medio", expressoesIdiomaticas, 6, "Como tentar comer um pedaço grande demais.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to make matters worse\"?", "Fazer a matéria piorar", "Piorar uma situação que já está ruim", "Melhorar uma situação", "Piorar uma situação que já está ruim", "Medio", expressoesIdiomaticas, 6, "Tornar os assuntos ('matters') piores.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to see eye to eye\"?", "Ver olho no olho", "Concordar completamente com alguém", "Ter boa visão", "Concordar completamente com alguém", "Medio", expressoesIdiomaticas, 6, "Ter a mesma perspectiva.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"when pigs fly\"?", "Quando porcos voarem", "Algo que nunca vai acontecer", "Um milagre", "Algo que nunca vai acontecer", "Medio", expressoesIdiomaticas, 6, "Porcos não voam.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"speak of the devil\"?", "Falar do diabo", "Falar de alguém que aparece em seguida", "Estar com problemas", "Falar de alguém que aparece em seguida", "Medio", expressoesIdiomaticas, 6, "Ditado popular: 'Falando no diabo...'");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"the elephant in the room\"?", "O elefante na sala", "Um problema óbvio que ninguém quer discutir", "Um animal de estimação grande", "Um problema óbvio que ninguém quer discutir", "Dificil", expressoesIdiomaticas, 7, "Algo tão grande e óbvio que é impossível ignorar, mas todos evitam.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to cut corners\"?", "Cortar cantos", "Fazer algo de forma mais fácil/barata, sacrificando a qualidade", "Ser um bom motorista", "Fazer algo de forma mais fácil/barata, sacrificando a qualidade", "Dificil", expressoesIdiomaticas, 7, "Como cortar caminho em vez de seguir a rota correta.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to add insult to injury\"?", "Adicionar insulto à injúria", "Piorar uma situação ruim com mais uma coisa ruim", "Ser rude com alguém machucado", "Piorar uma situação ruim com mais uma coisa ruim", "Dificil", expressoesIdiomaticas, 7, "Tornar algo já mau ainda pior.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"don't cry over spilled milk\"?", "Não chore sobre o leite derramado", "Não adianta lamentar por algo que já aconteceu", "Tenha cuidado com laticínios", "Não adianta lamentar por algo que já aconteceu", "Medio", expressoesIdiomaticas, 6, "Não há o que fazer depois que o leite derramou.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"every cloud has a silver lining\"?", "Toda nuvem tem um forro de prata", "Toda situação ruim tem um lado positivo", "O tempo vai melhorar", "Toda situação ruim tem um lado positivo", "Medio", expressoesIdiomaticas, 6, "Mesmo nuvens escuras têm bordas brilhantes pelo sol.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to get cold feet\"?", "Ficar com pés frios", "Ficar nervoso e desistir de algo", "Estar com frio", "Ficar nervoso e desistir de algo", "Medio", expressoesIdiomaticas, 6, "Medo ou nervosismo antes de um evento importante.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to get out of hand\"?", "Sair da mão", "Ficar fora de controle", "Lavar as mãos", "Ficar fora de controle", "Medio", expressoesIdiomaticas, 6, "Algo que não pode mais ser controlado.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to hit the books\"?", "Bater nos livros", "Estudar com afinco", "Organizar a estante", "Estudar com afinco", "Medio", expressoesIdiomaticas, 6, "Começar a estudar seriamente.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to hit the sack\" / \"to hit the hay\"?", "Bater no saco / Bater no feno", "Ir dormir", "Trabalhar na fazenda", "Ir dormir", "Medio", expressoesIdiomaticas, 6, "Ir para a cama (saco de dormir ou colchão de feno).");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to keep an eye on someone/something\"?", "Manter um olho em alguém/algo", "Vigiar ou cuidar de alguém/algo", "Ser vesgo", "Vigiar ou cuidar de alguém/algo", "Medio", expressoesIdiomaticas, 6, "Prestar atenção.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to be on the ball\"?", "Estar na bola", "Estar alerta, rápido e eficiente", "Jogar futebol", "Estar alerta, rápido e eficiente", "Dificil", expressoesIdiomaticas, 7, "Estar atento e pronto para agir.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"(to be) on thin ice\"?", "Estar em gelo fino", "Estar em uma situação arriscada", "Patinar no gelo", "Estar em uma situação arriscada", "Medio", expressoesIdiomaticas, 6, "Uma situação perigosa onde algo ruim pode acontecer.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to ring a bell\"?", "Tocar um sino", "Soar familiar / lembrar vagamente", "Chamar atenção", "Soar familiar / lembrar vagamente", "Medio", expressoesIdiomaticas, 6, "Algo que desperta uma memória, mesmo que incerta.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to sit on the fence\"?", "Sentar na cerca", "Estar indeciso / não querer tomar partido", "Descansar", "Estar indeciso / não querer tomar partido", "Medio", expressoesIdiomaticas, 6, "Não escolher um lado numa discussão.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"straight from the horse's mouth\"?", "Direto da boca do cavalo", "Da fonte original ou mais confiável", "Uma informação falsa", "Da fonte original ou mais confiável", "Dificil", expressoesIdiomaticas, 7, "Informação obtida diretamente da pessoa envolvida.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"the best of both worlds\"?", "O melhor de ambos os mundos", "Ter as vantagens de duas situações diferentes", "Estar em dois lugares ao mesmo tempo", "Ter as vantagens de duas situações diferentes", "Medio", expressoesIdiomaticas, 6, "Aproveitar o lado bom de duas coisas.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to twist someone's arm\"?", "Torcer o braço de alguém", "Convencer alguém com insistência / persuadir", "Machucar alguém", "Convencer alguém com insistência / persuadir", "Medio", expressoesIdiomaticas, 6, "Pressionar alguém a fazer algo.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to be up in the air\"?", "Estar no ar", "Estar incerto / ainda não decidido", "Estar voando", "Estar incerto / ainda não decidido", "Medio", expressoesIdiomaticas, 6, "Planos ou situações indefinidas.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to call it a day\"?", "Chamar isso de um dia", "Decidir parar de trabalhar por hoje", "Agendar um dia", "Decidir parar de trabalhar por hoje", "Medio", expressoesIdiomaticas, 6, "Encerrar as atividades do dia.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to be glad to see the back of someone\"?", "Feliz em ver as costas de alguém", "Ficar feliz quando alguém vai embora", "Sentir falta de alguém", "Ficar feliz quando alguém vai embora", "Dificil", expressoesIdiomaticas, 7, "Alívio quando uma pessoa irritante parte.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"against the clock\"?", "Contra o relógio", "Fazer algo muito rápido / com prazo apertado", "Trabalhar à noite", "Fazer algo muito rápido / com prazo apertado", "Medio", expressoesIdiomaticas, 6, "Com pouco tempo disponível.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"(to have) a chip on your shoulder\"?", "Ter um pedaço no ombro", "Estar ressentido / guardar rancor por algo", "Estar com fome", "Estar ressentido / guardar rancor por algo", "Dificil", expressoesIdiomaticas, 7, "Agir de forma defensiva ou zangada por causa de uma injustiça passada.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"a blessing in disguise\"?", "Uma bênção disfarçada", "Algo que parece ruim no início, mas se revela bom", "Um presente ruim", "Algo que parece ruim no início, mas se revela bom", "Medio", expressoesIdiomaticas, 6, "Um bem aparente vindo de um mal.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to burn the midnight oil\"?", "Queimar o óleo da meia-noite", "Trabalhar ou estudar até tarde da noite", "Causar um incêndio", "Trabalhar ou estudar até tarde da noite", "Dificil", expressoesIdiomaticas, 7, "Referência às lamparinas a óleo usadas antigamente.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"don't put all your eggs in one basket\"?", "Não coloque todos os ovos em uma cesta", "Não concentre todos os seus recursos ou esperanças em uma só coisa", "Tenha cuidado ao fazer compras", "Não concentre todos os seus recursos ou esperanças em uma só coisa", "Medio", expressoesIdiomaticas, 6, "Diversifique seus riscos.");
        addQuestionIfNotExists(desiredQuestions, existingQuestions, "O que significa \"to face the music\"?", "Encarar a música", "Aceitar as consequências de suas ações", "Ir a um concerto", "Aceitar as consequências de suas ações", "Medio", expressoesIdiomaticas, 6, "Lidar com a realidade de uma situação ruim que você causou.");

        // --- FIM DA ADIÇÃO DE PERGUNTAS ---

        if (!desiredQuestions.isEmpty()) {
            System.out.println("Inserindo " + desiredQuestions.size() + " novas questões (com dicas)...");
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
                                        String resposta, String dificuldade, Content content, int levelMinimo, String hint) {
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
            q.setHint(hint);
            desiredQuestions.add(q);
        }
    }

    // Método para popular NPCs iniciais

    private void seedNpc(NPCRepository npcRepository) {

        Map<String, NPC> existingNpcs = npcRepository.findAll().stream()
                .collect(Collectors.toMap(NPC::getName, Function.identity()));

        List<NPC> desiredNpcs = new ArrayList<>();

        if (!existingNpcs.containsKey("Goblin da Gramática")) {
            NPC goblinGramatica = new NPC();
            goblinGramatica.setName("Goblin da Gramática");
            goblinGramatica.setDescription("Um guia excêntrico que ajuda os aventureiros a dominar as regras gramaticais do inglês enquanto exploram o mundo do RPG.");
            goblinGramatica.setType("info");
            goblinGramatica.setLocation("Cidade Principal");
            desiredNpcs.add(goblinGramatica);
        }

        if (!desiredNpcs.isEmpty()) { // <-- CORRIGIDO AQUI
            System.out.println("Inserindo " + desiredNpcs.size() + " novos NPCs...");
            npcRepository.saveAll(desiredNpcs);
        } else {
            System.out.println("Tabela 'npc' já está atualizada.");
        }
    }

    private void seedDialogue(DialogueRepository dialogueRepository, NPCRepository npcRepository){
        System.out.println("Verificando/adicionando diálogos...");

        Map<String, Dialogue> existingDialogue = dialogueRepository.findAll().stream()
                .collect(Collectors.toMap(Dialogue::getContent, Function.identity()));

        List<Dialogue> desiredDialogue = new ArrayList<>();

        NPC goblinGramatica = npcRepository.findByName("Goblin da Gramática").orElse(null);
        // Diálogos do Goblin da Gramática
        if (goblinGramatica != null) { // Adiciona diálogos apenas se o NPC foi encontrado
            addDialogueIfNotExists(desiredDialogue, existingDialogue, goblinGramatica,
                    "Ora, ora... Vejam só! Um 'Outworlder'! Bem-vindo, ou melhor, *welcome* a Aetheria! Ou seria 'Bem-vindo A Aetheria'? Ah, as preposições...", null);
            addDialogueIfNotExists(desiredDialogue, existingDialogue, goblinGramatica,
                    "Você deve estar confuso, não é? Este lugar, Aetheria, era um mundo vibrante, tecido pelas Palavras... até *Ele* chegar.", null);
            addDialogueIfNotExists(desiredDialogue, existingDialogue, goblinGramatica,
                    "Malak, O Silenciador. Um ser terrível que odeia a comunicação! Ele espalhou o 'Silêncio Crescente', uma praga que apaga as palavras, tornando tudo... *mudo* e sem sentido. Os livros estão em branco, as canções esquecidas...", null);
            addDialogueIfNotExists(desiredDialogue, existingDialogue, goblinGramatica,
                    "Mas há esperança! A sua língua, a 'Língua Arcana' vinda do seu mundo... Malak não pode tocá-la! É por isso que você foi invocado! Cada palavra que você conhece, cada regra gramatical... *isso* é poder aqui!", null);
            addDialogueIfNotExists(desiredDialogue, existingDialogue, goblinGramatica,
                    "Você é a nossa única chance de restaurar Aetheria. Aprenda, use a Língua Arcana, e ajude-nos a derrotar o Silenciador! Mas preste atenção na conjugação, por favor!", null);
        } else {
            System.err.println("### AVISO: NPC 'Goblin da Gramática' não encontrado. Seus diálogos não foram adicionados. ###");
        }

        if (!desiredDialogue.isEmpty()) {
            System.out.println("Inserindo " + desiredDialogue.size() + " novos diálogos...");
            dialogueRepository.saveAll(desiredDialogue);
        } else {
            System.out.println("Tabela 'dialogue' já está atualizada.");
        }

    }

    private void addDialogueIfNotExists(List<Dialogue> desiredDialogues,
                                          Map<String, Dialogue> existingDialogues,
                                          NPC npc, String content, String response) {
        // Verifica se o NPC é válido antes de prosseguir
        if (npc == null) {
            System.err.println("### ERRO: Tentativa de adicionar diálogo para NPC nulo. Conteúdo: '" + content + "' ###");
            return;
        }
        // Verifica se o diálogo já existe pelo conteúdo
        if (!existingDialogues.containsKey(content)) {
            Dialogue newDialogue = new Dialogue();
            newDialogue.setNpc(npc);
            newDialogue.setContent(content);
            newDialogue.setResponse(response); // Define a resposta (pode ser null)
            desiredDialogues.add(newDialogue);
        }
    }
}



