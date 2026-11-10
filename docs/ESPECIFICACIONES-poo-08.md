# Especificaciones — POO-08 aplicado al dominio Minecraft

**Ejercicio:** POO-08 — persistencia, CRUD, reglas de juego, excepciones e interface
**Dominio elegido:** Minecraft
**Alumno:** Cecilia Basualdo
**Repositorio:** <https://github.com/CeciBasu/clbasualdo-taller-git-2026>
**Commit de la solución:** <https://github.com/CeciBasu/clbasualdo-taller-git-2026/commit/abd1c2b409369b2ca1330f4c309283be07fbe179>

---

## Objetivo

Extender la API de Minecraft de las entregas anteriores para que:

- persista en H2 una **familia** de entidades (un padre y al menos dos hijas);
- exponga un **CRUD REST completo** sobre esa familia;
- modele **dos reglas de juego** con excepciones propias y las ejecute en una
  **simulación persistida**;
- traduzca cada excepción de dominio a un **código HTTP distinto** con un handler por tipo;
- muestre el uso de una **interface de dominio** (`UICapable`);
- entregue una **colección de Insomnia** y la documentación de la entrega.

## Arquitectura

El proyecto mantiene separadas tres capas:

| Capa | Paquete | Responsabilidad |
|---|---|---|
| Dominio | `…minecraft.domain` | `Entidad`, sus hijas y `UICapable`. No depende de Spring, JPA ni Jackson. |
| Persistencia | `…minecraft.entity` y `…minecraft.repository.jpa` | La familia JPA y los repositorios de Spring Data. |
| Servicios y HTTP | `…minecraft.service`, `…minecraft.rest.controller` | Reglas de negocio, entrada HTTP y traducción de errores. |

El dominio no se reemplazó por las entidades JPA: `Creeper`, `Zombie` y `Jugador` siguen
existiendo para la simulación y el demo de consola. `CreeperEntity`, `ZombieEntity` y
`JugadorEntity` son el mapeo de la tabla y tienen los getters/setters que necesita JPA/Jackson.

## 1. Familia persistida en H2

La familia usa una sola tabla con un discriminador:

```text
EntidadEntity (abstracta, tabla ENTIDADES, columna TIPO)
├── CreeperEntity   -> TIPO = "Creeper"  (detonado)
├── ZombieEntity    -> TIPO = "Zombie"   (puertaRota)
└── JugadorEntity   -> TIPO = "Jugador"  (nivel, experiencia)
```

Decisiones:

- `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)` porque las tres comparten casi todos
  los datos y la API las lista juntas.
- `@DiscriminatorColumn(name = "tipo")` para saber qué subtipo representa cada fila.
- `@JsonTypeInfo` y `@JsonSubTypes` para que el JSON de entrada y salida use `"tipo"` con los
  nombres `Creeper`, `Zombie` y `Jugador`.
- Los constructores vacíos son `protected` para que JPA/Jackson puedan usarlos sin abrir la
  creación de entidades sin datos de negocio al resto del código.

