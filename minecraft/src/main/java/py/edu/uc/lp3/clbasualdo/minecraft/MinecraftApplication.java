package py.edu.uc.lp3.clbasualdo.minecraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Descripción:
 * Clase que arranca el proyecto usando Spring Boot.
 *
 * Responsabilidad:
 * Se encarga de iniciar la aplicación de Spring.
 */
@SpringBootApplication
public class MinecraftApplication {

	/**
	 * Descripción:
	 * Método donde empieza a ejecutarse la aplicación de Spring.
	 *
	 * Parámetros:
	 * args - Argumentos que se reciben al ejecutar el programa.
	 */
	public static void main(String[] args) {
		SpringApplication.run(MinecraftApplication.class, args);
	}

}
