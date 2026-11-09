# Minecraft API — Taller de Git 2026

API REST hecha con Spring Boot que expone el modelado de clases de Minecraft
visto en las clases (herencia, sobreescritura y ocultamiento de la
información), como parte del Taller de Git.

**Commit de la solución (POO-08):**
<https://github.com/CeciBasu/clbasualdo-taller-git-2026/commit/fbf5f524d4dad41f39d51d212347165dc89b0f8d>

La entrega anterior (POO-06) quedó documentada en
[`docs/ESPECIFICACIONES-poo-06.md`](docs/ESPECIFICACIONES-poo-06.md).

## Tecnologías

- Java 21
- Spring Boot 4 (Spring Web MVC)
- Spring Data JPA
- H2 (archivo local y consola web)
- Maven

## Cómo correr el proyecto

```bash
git clone https://github.com/CeciBasu/clbasualdo-taller-git-2026.git
cd clbasualdo-taller-git-2026/minecraft
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

Para ver la demostración por consola, sin levantar la API:

```bash
./mvnw -q compile
java -cp target/classes py.edu.uc.lp3.clbasualdo.minecraft.demo.Main
```

## Estructura

| Paquete | Qué contiene |
|---|---|
| `py.edu.uc.lp3.clbasualdo.minecraft.domain` | El modelo: `Entidad`, sus hijas y la interface `UICapable`. No depende de nada externo. |
| `py.edu.uc.lp3.clbasualdo.minecraft.entity` | La familia JPA que mapea la tabla `entidades`: `EntidadEntity` (padre) y `CreeperEntity`, `ZombieEntity`, `JugadorEntity` (hijas). `SimulacionExplosionEntity` guarda el resultado de cada explosión. |
| `py.edu.uc.lp3.clbasualdo.minecraft.repository` | El contrato para guardar y recuperar entidades. `.impl` es la versión en memoria y `.jpa` es Spring Data JPA. |
| `py.edu.uc.lp3.clbasualdo.minecraft.service` | Las reglas de negocio (crear un esqueleto, armar la lista de comportamientos, ejecutar las dos versiones de `disparar()` y simular explosiones persistidas). `.impl` tiene las implementaciones y `.entidad` el servicio del CRUD. |
| `py.edu.uc.lp3.clbasualdo.minecraft.rest.controller` | La entrada HTTP: recibe los datos, delega en los servicios y arma la respuesta. `.handler` tiene el `@RestControllerAdvice` que traduce cada excepción de dominio a su código HTTP. |
| `py.edu.uc.lp3.clbasualdo.minecraft.constants` | `ApiPaths`, con las rutas de la API en un solo lugar. |
| `py.edu.uc.lp3.clbasualdo.minecraft.exceptions` | Excepciones propias del juego: `MinecraftException` como base, `RecursoNoEncontradoException`, `DatosInvalidosException`, `CreeperMuertoException` y `CreeperYaDetonadoException`. |
| `py.edu.uc.lp3.clbasualdo.minecraft.demo` | `Main`, la demostración en consola. Vive aparte del dominio. |
| `MinecraftApplication` | Arranque de Spring Boot. Se queda en la raíz porque desde ahí sale el barrido de componentes. |

## Modelo de dominio

El modelo trata cualquier entidad de forma uniforme (moverse, recibir daño,
desaparecer, reaccionar ante el jugador) sin preguntar de qué tipo concreto
se trata. Las reglas de vida, movimiento y muerte viven en `Entidad`, la
clase base: cada subclase solo redefine lo que realmente cambia.

`nombre` y `vida` son `private`: nadie fuera de la jerarquía puede dejar una
entidad en un estado inválido, ni siquiera las propias hijas. El estado
cambia únicamente a través de mensajes (`recibirDanio`, `curar`) que
validan lo que reciben. `curar()` es `protected` porque no es una operación
que cualquier entidad deba poder recibir desde afuera (un `Creeper` no se
regenera): es una herramienta interna de la jerarquía que usa `Jugador`.

`reaccionar()` es un método abstracto declarado en `Entidad`: expresa un
comportamiento que todas las hijas deben saber responder, pero cuya forma
concreta no puede escribir el padre porque cada tipo se comporta distinto
(un `Creeper` explota, un `Esqueleto` dispara, un `Enderman` se teletransporta,
un `Aldeano` huye).

```mermaid
classDiagram
    class Entidad {
        <<abstract>>
        -String nombre
        -int vida
        -int vidaMaxima
        -double x
        -double y
        -double z
        +getNombre() String
        +getVida() int
        +getVidaMaxima() int
        +getX() double
        +getY() double
        +getZ() double
        +estaVivo() bool
        +recibirDanio(int danio)
        #curar(int cantidad)
        +moverse()
        +moverse(double dx, double dy, double dz)
        +teletransportar(double x, double y, double z)
        +distanciaHacia(Entidad otra) double
        +mostrarInfo()
        +desaparecer()
        +reaccionar()* String
    }

    class EntidadHostil {
        <<abstract>>
        -int danioAtaque
        -double rangoAtaque
        +getDanioAtaque() int
        +getRangoAtaque() double
        +golpear(Entidad objetivo)
        +atacar()*
    }

    class EntidadPasiva {
        <<abstract>>
        -DISTANCIA_SEGURA
        +huir()
        +huir(Entidad amenaza)
        +estaEnPeligro(Entidad amenaza) bool
    }

    class Creeper {
        -RADIO_EXPLOSION
        -boolean detonado
        +atacar()
        +explotar(List~Entidad~ cercanas)
        +isDetonado() bool
        +reaccionar() String
    }

    class Zombie {
        -boolean puertaRota
        +atacar()
        +romperPuerta()
        +isPuertaRota() bool
        +reaccionar() String
    }

    class Esqueleto {
        -int flechas
        +atacar()
        +disparar() String
        +disparar(double distancia) String
        +recargar(int cantidad)
        +getFlechas() int
        +reaccionar() String
    }

    class Enderman {
        +atacar()
        +teletransportarse()
        +reaccionar() String
    }

    class Aldeano {
        -String profesion
        -int esmeraldas
        +comerciar()
        +comerciar(int esmeraldasOfrecidas)
        +getProfesion() String
        +getEsmeraldas() int
        +reaccionar() String
    }

    class Animal {
        -String especie
        -boolean domesticado
        +comer()
        +comer(int puntosAlimento)
        +domesticar()
        +reproducirse(Animal pareja) Animal
        +isDomesticado() bool
        +getEspecie() String
        +reaccionar() String
    }

    class Jugador {
        -DANIO_BASE
        -RANGO_ATAQUE
        -int nivel
        -int experiencia
        -List~String~ inventario
        +atacar(Entidad objetivo)
        +construir()
        +construir(String bloque)
        +regenerar(int cantidad)
        +agregarItem(String item)
        +usarItem(String item) bool
        +ganarExperiencia(int puntos)
        +mostrarNivel()
        +getNivel() int
        +getExperiencia() int
        +getInventario() List~String~
        +reaccionar() String
    }

    Entidad <|-- EntidadHostil
    Entidad <|-- EntidadPasiva
    Entidad <|-- Jugador
    EntidadHostil <|-- Creeper
    EntidadHostil <|-- Zombie
    EntidadHostil <|-- Esqueleto
    EntidadHostil <|-- Enderman
    EntidadPasiva <|-- Aldeano
    EntidadPasiva <|-- Animal
