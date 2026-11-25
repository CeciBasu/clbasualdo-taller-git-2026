# Bitácora — Taller de Git y práctica POO + API REST

## Asistencia de inteligencia artificial

Se trabajó con **dos** asistentes. La rúbrica pide la marca y el modelo exacto con que se
trabajó, así que van los dos.

| # | Asistente / agente | Modelo exacto del LLM | Qué se hizo con esa herramienta |
|---|---|---|---|
| 1 | **Claude** (Anthropic) | Sonnet 5.5 | Endurecer la jerarquía de POO, el refactor del proyecto a capas, los comentarios de todo el código y el diagrama Mermaid del README. |
| 2 | **OpenCode** | `big-pickle` (ID completo: `opencode/big-pickle`) | Revisión final de POO-06 (`docs/BITACORA.md`, `docs/ESPECIFICACIONES-poo-06.md`, la sobrecarga `Esqueleto.disparar()` y su ruta por HTTP, las pruebas nuevas y el README) y, en POO-08, la familia JPA, el CRUD, la simulación persistida, la jerarquía de excepciones, la colección de Insomnia y la documentación de esta entrega. |
| 3 | **Claude Code** (Anthropic, app de escritorio) | Claude Opus 5.5 (`claude-opus-5-5`) | POO-09: cierre de las observaciones de la corrección de POO-08, publicación del OpenAPI, clientes hacia A2 y A3, escenario «emboscada del Creeper», colección de Insomnia y documentación. |



### Resumen de los prompts

Los prompts se agrupan por lo que se pidió, no por sesión. En todos los casos el objetivo
era el mismo: que el modelo no inventara diseño nuevo sino que respetara el modelado de las
clases y, sobre todo, que no escribiera las reglas del juego fuera del dominio. Cada punto
lleva la herramienta con la que se hizo.

1. **[Claude] Revisar la jerarquía de POO y endurecerla.** Partir de la jerarquía que yo
   tenía (con `nombre` y `vida` en `protected`) y pedir que me explicara por qué eso estaba
   mal: con `protected`, cualquier clase del mismo paquete podía hacer `entidad.vida = -500`
   sin pasar por ningún método. De ahí salió el cambio a `private` con getters y con
   `recibirDanio()` / `curar()` como únicos mensajes que tocan el estado.

2. **[Claude] Preguntar por `protected` contra `private`, y por qué `curar()` quedó
   `protected`.** Este fue el punto que más me costó entender. La respuesta que me terminó
   cerrando el diseño: la modificación real de `vida` tiene que seguir pasando dentro de
   `Entidad`, que es la única dueña del campo; `curar()` es `protected` porque es una
   herramienta interna de la jerarquía y no tiene sentido que cualquier entidad pueda
   regenerarse desde afuera (un `Creeper` no se regenera); y `Jugador` la expone hacia
   afuera con su propio método público `regenerar()`.

3. **[Claude] Agregar el método abstracto `reaccionar()` y entender el contrato.** Pedir un
   método abstracto en la clase base que exprese cómo responde cada entidad ante el jugador,
   y que la base no pueda implementar. Lo que me costó ver es que, por ser abstracto, obliga
   a *todas* las clases concretas de la jerarquía, no solo a las dos que después se muestran
   en el controller: si olvidaba una (por ejemplo `Zombie`), el proyecto directamente no
   compilaba.

4. **[Claude] Armar los controllers.** Un `IndexController` para `GET /`, un
   `EsqueletoController` que reciba nombre y vida por `@RequestParam` y construya el esqueleto
   **pasando esos valores por el constructor y no asignando campos a mano**, y un
   `ComportamientoController` con `/comportamiento` que arme una `List<Entidad>` —el tipo
   padre— con las hijas adentro y les pida `reaccionar()` a todas, sin ningún `if` por tipo.

5. **[Claude] Preguntar por errores de compilación concretos.** Los tres que más me
   confundieron: unos getters escritos fuera de las llaves de la clase (Java los interpretó
   como código suelto y tiró *"unnamed classes are a preview feature"*); un archivo del
   controller guardado con el nombre de la clase del dominio (`Esqueleto.java` en vez de
   `EsqueletoController.java`), que provocó *duplicate class*; y la línea `package` faltante
   en varios archivos, que hace que Java no encuentre la clase aunque la carpeta diga otra
   cosa.

6. **[Claude] Refactor a capas y comentarios del código.** Pedir que el proyecto quedara
   ordenado como el template de paquetes de la cátedra (`domain`, `repository` + `impl`,
   `service` + `impl`, `rest.controller`, `constants`, `exceptions`), con
   `MinecraftApplication` en la raíz y el dominio sin ninguna dependencia de Spring. En la
   misma línea, pedir que se comentara todo el código con el formato `Descripcion` /
   `Parametros` / `Retorno`, para poder entender lo que estaba haciendo.