La configuración de H2 está en `minecraft/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:file:./data/entidades
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

`minecraft/data/` está en `.gitignore` porque es un archivo local generado. Las pruebas usan
H2 en memoria (`src/test/resources/application.properties`).

## 2. CRUD REST completo

`EntidadController` expone el CRUD en `/entidades`:

| Método | Ruta | Resultado |
|---|---|---|
| `POST` | `/entidades` | Crea una entidad; responde `201` con el id. |
| `GET` | `/entidades` | Lista todas. |
| `GET` | `/entidades/{id}` | Busca por id; `404` si no existe. |
| `GET` | `/entidades/nombre/{nombre}` | Busca por nombre; `404` si no existe. |
| `PUT` | `/entidades/{id}` | Actualiza por id. |
| `PUT` | `/entidades/nombre/{nombre}` | Actualiza por nombre. |
| `DELETE` | `/entidades/{id}` | Elimina; responde `204`. |
| `DELETE` | `/entidades/nombre/{nombre}` | Elimina por nombre; responde `204`. |

Invariantes que aplica `EntidadCrudServiceImpl` antes de guardar:

- nombre obligatorio y único;
- `vidaMaxima > 0`; si no viene, se toma igual a `vida`;
- `vida >= 0` y `vida <= vidaMaxima`;
- al crear, `vida > 0`;
- un `PUT` no puede cambiar el subtipo de la fila;
- en un `Jugador`, `nivel` y `experiencia` no pueden ser negativos.

Un POST ilegal no agranda la colección: el servicio valida antes de llamar a `save(...)` y el
handler responde `400`.

## 3. Las dos reglas de juego

Las reglas viven en el dominio, en `Creeper.detonar(List<Entidad>)`:

1. **Solo puede detonar si está vivo.** Si la vida llegó a cero, lanza
   `CreeperMuertoException`.
2. **Solo puede detonar una vez.** Si `detonado` ya es `true`, lanza
   `CreeperYaDetonadoException`.

`detonar(...)` reutiliza el comportamiento anterior de `explotar(...)`, así que no se cambió la
forma en que la explosión daña a las entidades dentro del radio.

## 4. Simulación persistida

`SimulacionExplosionServiceImpl` arma el escenario:

| Entidad | Posición | Resultado |
|---|---|---|
| `Creeper` | `(0, 64, 0)` | Detona y queda con vida `0`. |
| `Zombie` | `(2, 64, 0)` | Dentro del radio de 3; recibe 25 de daño y muere. |
| `Jugador` | `(5, 64, 0)` | Fuera del radio; queda con vida `20`. |

Después de ejecutar el dominio, el servicio:

- guarda o actualiza `Creeper Simulado`, `Zombie Simulado` y `Steve Simulado` en `ENTIDADES`;
- guarda una foto completa en `SIMULACIONES_EXPLOSION` con ids, nombres, vidas, distancias,
  radio y fecha;
- devuelve un `SimulacionExplosionResultado` con `simulacionId` y `fecha`.

Endpoints:

| Método | Ruta | Uso |
|---|---|---|
| `POST` | `/simulacion/explosion` | Ejecuta y persiste; responde `201`. |
| `GET` | `/simulacion/explosion` | Última simulación guardada. |
| `GET` | `/simulacion/explosion/{id}` | Simulación por id; `404` si no existe. |
| `GET` | `/simulacion/explosiones` | Lista todas las simulaciones. |

Parámetros del POST:

- `vidaCreeper` (por defecto `20`): con `0` se prueba la regla del Creeper muerto.
- `repetirDetonacion` (por defecto `false`): con `true` se prueba la regla de una sola
  detonación.

Si una regla falla, la transacción no deja una simulación nueva en la base.

## 5. Jerarquía de excepciones y handler

```text
MinecraftException
├── RecursoNoEncontradoException     -> HTTP 404
├── DatosInvalidosException          -> HTTP 400
├── CreeperMuertoException           -> HTTP 422
└── CreeperYaDetonadoException       -> HTTP 409
```

`MinecraftExceptionHandler` es un `@RestControllerAdvice` con un `@ExceptionHandler` por cada
excepción concreta. No hay un handler genérico de `Exception`: un error de programación debe
seguir viéndose como `500`, no como un error de entrada. Todas las respuestas usan el mismo
cuerpo:

```json
{
  "timestamp": "2026-11-09T20:24:26.123",
  "status": 422,
  "error": "Unprocessable Entity",
  "tipo": "CreeperMuertoException",
  "mensaje": "Creeper no puede detonar: está muerto."
}
```

## 6. Interface de dominio

`UICapable` declara los datos que una entidad puede mostrar en una UI. La implementan
`Creeper`, `Zombie`, `Esqueleto`, `Enderman`, `Aldeano`, `Animal` y `Jugador`.
`GET /ui` arma una `List<UICapable>` y le pide los datos a cada elemento, sin preguntar por el
tipo concreto.

## 7. Colección de Insomnia

La colección está en [`docs/insomnia/minecraft-explosiones.json`](insomnia/minecraft-explosiones.json)
y cubre:

- simulación exitosa;
- `GET` de la última simulación, por id y lista;
- `422` por Creeper muerto;
- `409` por Creeper ya detonado;
- CRUD: listar, crear, obtener, actualizar y eliminar;
- `400` por POST ilegal (`DatosInvalidosException`);
- `404` por id inexistente (`RecursoNoEncontradoException`);
- `GET /ui`.

## 8. README y bitácora

- `README.md`: arquitectura, diagrama, endpoints, persistencia H2, simulación, jerarquía de
  excepciones, preguntas de diseño, colección de Insomnia y total de pruebas.
- [`docs/BITACORA.md`](BITACORA.md): asistentes, modelos exactos (`opencode/big-pickle`) y
  resumen de los prompts, incluido el de POO-08.

## Cómo probarlo

### Arrancar

```bash
git clone https://github.com/CeciBasu/clbasualdo-taller-git-2026.git
cd clbasualdo-taller-git-2026/minecraft
./mvnw spring-boot:run
```

Queda en `http://localhost:8080`.