```

## Sobrecarga y sobreescritura

Las dos están en el modelo, y conviene no confundirlas: son mecanismos distintos que se
resuelven en momentos distintos de la ejecución.

### Qué se agregó

**Sobreescritura.** `Entidad` declara el método abstracto

```java
public abstract String reaccionar();
```

porque sabe que toda entidad tiene que saber responder ante el jugador, pero no sabe *cómo*:
eso solo lo sabe cada tipo. Lo sobreescriben las siete clases concretas con la misma firma
exacta y su propio cuerpo: `Zombie`, `Esqueleto`, `Creeper`, `Enderman`, `Aldeano`, `Animal` y
`Jugador`. Lo mismo pasa con `EntidadHostil.atacar()`, abstracto, que sobreescriben `Creeper`,
`Zombie`, `Esqueleto` y `Enderman`.

```java
// Entidad.java — la base no puede resolverlo
public abstract String reaccionar();

// Creeper.java — misma firma, cuerpo propio
@Override
public String reaccionar() {
    return getNombre() + " se acerca silbando y está a punto de explotar.";
}

// Aldeano.java — misma firma, otro cuerpo
@Override
public String reaccionar() {
    return getNombre() + " se asusta y corre a esconderse.";
}
```

**Sobrecarga.** El mismo nombre, otra lista de argumentos, todo en la misma clase. La más
nueva es `Esqueleto.disparar()`, que es el caso que pide la consigna: la misma acción de
disparar, con y sin distancia.

```java
// Sin datos: no hay nada que revisar, siempre gasta flecha si le queda alguna.
public String disparar() {
    return disparar(0.0);
}