7. **[Claude] Verificar y documentar.** `./mvnw test` en verde, prueba de los endpoints en el
   navegador, y el diagrama Mermaid en el README alineado con lo que hay en `src/`.

8. **[OpenCode] Agregar la sobrecarga de `disparar()` y exponerla por HTTP.** La consigna
   pedía sobrecargar un mensaje del dominio y lo daba como ejemplo "disparar sin más datos, o
   disparar indicando una distancia". Se agregó `Esqueleto.disparar()` y
   `Esqueleto.disparar(double distancia)`: la primera dispara al alcance del esqueleto y la
   segunda revisa la distancia antes de gastar la flecha. Se pidió además que las dos
   variantes se vieran juntas en el JSON de una ruta nueva, y que el controller no tuviera
   reglas del juego: el servicio arma el resultado y el controller solo lo devuelve.

9. **[OpenCode] Revisar el diseño contra la rúbrica.** Pedir un repaso de las ocho consignas
   contra lo que ya estaba hecho, para detectar los huecos: faltaba la bitácora, faltaba el
   apartado del README que explica sobrecarga y sobreescritura, y las sobrecargas que había no
   eran de una acción de dominio visible por la API.

10. **[OpenCode] POO-08: persistencia de una familia, CRUD, reglas y excepciones.** Pedir que
    la API dejara de guardar una sola entidad y pasara a persistir una familia con un padre y
    tres hijas (`EntidadEntity`, `CreeperEntity`, `ZombieEntity`, `JugadorEntity`) en una tabla
    con discriminador. Además del CRUD completo, se pidió que la simulación de explosión
    guardara el estado final de las tres entidades y una foto en `SIMULACIONES_EXPLOSION`, con
    `GET` por id, última y lista. También se pidió una jerarquía de excepciones con un handler
    por tipo (`404`, `400`, `422`, `409`), una colección de Insomnia con un pedido por cada
    error y la documentación de la entrega.

Algo muy gracioso que me paso con opencode fue que al limpiar el worktree borro la rama por error pero pudo recuperar los objetos, verifico los contenidos y nada se perdio por suerte pero fue muy gracioso cuando lei.

---

## De qué se trata esta entrega

Esta es la bitácora de lo que fui haciendo para el taller de Git, aplicado al modelado de
Minecraft que vimos en clases. La idea era agarrar ese modelo (una jerarquía de clases con
herencia) y exponerlo como una API con Spring Boot, pero antes había que mejorarlo: que
nadie desde afuera pudiera dejar un objeto en un estado inválido, y que se pudiera tratar
cualquier entidad de la misma forma sin preguntar de qué tipo concreto es.

**Repositorio:** <https://github.com/CeciBasu/clbasualdo-taller-git-2026>

## El modelo que venía de POO-03

La jerarquía que traía de POO-03 era esta: `Entidad` (abstracta) con `nombre` y `vida` como
campos `protected`, y dos ramas abstractas: `EntidadHostil` (con `atacar()` abstracto) y
`EntidadPasiva` (con `huir()`). De ahí colgaban `Creeper`, `Zombie` y `Esqueleto` del lado
hostil, y `Aldeano` y `Animal` del lado pasivo. Aparte estaba `Jugador`, que hereda directo
de `Entidad`.

**El problema (que no había notado):** los campos `protected` se pueden tocar directo desde
cualquier clase del mismo paquete, sin pasar por ningún método. O sea que nada impedía, en
teoría, hacer algo como `entidad.vida = -500` desde afuera de la jerarquía. Eso rompe justo
lo que pide la consigna: que nadie pueda dejar una entidad en un estado imposible.

## Los controllers

### `IndexController`

Es el más simple: responde `GET /` con un texto que confirma que el servicio está vivo y de
qué dominio se trata (Minecraft, en mi caso). No construye ninguna entidad.

### `EsqueletoController`

Este recibe nombre y vida como parámetros de la URL (`@RequestParam`), construye un
`Esqueleto` real pasando esos valores por el constructor (no asignando campos a mano) y
devuelve un JSON con lo que el objeto informa de sí mismo a través de sus getters. Tuve que
agregarle al `Esqueleto` un constructor con parámetros, porque antes solo tenía uno fijo.

### Los errores que tuve acá

Me llevé varios errores de compilación en este paso, por lo que tuve que consultar con la IA
ya que no entendía por qué pasaba.

