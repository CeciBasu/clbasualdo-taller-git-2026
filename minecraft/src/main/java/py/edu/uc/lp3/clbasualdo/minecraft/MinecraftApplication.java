package py.edu.uc.lp3.clbasualdo.minecraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Descripcion:
 * Clase que arranca el proyecto usando Spring Boot.
 *
 * Responsabilidad:
 * Se encarga de iniciar la aplicacion de Spring.
 */
@SpringBootApplication
public class MinecraftApplication {

	/**
	 * Descripcion:
	 * Metodo donde empieza a ejecutarse la aplicacion de Spring.
	 *
	 * Parametros:
	 * args - Argumentos que se reciben al ejecutar el programa.
	 */
	public static void main(String[] args) {
		SpringApplication.run(MinecraftApplication.class, args);
	}

}