// Con datos: aparece el contexto. Fuera del rango la flecha se pierde y no se gasta,
// y una distancia negativa se rechaza.
public String disparar(double distancia) { /* ... */ }
```

Las demás sobrecargas del modelo, para que se vea que el patrón ya venía usándose:

| Clase | Versión simple | Versión sobrecargada |
|---|---|---|
| `Esqueleto` | `disparar()` | `disparar(double distancia)` |
| `Entidad` | `moverse()` | `moverse(double dx, double dy, double dz)` |
| `EntidadPasiva` | `huir()` | `huir(Entidad amenaza)` |
| `Aldeano` | `comerciar()` | `comerciar(int esmeraldasOfrecidas)` |
| `Animal` | `comer()` | `comer(int puntosAlimento)` |
| `Jugador` | `construir()` | `construir(String bloque)` |

### Cómo se distingue una de la otra

|  | Sobrecarga | Sobreescritura |
|---|---|---|
| **Dónde** | En la misma clase | En clases distintas de la jerarquía |
| **Qué cambia** | La lista de argumentos | El cuerpo, con la misma firma |
| **Cómo se marca** | No lleva nada especial | Con `@Override` |
| **Cuándo se resuelve** | Al compilar, por la firma | Al ejecutar, por el tipo real del objeto |
| **Para qué sirve** | Darle una versión corta a una acción que tiene una versión completa | Cambiar un comportamiento que el padre no puede saber |

La diferencia clave: si `ComportamientoController` recorre una `List<Entidad>` y llama a
`reaccionar()`, Java no sabe de antemano qué clase es cada elemento. Eso es sobreescritura,
y funciona sin un solo `if` por tipo. En cambio, cuando dentro de `Esqueleto` se llama a
`disparar(5.0)`, el compilador ya sabe que es la versión con `double`: eso es sobrecarga, y
no tiene nada que ver con la herencia.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/` | Confirma que el servicio está vivo. |
| `GET` | `/esqueleto?nombre=...&vida=...` | Construye un `Esqueleto` con los parámetros de la URL y devuelve su estado en JSON. |
| `GET` | `/esqueleto/disparar` | Muestra la **sobrecarga** de `disparar()` en acción: crea un `Esqueleto` desde la URL, ejecuta `disparar()` y `disparar(double distancia)` y devuelve los dos resultados en el mismo JSON. |
| `GET` | `/comportamiento` | Devuelve, en JSON, la reacción de una entidad de cada tipo frente al jugador. Todas se tratan como `Entidad`: no hay ningún `if` por tipo, cada objeto informa lo suyo. |
| `GET` | `/ui` | Devuelve los datos de las entidades que implementan la interface `UICapable`. |
| `GET` | `/entidades` | Lista todas las entidades persistidas en H2. |
| `POST` | `/entidades` | Crea una entidad y la guarda en H2. El JSON lleva `"tipo"` (`Creeper`, `Zombie` o `Jugador`). |
| `GET` | `/entidades/{id}` | Busca una entidad por id; si no existe responde `404`. |
| `GET` | `/entidades/nombre/{nombre}` | Busca una entidad por nombre; si no existe responde `404`. |
| `PUT` | `/entidades/{id}` | Actualiza una entidad por id; no permite cambiar su subtipo. |
| `PUT` | `/entidades/nombre/{nombre}` | Actualiza una entidad por nombre. |
| `DELETE` | `/entidades/{id}` | Elimina una entidad por id; responde `204`. |
| `DELETE` | `/entidades/nombre/{nombre}` | Elimina una entidad por nombre; responde `204`. |
| `POST` | `/simulacion/explosion` | Ejecuta y persiste la simulación de explosión entre un `Creeper`, un `Zombie` y un `Jugador`. Responde `201`. |
| `GET` | `/simulacion/explosion` | Devuelve la última simulación persistida. |
| `GET` | `/simulacion/explosion/{id}` | Devuelve una simulación persistida por id; si no existe responde `404`. |
| `GET` | `/simulacion/explosiones` | Lista todas las simulaciones persistidas. |

### Ejemplo — `GET /esqueleto?nombre=Bony&vida=15`

```json
{
  "nombre": "Bony",
  "vida": 15,
  "vivo": true,
  "flechas": 16,
  "posicion": { "x": 0.0, "y": 64.0, "z": 0.0 }
}
```