- Creé unos getters (`getNombre`, `getVida`) arriba del todo del archivo `Entidad.java`,
  fuera de las llaves de la clase. Java los interpretó como código suelto y tiró un error de
  *"unnamed classes are a preview feature"*. La solución fue simplemente moverlos adentro de
  la clase.
- Guardé el archivo del `EsqueletoController` con el nombre `Esqueleto.java` en vez de
  `EsqueletoController.java`. En Java el nombre del archivo tiene que coincidir con el de la
  clase pública que contiene, así que tiré error de *"duplicate class"*, porque además ya
  existía otra clase `Esqueleto` (la del dominio). Lo arreglé renombrando el archivo.
- Me faltaba la línea `package` en varios archivos (`Esqueleto.java`,
  `MinecraftApplication.java`, y capaz algún otro). Sin esa línea, Java no sabe en qué paquete
  está la clase realmente, aunque la carpeta diga otra cosa, y tira errores de *"cannot
  access"* o de que el archivo *"no contiene la clase"*. La solución fue revisar todos los
  archivos y poner el `package` que corresponde a la carpeta donde está cada uno.

Después de esto probé los dos endpoints en el navegador y anduvieron bien, así que hice commit
y push de este paso.

## De `protected` a `private`

Cambié `nombre` y `vida` de `protected` a `private` en `Entidad`. Esto significa que ni
siquiera las clases hijas pueden tocar esos campos directamente: todo pasa por mensajes
(`recibirDanio`, `curar`) que además validan que el valor tenga sentido (por ejemplo, no se
puede dañar con un número negativo). Esto rompió algunas líneas que usaban `nombre` directo
(como en `Animal.comer()` o en `Jugador`), así que tuve que cambiarlas por `getNombre()`.

## El método abstracto: `reaccionar()`

Agregué en `Entidad` un método abstracto llamado `reaccionar()`, que representa cómo cada
entidad responde ante la presencia del jugador. La clase base no lo puede implementar porque
no tiene forma de saber cómo se comporta cada tipo concreto: un `Creeper` explota, un
`Esqueleto` dispara, un `Aldeano` huye, un `Animal` ni se inmuta (si no le hacen daño). Eso es
información que solo tiene cada subclase, así que cada una tuvo que escribir su propia versión.

Una cosa que no tenía tan clara al principio: como el método es abstracto en la clase base,
todas las clases concretas de toda la jerarquía tienen la obligación de implementarlo, no solo
las dos que después iba a mostrar en el controller. Si me olvidaba de una (por ejemplo
`Zombie`), el proyecto directamente no compilaba. Eso me sirvió para entender que un método
abstracto es una especie de "contrato" que obliga a todos los que heredan.

## El método `protected` `curar()`

Para el `Jugador` necesitaba algo que le permitiera regenerar vida, pero sin volver a abrir el
campo `vida`. La solución fue agregar un método `curar(int cantidad)` en `Entidad`, pero como
`protected`, no público. La razón: no tiene sentido que cualquier entidad pueda "curarse" desde
afuera (las entidades hostiles no se regeneran), entonces no puede ser un método público y
general de toda la jerarquía. Pero tampoco alcanzaba con un método público en la clase
`Jugador` nada más, porque la modificación real de `vida` tiene que seguir pasando dentro de
`Entidad`, que es la única dueña del campo. Por eso `protected`: es una herramienta que solo
las clases de la propia jerarquía pueden usar, y `Jugador` la expone hacia afuera con su
propio método `regenerar()`.

## El controller y el polimorfismo

Armé un `ComportamientoController` con un endpoint `/comportamiento` que arma una lista de tipo
`Entidad` (el padre) con un `Creeper` y un `Aldeano` adentro, y le pide a cada uno
`reaccionar()`.

El controller nunca pregunta *"¿sos un Creeper?"* con un `if`; simplemente le pide el mensaje
al tipo padre y cada objeto responde lo que le corresponde. Cuando lo probé, el JSON mostró la
reacción distinta de cada uno, lo cual fue la prueba de que el polimorfismo estaba funcionando
de verdad y no era solo un método que hacía lo mismo para todos.

Agregué al `README.md` del repositorio un diagrama con sintaxis Mermaid que muestra toda la
jerarquía: `Entidad` como clase abstracta con el método abstracto `reaccionar()`, las dos
ramas `EntidadHostil` y `EntidadPasiva`, las seis hijas concretas (`Creeper`, `Zombie`,
`Esqueleto`, `Enderman`, `Aldeano`, `Animal`) y `Jugador` heredando directo de `Entidad`.

