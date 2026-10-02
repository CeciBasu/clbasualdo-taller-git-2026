# Bitácora — Taller de Git y práctica POO + API REST

## Asistencia de inteligencia artificial

Se trabajó con **dos** asistentes. La rúbrica pide la marca y el modelo exacto con que se
trabajó, así que van los dos.

| # | Asistente / agente | Modelo exacto del LLM | Qué se hizo con esa herramienta |
|---|---|---|---|
| 1 | **Claude** (Anthropic) | No anotado en la sesión | Endurecer la jerarquía de POO, el refactor del proyecto a capas, los comentarios de todo el código y el diagrama Mermaid del README. |
| 2 | **OpenCode** | `big-pickle` (ID completo: `opencode/big-pickle`) | Revisión final: `docs/BITACORA.md`, `docs/ESPECIFICACIONES-poo-06.md`, la sobrecarga `Esqueleto.disparar()` y su ruta por HTTP, las pruebas nuevas, el renombre del repositorio y la actualización del README. |

Sobre la fila 1: la marca anotada es **Claude**, pero el nombre exacto del modelo con el que
se trabajó en esa sesión no quedó registrado, así que se anota como no informado en vez de
inventarlo. La fila 2 sí tiene el modelo exacto, tal como lo muestra la herramienta.


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