Los parámetros `x`, `y` y `z` son opcionales y por defecto `(0, 64, 0)`. Si un dato no
es válido, la API responde `400` con el mensaje del dominio, sin tirar la excepción:

```json
{ "error": "La vida inicial debe ser mayor a 0." }
```

### Ejemplo — `GET /esqueleto/disparar?nombre=Bony&distancia=5`

Acá se ve la sobrecarga: las dos versiones del mismo mensaje, en el mismo JSON.

```json
{
  "nombre": "Bony",
  "flechasIniciales": 16,
  "flechasRestantes": 14,
  "rango": 8.0,
  "distanciaPedida": 5.0,
  "disparoSinDistancia": "Bony dispara una flecha a 0.0. Le quedan 15.",
  "disparoConDistancia": "Bony dispara una flecha a 5.0. Le quedan 14."
}
```

Y con una distancia fuera del rango se ve la diferencia entre las dos versiones: el disparo
sin distancia gasta la flecha igual, el que lleva distancia no, porque la flecha se pierde.

```json
{
  "nombre": "Bony",
  "flechasIniciales": 16,
  "flechasRestantes": 15,
  "rango": 8.0,
  "distanciaPedida": 20.0,
  "disparoSinDistancia": "Bony dispara una flecha a 0.0. Le quedan 15.",
  "disparoConDistancia": "Bony no alcanza a disparar a 20.0 (rango 8.0) y no gasta flecha."
}
```

Una distancia negativa la rechaza el dominio y la API la avisa con `400`:

```json
{ "error": "La distancia no puede ser negativa." }
```

### Ejemplo — `GET /comportamiento`

```json
[
  {
    "tipo": "Zombie",
    "reaccion": "Zombie gruñe y persigue al jugador.",
    "vida": 20,
    "posicion": { "x": 0.0, "y": 0.0, "z": 0.0 }
  },
  {
    "tipo": "Esqueleto",
    "reaccion": "Esqueleto retrocede y dispara una flecha.",
    "vida": 20,
    "posicion": { "x": 0.0, "y": 0.0, "z": 0.0 }
  },
  {
    "tipo": "Creeper",
    "reaccion": "Creeper se acerca silbando y está a punto de explotar.",
    "vida": 20,
    "posicion": { "x": 0.0, "y": 0.0, "z": 0.0 }
  },
  {
    "tipo": "Enderman",
    "reaccion": "Enderman se teletransporta al ser observado.",
    "vida": 40,
    "posicion": { "x": 0.0, "y": 0.0, "z": 0.0 }
  },
  {
    "tipo": "Aldeano",
    "reaccion": "Aldeano se asusta y corre a esconderse.",
    "vida": 20,
    "posicion": { "x": 0.0, "y": 0.0, "z": 0.0 }
  },
  {
    "tipo": "Animal",
    "reaccion": "Vaca sigue comiendo, no le presta atencion al jugador.",
    "vida": 10,
    "posicion": { "x": 0.0, "y": 0.0, "z": 0.0 }
  }
]
```

## Persistencia H2 y CRUD

La familia persistida tiene un padre y tres hijas. El mapeo usa una sola tabla con un
discriminador, así que las filas de distinto subtipo comparten los datos comunes y cada una
aporta sus columnas propias:

```text
EntidadEntity (abstracta, tabla ENTIDADES, columna TIPO)
├── CreeperEntity   -> TIPO = "Creeper"  (detonado)
├── ZombieEntity    -> TIPO = "Zombie"   (puertaRota)
└── JugadorEntity   -> TIPO = "Jugador"  (nivel, experiencia)
```

En el JSON, `@JsonTypeInfo` y `@JsonSubTypes` usan la propiedad `"tipo"` para elegir la clase
concreta. Por eso un mismo `POST /entidades` puede crear cualquiera de los tres subtipos, y un
`GET` devuelve el objeto con su tipo y sus datos propios. El CRUD completo es:

- `POST /entidades`: crea y persiste una entidad; responde `201`.
- `GET /entidades`, `GET /entidades/{id}` y `GET /entidades/nombre/{nombre}`: consultan.
- `PUT /entidades/{id}` y `PUT /entidades/nombre/{nombre}`: actualizan sin permitir cambiar el subtipo.
- `DELETE /entidades/{id}` y `DELETE /entidades/nombre/{nombre}`: eliminan; responden `204`.

Invariantes que aplica el servicio antes de guardar:

- nombre obligatorio y único;
- `vidaMaxima > 0` (si no viene, se toma igual a `vida`);
- `vida >= 0` y `vida <= vidaMaxima`;
- al crear, `vida > 0`;
- el subtipo de una fila existente no cambia en un `PUT`;
- en un `Jugador`, `nivel` y `experiencia` no pueden ser negativos.

La configuración está en `minecraft/src/main/resources/application.properties` y apunta a
`jdbc:h2:file:./data/entidades`, con `ddl-auto=update`. La consola H2 queda disponible en:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/entidades`
- Usuario: `sa`
- Contraseña: vacía
- Tablas: `ENTIDADES` y `SIMULACIONES_EXPLOSION`

La ruta relativa `./data/entidades` se resuelve desde la carpeta `minecraft`, que es donde se
ejecuta Maven. Esa carpeta está ignorada en `.gitignore` porque es un archivo local generado.
Las pruebas usan una base H2 en memoria (`src/test/resources/application.properties`) para no
bloquear ni modificar la base real del proyecto.

## Simulación de explosiones

La simulación usa tres clases del modelo y después guarda el estado final de las tres:

| Clase | Rol en la simulación |
|---|---|
| `Creeper` | Es la fuente de la explosión. Se autodestruye y daña a las entidades dentro de su radio de 3 bloques. |
| `Zombie` | Entidad hostil ubicada a 2 bloques del Creeper: queda dentro del radio y recibe 25 de daño. |
| `Jugador` | Entidad ubicada a 5 bloques del Creeper: queda fuera del radio y no recibe daño. |

La lista de alcanzables se maneja como `List<Entidad>`, así que la explosión no pregunta por el
tipo concreto de cada víctima: recorre la lista y cada objeto responde con su propia vida y
posición. La lógica de la detonación está en `Creeper.detonar(...)`. El servicio
`SimulacionExplosionServiceImpl` arma el escenario, ejecuta la regla, persiste el estado final
de las tres entidades (`Creeper Simulado`, `Zombie Simulado` y `Steve Simulado`) y guarda una
fila en `SIMULACIONES_EXPLOSION` con la foto completa del resultado.

### Las dos reglas del dominio

1. **Un Creeper solo puede detonar si está vivo:** si recibe daño letal antes de la explosión, el dominio rechaza la detonación con `CreeperMuertoException`.
2. **Un Creeper solo puede detonar una vez:** un segundo intento sobre el mismo Creeper se rechaza con `CreeperYaDetonadoException`.

Las dos reglas ya existían en el comportamiento de `Creeper.explotar(...)`; la nueva
`Creeper.detonar(...)` las hace explícitas con excepciones de dominio propias sin cambiar el
comportamiento anterior de `explotar(...)`.

### Endpoints

| Método | Ruta | Uso |
|---|---|---|
| `POST` | `/simulacion/explosion` | Ejecuta y persiste la simulación; responde `201`. |
| `GET` | `/simulacion/explosion` | Devuelve la última simulación guardada. |
| `GET` | `/simulacion/explosion/{id}` | Devuelve una simulación por id; si no existe responde `404`. |
| `GET` | `/simulacion/explosiones` | Lista todas las simulaciones guardadas. |

Parámetros del `POST`:

| Parámetro | Por defecto | Uso |
|---|---|---|
| `vidaCreeper` | `20` | Vida inicial del Creeper. Con `0` o menos se prueba la regla del Creeper muerto. |
| `repetirDetonacion` | `false` | Con `true` se simula un segundo intento sobre el mismo Creeper para probar la regla de una sola detonación. |

Ejemplo exitoso:

```bash
curl -X POST http://localhost:8080/simulacion/explosion
```

```json
{
  "simulacionId": 1,
  "fecha": "2026-11-09T20:24:26.123",
  "radioExplosion": 3.0,
  "creeper": {
    "nombre": "Creeper Simulado",
    "vidaInicial": 20,
    "vidaFinal": 0,
    "detonado": true,
    "vivo": false,
    "x": 0.0,
    "y": 64.0,
    "z": 0.0
  },
  "afectados": [
    {
      "tipo": "Zombie",
      "nombre": "Zombie Simulado",
      "vidaInicial": 20,
      "vidaFinal": 0,
      "distanciaAlCreeper": 2.0,
      "alcanzada": true
    }
  ],
  "noAfectados": [
    {
      "tipo": "Jugador",
      "nombre": "Steve Simulado",
      "vidaInicial": 20,
      "vidaFinal": 20,
      "distanciaAlCreeper": 5.0,
      "alcanzada": false
    }
  ]
}
```

Después del `POST`, un `GET /simulacion/explosion/{simulacionId}` devuelve el mismo resultado
leído de H2, y `GET /entidades/nombre/Creeper Simulado` muestra que la entidad persistida quedó
con vida `0` y `detonado: true`.

### Jerarquía de excepciones y handlers

```text
MinecraftException
├── RecursoNoEncontradoException     -> HTTP 404
├── DatosInvalidosException          -> HTTP 400
├── CreeperMuertoException           -> HTTP 422
└── CreeperYaDetonadoException       -> HTTP 409
```

`MinecraftExceptionHandler` usa `@RestControllerAdvice` y tiene un `@ExceptionHandler` por cada
excepción concreta. No hay un handler genérico de `Exception`, para que un error de programación
no se convierta en un `400` y siga viéndose como un `500`. Todas las respuestas de error tienen
la misma forma (`timestamp`, `status`, `error`, `tipo`, `mensaje`).

Creeper muerto (`POST /simulacion/explosion?vidaCreeper=0`):

```json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "tipo": "CreeperMuertoException",
  "mensaje": "Creeper no puede detonar: está muerto."
}
```

Creeper ya detonado (`POST /simulacion/explosion?repetirDetonacion=true`):

```json
{
  "status": 409,
  "error": "Conflict",
  "tipo": "CreeperYaDetonadoException",
  "mensaje": "Creeper ya detonó y no puede volver a explotar."
}
```

Datos inválidos (`POST /entidades` con `vida` negativa) responde `400` con
`"tipo": "DatosInvalidosException"`, y un id inexistente responde `404` con
`"tipo": "RecursoNoEncontradoException"`. La colección de Insomnia tiene un pedido para cada
uno de esos casos.

### Interface `UICapable`

`UICapable` es una interface del dominio que declara los datos que una entidad puede mostrar en
una interfaz gráfica. La implementan `Creeper`, `Zombie`, `Esqueleto`, `Enderman`, `Aldeano`,
`Animal` y `Jugador`. `GET /ui` arma una `List<UICapable>` y le pide los datos a cada elemento:
el controller no pregunta por el tipo concreto y cada clase responde lo suyo.

## Preguntas para pensar

**¿Por qué separar `domain` de `entity`?**

Porque el dominio modela el juego y no debe depender de JPA ni de Jackson. `Entidad` y sus
hijas sirven para el demo de consola y para la simulación, mientras que `EntidadEntity` y sus
hijas son el mapeo de la tabla. Así las reglas del juego no se mezclan con detalles de
persistencia.

**¿Por qué `SINGLE_TABLE` y no una tabla por clase?**

Porque las tres entidades comparten casi todos los datos y la API las trata como una sola
familia en `/entidades`. Una tabla con un discriminador `tipo` permite listar todo junto y
devolver el subtipo correcto sin hacer uniones. El costo es que las columnas propias de cada
subtipo quedan nulas en las otras filas, algo aceptable para este tamaño.

**¿Por qué la simulación guarda también las entidades y no solo el resultado?**

Porque el pedido es que la simulación se pueda consultar después y que el estado final de las
entidades quede persistido. Guardar la foto en `SIMULACIONES_EXPLOSION` permite responder un
`GET` sin volver a ejecutar la explosión, y guardar las entidades permite ver el efecto en el
CRUD.

**¿Por qué una excepción por regla y no un `if` que devuelva `400`?**

Porque cada error tiene un significado distinto: un recurso inexistente es `404`, un dato
inválido es `400`, un Creeper muerto es `422` y una segunda detonación es `409`. La jerarquía
`MinecraftException` permite que el dominio lance la excepción correcta y que el handler decida
el código HTTP, sin que el controller conozca las reglas.

**¿Qué pasa si el controller arma el JSON a mano?**

Se pierde el polimorfismo: habría que preguntar por el tipo con `if` o `instanceof`. Con
`@JsonTypeInfo` y `@JsonSubTypes`, Jackson elige la clase concreta a partir de `"tipo"` y el
controller solo devuelve `EntidadEntity`.

## Colección Insomnia

La colección exportable está en
[`docs/insomnia/minecraft-explosiones.json`](docs/insomnia/minecraft-explosiones.json).

Para importarla:

1. Abrir Insomnia.
2. Ir a `Application` → `Import` → `From File`.
3. Elegir `docs/insomnia/minecraft-explosiones.json`.
4. Seleccionar el environment `Base local` y verificar que `base_url` sea
   `http://localhost:8080`.