El diagrama muestra, entre otras cosas, que `nombre` y `vida` son privados (con el símbolo
`-`), que `reaccionar()` tiene el asterisco de método abstracto, y que `curar()` es `protected`
(con el símbolo `#`). Es básicamente el mismo diseño que expliqué más arriba, pero en forma
gráfica.

## Conclusión

Lo que más me costó entender no fue Spring Boot en sí (una vez que agarrás la mecánica de
`@GetMapping` y `@RequestParam` es bastante directo), sino el tema de `protected` vs `private`
y por qué un método abstracto tiene que ir en la clase más general, aunque ahí no se pueda
implementar. Los errores de compilación por los `package` mal puestos también me sirvieron
para entender que Java es bastante estricto con que la carpeta y el paquete coincidan, cosa que
antes daba por sentada sin pensar por qué.

## POO-09 — APIs cruzadas (asistente 3: Claude Code, Claude Opus 5.5)

### Resumen de los prompts

1. **Leer el enunciado, los grupos y el ejemplo UNO de POO-09** y el estado del repositorio,
   y armar el plan: primero cerrar POO-08, después OpenAPI, clientes, escenario e Insomnia,
   cada parte en su propio commit.
2. **Cerrar la corrección de POO-08** a partir del PDF de la corrección (16 de 24). Se pidió
   cerrar cada observación y dejarla en el README con cómo se cerró:
   - sacar todas las validaciones y excepciones del dominio y moverlas a la capa `service`;
   - quitar los setters públicos de las Entity y validar en el service las combinaciones
     imposibles («Creeper detonado y con vida», muertos que reviven, experiencia de 100 o más);
   - que la simulación detone un Creeper **guardado** (por `creeperId`) sobre las entidades
     guardadas, de modo que un segundo intento dé `409` por lo que hay en H2;
   - un JSON incompleto o mal formado tiene que dar `400 DatosInvalidosException`;
   - corregir el diagrama Mermaid (controller → service → repositorio) y las tildes de todo
     el Javadoc y los comentarios. Para las tildes se usó el diccionario de español de
     hunspell como control y se revisaron a mano las palabras ambiguas (*está/esta*,
     *cómo/como*, *sí/si*, *disparo/disparó*).
3. **Publicar el OpenAPI** con springdoc y documentar cada ruta con sus errores. Se pidió que
   las dos reglas se pudieran consultar **sin modificar la base**, para que los compañeros
   las usen desde su escenario: `POST /reglas/creeper-vivo` y `POST /reglas/detonacion-unica`.
4. **Clientes hacia A2 y A3.** Un cliente HTTP con `RestClient` que solo conoce la URL base y
   la ruta (en `application.properties`) y el cuerpo que pide el OpenAPI del compañero. Se
   pidió distinguir «la regla rechazó» (4xx → `422`) de «la API no responde» (5xx o sin
   conexión → `503`), y que la respuesta diga si fue A2 o A3.
5. **Escenario «emboscada del Creeper».** Orden de consulta: mis reglas, A2, A3. Las llamadas
   HTTP fuera de la transacción y la explosión más la jugada en una sola transacción.
6. **Pruebas** con dos servidores HTTP de prueba (del JDK) que hacen de A2 y A3: feliz,
   rechazo de cada uno, compañero caído y mis reglas cortando antes de llamar a nadie.
7. **Insomnia y respuestas de ejemplo.** La colección se ejecutó entera con un script contra
   el servicio levantado. Los stubs de A2 y A3 se usaron para la pasada preliminar y las
   pruebas automáticas; las respuestas definitivas en `docs/insomnia/respuestas/` se grabaron
   contra los servicios reales de Irina y Sol por Tailscale.

### Qué revisé y decidí

- La regla de A2 (mecha corta) la tomé del README de Irina, no de su código. Su API de
  POO-08 solo aplica la regla dentro de `POST /api/minecraft/explosiones`, que crea una
  explosión en su base. Para no modificar su H2 en cada consulta, le pedí que publique la
  regla en una ruta propia que no guarde nada; mi cliente apunta a esa ruta y, si ella la
  nombra distinto en su OpenAPI, se cambia `companeros.a2.ruta` sin tocar el código.
- Las respuestas de ejemplo definitivas salen de los servicios reales de A2 y A3. Los stubs
  quedaron solo en las pruebas automáticas y en la pasada preliminar; no hay respuestas de
  ejemplo de stubs en la colección final.
- La explicación de más arriba sobre `recibirDanio()` validando el daño negativo describe
  POO-06: desde la corrección de POO-08 esa validación está en el service y el dominio solo
  cambia el estado.

