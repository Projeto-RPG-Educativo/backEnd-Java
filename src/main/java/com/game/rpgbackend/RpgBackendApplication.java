package com.game.rpgbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação RPG Backend.
 * <p>
 * Esta é a classe de inicialização da aplicação Spring Boot que gerencia
 * um sistema de RPG com batalhas, personagens, quests e sistema de perguntas educacionais.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class RpgBackendApplication {

	/**
	 * Método principal que inicia a aplicação Spring Boot.
	 *
	 * @param args argumentos da linha de comando
	 */
	public static void main(String[] args) {
		SpringApplication.run(RpgBackendApplication.class, args);
	}

}