5. Arrancar la aplicación con `./mvnw spring-boot:run` y ejecutar las solicitudes en orden.

La colección incluye:

- simulación exitosa y sus tres consultas posteriores (`GET` por id, última y lista);
- error de Creeper muerto (`422`);
- error de Creeper ya detonado (`409`);
- CRUD: listar, crear, obtener por nombre, actualizar por nombre y eliminar por nombre;
- POST ilegal (`400`, `DatosInvalidosException`);
- id inexistente (`404`, `RecursoNoEncontradoException`);
- `GET /ui` para ver la interface `UICapable` en acción.

Para el CRUD, si se ejecuta `Crear entidad` más de una vez, el nombre duplicado responde `400`.
Eliminar la entidad antes de volver a crearla, o cambiar `entidad_nombre` en el environment.
Para `Consultar simulación por id`, copiar el `simulacionId` que devuelve la simulación exitosa
en la variable `simulacion_id` del environment.

**Aclaración de verificación:** la colección se entrega lista para importar, pero en esta
entrega las pruebas automatizadas se ejecutaron con Maven y MockMvc, y la aplicación se verificó
con `curl` y una consulta directa a H2. No se afirma que las solicitudes se hayan ejecutado desde
la interfaz de Insomnia.

## Pruebas

```bash
./mvnw test
```