## Incidente con `GET /entidades`: 500 Internal Server Error

Al probar `GET /entidades` desde Insomnia seguía devolviendo `500` con el cuerpo por defecto
de Spring:

```json
{
  "timestamp": "2026-11-21T17:57:32.733Z",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/entidades"
}
```

Ese JSON no dice la causa; la causa estaba en la consola de la aplicación. Primero apareció
`Unknown discriminator value ... : ESQUELETO` y, después de corregir eso,
`Unknown discriminator value ... : ZOMBIE`. Más tarde saltó otro:
`Null value was assigned to a property [class ...ZombieEntity.puertaRota] of primitive type`.
Tres mensajes distintos para el mismo endpoint.

### Qué significaba

La tabla `entidades` usa un discriminador (`@DiscriminatorColumn(name = "tipo")` en
`EntidadEntity`) para saber qué subclase instanciar. El proyecto solo registra tres valores:
`Creeper`, `Zombie` y `Jugador`, con esa mayúscula exacta. Como el H2 es file-based
(`jdbc:h2:file:./data/entidades`), quedaron filas viejas cargadas a mano en la consola con
valores que la jerarquía no conoce (`ESQUELETO`, `ZOMBIE`): Hibernate los leía, no encontraba
la clase y tiraba el 500. `Esqueleto` existe en el dominio, pero no es una Entity, así que
nunca pudo guardarse en esa tabla.

Lo de `puertaRota` fue la segunda capa del mismo problema: esa fila tenía la columna en
`null`, pero `ZombieEntity.puertaRota` es `boolean` primitivo y un primitivo no acepta nulos.
Lo mismo puede pasar con `detonado` (Creeper), `nivel` y `experiencia` (Jugador).

### Cómo lo solucioné

En el H2 Console (`http://localhost:8083/h2-console`, el puerto de A1 en esta entrega) revisé
los datos y corregí solo lo que estaba mal:

```sql
SELECT id, tipo, nombre FROM entidades;

DELETE FROM entidades WHERE tipo NOT IN ('Creeper', 'Zombie', 'Jugador');
UPDATE entidades SET tipo = 'Zombie' WHERE id = 2;

UPDATE entidades SET puerta_rota = FALSE WHERE tipo = 'Zombie' AND puerta_rota IS NULL;
UPDATE entidades SET detonado = FALSE WHERE tipo = 'Creeper' AND detonado IS NULL;
UPDATE entidades SET nivel = 0 WHERE tipo = 'Jugador' AND nivel IS NULL;
UPDATE entidades SET experiencia = 0 WHERE tipo = 'Jugador' AND experiencia IS NULL;

SELECT id, tipo, nombre FROM entidades WHERE tipo NOT IN ('Creeper', 'Zombie', 'Jugador');
```

La última consulta tiene que dar cero filas. No hizo falta reiniciar: el H2 Console se conecta
a la misma base que usa la aplicación. Después de eso `GET /entidades` respondió bien.

### Qué me llevé

Que un `500` con ese JSON por defecto no explica nada y hay que ir al stacktrace; que un
discriminador es sensible a mayúsculas (`ZOMBIE` no es `Zombie`); que con H2 en archivo los
datos sobreviven al reinicio y un insert viejo roto puede romper el CRUD mucho después; y que
las columnas que mapean a tipos primitivos nunca deben quedar en `null`. El CRUD actual ya
carga esos campos con valor, así que el error venía de los datos viejos, no del código.

### Cierre de los entregables de POO-09

Para cerrar la entrega completé el archivo con el enunciado aplicado
(`docs/ESPECIFICACIONES-poo-09.md`), puse los repositorios y commits reales de A2 (Irina) y A3
(María Sol Ferreira) en el README, re-exporté el OpenAPI con las rutas nuevas del escenario y
alineé el entorno de Insomnia con las variables `a1_url`, `a2_url` y `a3_url`. El enlace exacto
al commit de la solución quedó en el README y en el archivo de entrega; el commit que agrega
ese enlace es posterior al commit que contiene el código y la documentación, porque un commit
no puede contener su propio hash. También se aclaró en el mapa de A1/A2/A3 que mi entrega está
en la rama `poo-09-apis-cruzadas` y en el commit `4d09e0b`, igual que se indica la rama y el
commit de los compañeros. Después avisaron que la emboscada daba `503` porque sin variables de
entorno caía en `localhost:8082`; corregí los valores por defecto de `application.properties`
para que A2 apunte a `http://100.87.95.24:8082` y A3 a `http://100.87.131.50:8080`, y dejé esas
mismas URLs en el README y en el archivo de entrega.