### Crear la familia

```bash
# Creeper
curl -i -X POST http://localhost:8080/entidades \
  -H 'Content-Type: application/json' \
  -d '{"tipo":"Creeper","nombre":"Creeper Demo","vida":20,"vidaMaxima":20,"x":0,"y":64,"z":0}'

# Zombie
curl -i -X POST http://localhost:8080/entidades \
  -H 'Content-Type: application/json' \
  -d '{"tipo":"Zombie","nombre":"Zombie Demo","vida":20,"vidaMaxima":20,"x":1,"y":64,"z":0}'

# Jugador
curl -i -X POST http://localhost:8080/entidades \
  -H 'Content-Type: application/json' \
  -d '{"tipo":"Jugador","nombre":"Steve Demo","vida":20,"vidaMaxima":20,"x":2,"y":64,"z":0,"nivel":3}'

# Listar y consultar
curl http://localhost:8080/entidades
curl http://localhost:8080/entidades/nombre/Zombie%20Demo
```

### Probar los errores

```bash
# 400: vida negativa
curl -i -X POST http://localhost:8080/entidades \
  -H 'Content-Type: application/json' \
  -d '{"tipo":"Zombie","nombre":"Zombie Ilegal","vida":-5,"vidaMaxima":20}'

# 404: id inexistente
curl -i http://localhost:8080/entidades/999999

# 422: Creeper muerto
curl -i -X POST 'http://localhost:8080/simulacion/explosion?vidaCreeper=0'

# 409: Creeper ya detonado
curl -i -X POST 'http://localhost:8080/simulacion/explosion?repetirDetonacion=true'
```

### Simulación y consulta posterior

```bash
curl -X POST http://localhost:8080/simulacion/explosion
curl http://localhost:8080/simulacion/explosion
curl http://localhost:8080/simulacion/explosiones
curl http://localhost:8080/entidades/nombre/Creeper%20Simulado
```

### Interface de dominio

```bash
curl http://localhost:8080/ui
```

### Correr las pruebas

```bash
./mvnw test
```

67 pruebas: las del dominio, las de la entrada HTTP con MockMvc, el CRUD completo contra H2 en
memoria, la simulación persistida y cada excepción de dominio.

## Preguntas para pensar

**¿Por qué separar `domain` de `entity`?**

Porque el dominio modela el juego y no debe depender de JPA ni de Jackson. Así las reglas del
juego no se mezclan con detalles de persistencia.

**¿Por qué `SINGLE_TABLE` y no una tabla por clase?**

Porque las tres entidades comparten casi todos los datos y la API las trata como una sola
familia. Una tabla con discriminador permite listarlas juntas y devolver el subtipo correcto.
El costo es que las columnas propias de cada subtipo quedan nulas en las otras filas.

**¿Por qué la simulación guarda también las entidades y no solo el resultado?**

Porque el estado final de las entidades también debe persistir. Guardar la foto en
`SIMULACIONES_EXPLOSION` permite responder un `GET` sin volver a simular, y guardar las
entidades permite ver el efecto en el CRUD.

**¿Por qué una excepción por regla y no un `if` que devuelva `400`?**

Porque cada error tiene un significado distinto: `404` recurso inexistente, `400` dato
inválido, `422` Creeper muerto y `409` segunda detonación. La jerarquía `MinecraftException`
permite que el dominio lance la excepción correcta y que el handler decida el código HTTP.

**¿Qué pasa si el controller arma el JSON a mano?**

Se pierde el polimorfismo: habría que preguntar por el tipo con `if` o `instanceof`. Con
`@JsonTypeInfo` y `@JsonSubTypes`, Jackson elige la clase concreta a partir de `"tipo"` y el
controller solo devuelve `EntidadEntity`.