67 pruebas. Sobre el dominio: los límites de vida y el ocultamiento de estado
(`EntidadTest`), el radio y la unicidad de la explosión (`CreeperTest`), la huida y
el borde exacto de peligro (`EntidadPasivaTest`), la munición, el alcance del
esqueleto y las dos versiones de `disparar()` (`EsqueletoTest`), el ataque y la reacción
del Enderman (`EndermanTest`), el inventario y la experiencia (`JugadorTest`), y que la
jerarquía se use sin preguntar por el tipo (`PolimorfismoTest`). Sobre la entrada HTTP,
`ApiControllerTest` levanta el contexto con MockMvc y prueba las rutas anteriores, incluido
que un dato inválido termine en `400` y no en un `500`.

Las pruebas de esta entrega son:

- `SimulacionExplosionServiceImplTest`: caso exitoso persistido y las dos excepciones de dominio.
- `SimulacionExplosionControllerTest`: POST exitoso, GET por id, última, lista, `422` y `409` con su JSON.
- `EntidadCrudControllerTest`: CRUD completo, subtipos, POST ilegal, id inexistente y cambio de tipo prohibido.

## Bitácora y documentación

- **[`docs/BITACORA.md`](docs/BITACORA.md)** — bitácora de la entrega: con qué asistentes y
  qué modelos de LLM se trabajó, el resumen de los prompts, y el relato de cómo se fue
  tocando la jerarquía.
- **[`docs/ESPECIFICACIONES-poo-08.md`](docs/ESPECIFICACIONES-poo-08.md)** — consignas de
  POO-08 y dónde se cumple cada una.

## Resumen de chat con el agente

**1. Endurecimiento del proyecto Minecraft (POO)**
Mejoramos toda la jerarquía de clases para que dejara de ser "solo prints" y tuviera comportamiento real:
- **`Entidad`**: se le agregaron coordenadas `x, y, z`, métodos de movimiento (`moverse`, `teletransportar`), cálculo de distancia entre entidades (`distanciaHacia`), `vidaMaxima`, y `toString()`/`equals()`/`hashCode()`.
- **`EntidadHostil`**: ataque real con `danioAtaque` y `rangoAtaque` — el método `golpear(objetivo)` valida rango y vida antes de aplicar daño.
- **`EntidadPasiva`**: huida real (`huir(amenaza)`) que se mueve en base a la posición de la amenaza, y detección de peligro (`estaEnPeligro`).
- **Subclases específicas**: `Zombie` rompe puertas, `Esqueleto` tiene munición de flechas y recarga, `Creeper` explota en área y se autodestruye, `Aldeano` comercia con esmeraldas, `Animal` se domestica y reproduce.
- **`Jugador`**: inventario, experiencia con subida de nivel, y ataque real con chequeo de rango.
- **Controllers**: `EsqueletoController` ahora acepta posición por parámetros de URL y devuelve error 400 con `ResponseEntity` ante datos inválidos; `ComportamientoController` devuelve vida y posición de más entidades.
- Te entregué todos los `.java` actualizados más un `.zip` con la estructura de paquetes lista para copiar.

**2. Cómo probar la API**
EL agente me explico que con la app corriendo (`MinecraftApplication`), se accede por `http://localhost:8080/`, `/comportamiento` y `/esqueleto` (este último acepta `?nombre=&vida=&x=&y=&z=`), y que no hace falta ningún otro link salvo que quiera exponerlo fuera de mi red local.

**3. Git: ramas divergentes**
Resolvimos un caso real de mi repo donde mi rama local y `origin/main` habían divergido (mi commit de las entidades vs. 2 commits de README subidos por mi compañera). EL agente me explicò por qué pasó, y la solución fue `git pull --rebase origin main` para reordenar mi commit arriba de los del remoto sin conflictos, seguido de `git push`.

**4. README, diagrama de clases y comentarios en el código** 
Armamos el README explicando el código y agregamos el diagrama de clases. Además, me ayudo a comentar todo el código(como para que pudiera entender mejor todo lo que iba haciendo).

**5. Estado y entrega**
- Verificamos que `main` remoto quedaba igual a mi local tras el push.
- Cloné el repo desde cero (como el corrector) y confirmé que compila y arranca sin `target/` ni archivo basura.

**6. Enderman / PR**
- Corregí el Enderman (`super("Enderman", 40, 7, 16.0)`) y lo sumé a `/comportamiento`.
- Lo dejé en la rama de la compañera; y mergee el PR #1 (fast-forward, sin conflictos).
- Agregué `EndermanTest` (5 tests) → total 44 tests.

**7. Documentación**
- Actualicé el README: Enderman en el diagrama, 6.ª entidad en el ejemplo, y "44 pruebas".
- Commiteado y subido.

**8. Refactor estilo template (lo que pediste)**
- El agente movio el proyecto a capas: `domain`, `repository` (+impl), `service` (+impl), `rest.controller`, `constants` y `exceptions`.
- Dejo `MinecraftApplication` en la raíz y el dominio puro (sin Spring).
- Movio los tests del dominio a su paquete espejo.
- Todo commiteado y subido en `a1ca752`.

**9. Verificaciones que pasaron**
- `./mvnw test` → 44 tests, BUILD SUCCESS.
- Endpoints iguales: `/`, `/comportamiento` (6 entidades), `/esqueleto` y el error 400 de `vida=0`.
- El demo de consola (`Main`) corre bien.

**10. Rúbricas**
- Paquetes mejoró con el refactor; el resto se mantuvo OK.

**Después de este resumen (revisión final).** Se agregó la sobrecarga
`Esqueleto.disparar()` con su ruta `/esqueleto/disparar`, el apartado de sobrecarga y
sobreescritura de este README y las pruebas de la entrada HTTP. Las pruebas pasaron de
44 a **56**. El detalle de esa revisión está en [`docs/BITACORA.md`](docs/BITACORA.md).

**11. Nueva consigna: explosiones, excepciones, H2 e Insomnia**
- Se mantuvo el CRUD de `/entidades` con Spring Data JPA y H2.
- Se agregó la simulación de explosiones con `Creeper`, `Zombie` y `Jugador` en
  `POST /simulacion/explosion`.
- Se formalizaron las dos reglas del Creeper con `CreeperMuertoException` y
  `CreeperYaDetonadoException`, atendidas por `MinecraftExceptionHandler`.
- Se agregaron pruebas de simulación, de los dos errores y del CRUD completo contra H2.
- Se agregó la colección exportable de Insomnia y las instrucciones de importación.

**12. POO-08: familia JPA, simulaciones persistidas y documentación**
- La tabla `ENTIDADES` pasó a una familia `SINGLE_TABLE` con `EntidadEntity` como padre y
  `CreeperEntity`, `ZombieEntity` y `JugadorEntity` como hijas.
- El CRUD de `/entidades` valida los invariantes, no permite cambiar el subtipo y usa
  `@JsonTypeInfo` / `@JsonSubTypes` para discriminar el `"tipo"` en el JSON.
- La simulación persiste el estado final de las tres entidades y guarda una foto en
  `SIMULACIONES_EXPLOSION`; se agregaron `GET` por id, última y lista.
- La jerarquía de excepciones quedó con `MinecraftException` como base y un handler por tipo:
  `404`, `400`, `422` y `409`.
- La colección de Insomnia cubre el CRUD, `/ui`, la simulación, un POST ilegal, un id
  inexistente y cada excepción hija.
- Se agregaron `docs/ESPECIFICACIONES-poo-08.md` y el detalle de esta entrega en la bitácora.
- `./mvnw test` → **67 pruebas**, BUILD SUCCESS.

## Licencia

Este proyecto está bajo licencia Apache 2.0. Ver [LICENSE](./LICENSE).
