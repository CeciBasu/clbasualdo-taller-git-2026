# Especificaciones — POO-06 aplicado al dominio Minecraft

**Ejercicio:** `ejercicio-poo-06-revision-paquetes-constructores-2026-09-30`
**Dominio elegido:** Minecraft
**Alumno:** Cecilia Basualdo
**Repositorio:** <https://github.com/CeciBasu/clbasualdo-taller-git-2026>
**Commit de la solución:** <https://github.com/CeciBasu/clbasualdo-taller-git-2026/commit/94a8bd0fcb63b926b1b281c76a46029bd0abbe71>

---

## Objetivo

Publicar un servicio HTTP con Spring Boot sobre el modelado de clases de Minecraft que ya
venía de POO-03, con estos mecanismos a la vista:

- **herencia** y **sobreescritura**, con un método abstracto en la clase base y dos clases
  hijas independientes que lo implementan a su manera;
- **sobrecarga** de al menos un mensaje del dominio (la misma acción, otra lista de
  argumentos, otro contexto);
- **ocultamiento de la información**, de modo que el estado cambie solo por mensajes y
  constructores y nunca por asignación directa desde afuera;
- **paquetes** cuyo nombre coincide con la carpeta, siguiendo el template de la cátedra;
- dos **servicios REST**: un `GET /` que confirma que el servicio está vivo, y un controller
  que construye una instancia del dominio a partir de parámetros de la URL y responde JSON.

Al terminar, otra persona tiene que poder clonar el repositorio, arrancar el servicio y usarlo
por el navegador, sin abrir un `main()` en el entorno de desarrollo.

## Cómo está modelado Minecraft

La jerarquía es esta:

- `Entidad` (abstracta): lo común a todo lo que existe en el mundo (`nombre`, `vida`,
  posición). Declara el método abstracto `reaccionar()`.
- `EntidadHostil` (abstracta): los enemigos. Declara `atacar()` abstracto y el ataque real
  `golpear(Entidad)` con daño y rango.
- `EntidadPasiva` (abstracta): los que no atacan. Sabe `huir()` y `estaEnPeligro(...)`.
- Concretas hostiles: `Zombie`, `Esqueleto`, `Creeper`, `Enderman`.
- Concretas pasivas: `Aldeano`, `Animal`.
- `Jugador`: hereda directo de `Entidad`.

Las reglas de vida, movimiento y muerte viven en `Entidad`; cada subclase redefine solo lo
que realmente cambia.

## Consignas y dónde se cumplen

### 1. Paquetes que coinciden con las carpetas

El proyecto sigue el template de paquetes de la cátedra:

| Paquete | Qué contiene |
|---|---|
| `…minecraft.domain` | El modelo: `Entidad` y sus hijas. Sin dependencia de Spring. |
| `…minecraft.repository` y `.impl` | El contrato y la implementación en memoria. |
| `…minecraft.service` y `.impl` | Las reglas de negocio. |
| `…minecraft.rest.controller` | La entrada HTTP. |
| `…minecraft.constants` | `ApiPaths`. |
| `…minecraft.exceptions` | `MinecraftException` y `DatosInvalidosException`. |
| `…minecraft.demo` | `Main`, la demo de consola, aparte del dominio. |
| `MinecraftApplication` | Arranque de Spring Boot, en la raíz del paquete. |

La lógica del juego no está en la `Application` ni en los controllers.

### 2. El modelado de septiembre entra al proyecto y compila

Está en `src/main/java/.../domain`, compila y corre. Las 56 pruebas pasan.

### 3. Método abstracto y sobreescritura en dos clases hijas

`Entidad` declara:

```java
public abstract String reaccionar();
```

Lo sobreescriben las siete clases concretas con la misma firma y su propio cuerpo. Dos
ejemplos independientes:

```java
// Creeper
@Override public String reaccionar() {
    return getNombre() + " se acerca silbando y está a punto de explotar.";
}

// Aldeano
@Override public String reaccionar() {
    return getNombre() + " se asusta y corre a esconderse.";
}
```

`EntidadHostil.atacar()` también es abstracto y lo sobreescriben `Creeper`, `Zombie`,
`Esqueleto` y `Enderman`.

### 4. Dos servicios REST

- `IndexController` → `GET /` confirma que el servicio está vivo.
- `EsqueletoController` → `GET /esqueleto?...` construye un `Esqueleto` con parámetros de la
  URL. Si un valor rompe una regla del dominio, la clase lo rechaza y el controller responde
  `400` con el mensaje (`ResponseEntity`), sin tirar la excepción.
- `ComportamientoController` → `GET /comportamiento` devuelve la reacción de cada hija.

### 5. JSON de las clases hijas, tratadas como el tipo padre

`GET /comportamiento` arma una `List<Entidad>` y le pide `reaccionar()` a cada elemento. No
hay ningún `if` por tipo: el texto sale del método sobreescrito de cada clase.

### 6. Constructores simples y sobrecargados

`Entidad` tiene dos constructores: `(nombre, vida)` y `(nombre, vida, x, y, z)`. Las hijas
llaman a `super(...)` y cada firma deja el objeto en un estado legal. `Esqueleto` tiene
`()` y `(nombre, vida)`, y rechaza nombre vacío o vida no positiva.

### 7. Sobrecarga de un mensaje del dominio

`Esqueleto.disparar()`:

```java
public String disparar()                 // dispara sin más datos
public String disparar(double distancia) // dispara indicando la distancia
```

Es el ejemplo de la consigna. La segunda versión tiene un contexto que la primera no: si la
distancia supera el rango de ataque (8.0) la flecha se pierde y **no** se gasta, y una
distancia negativa se rechaza. Ambas se ven en `GET /esqueleto/disparar`.

Otras sobrecargas del modelo: `Entidad.moverse()` / `moverse(dx,dy,dz)`,
`EntidadPasiva.huir()` / `huir(amenaza)`, `Aldeano.comerciar()` / `comerciar(int)`,
`Animal.comer()` / `comer(int)`, `Jugador.construir()` / `construir(String)`.

### 8. README al día

`README.md` trae la licencia Apache 2.0, el diagrama Mermaid alineado con `src/`, la tabla de
endpoints con ejemplos de JSON, y el apartado "Sobrecarga y sobreescritura" que explica qué se
agregó, en qué clases y cómo se distinguen.

### 9. Bitácora

[`docs/BITACORA.md`](../docs/BITACORA.md), con las dos herramientas de IA usadas, los modelos
y el resumen de los prompts.

## Cómo probarlo

### Arrancar

```bash
git clone https://github.com/CeciBasu/clbasualdo-taller-git-2026.git
cd clbasualdo-taller-git-2026/minecraft
./mvnw spring-boot:run
```

Queda en `http://localhost:8080`.

### Probar las rutas

```bash
# 1. El servicio está vivo
curl http://localhost:8080/

# 2. Construye un Esqueleto desde la URL (constructor sobrecargado)
curl "http://localhost:8080/esqueleto?nombre=Bony&vida=15"

# 3. Un dato inválido → 400 con el mensaje del dominio
curl -i "http://localhost:8080/esqueleto?vida=0"

# 4. La sobrecarga de disparar(): dentro del rango
curl "http://localhost:8080/esqueleto/disparar?nombre=Bony&distancia=5"

# 5. La sobrecarga de disparar(): fuera del rango (la flecha no se gasta)
curl "http://localhost:8080/esqueleto/disparar?nombre=Bony&distancia=20"

# 6. Distancia negativa → 400
curl -i "http://localhost:8080/esqueleto/disparar?distancia=-3"

# 7. El JSON de las dos clases hijas, tratadas como Entidad (sobreescritura)
curl http://localhost:8080/comportamiento
```

### Correr las pruebas

```bash
./mvnw test
```

56 pruebas: las del dominio (incluidas las dos versiones de `disparar()`) y las de la entrada
HTTP con MockMvc (`ApiControllerTest`), que verifican que `/`, `/esqueleto`,
`/esqueleto/disparar` y `/comportamiento` respondan lo esperado y que los datos inválidos
terminen en `400` y no en `500`.

## Pregunta de anclaje

**¿Qué ocurre si el controller asigna a mano la vida o la munición?**

No puede. `nombre`, `vida` y `flechas` son `private`, y no hay ningún setter. Desde el
controller no existe forma de escribir `esqueleto.vida = -500` ni `esqueleto.flechas = 99`:
el compilador lo rechaza porque el campo es inaccesible fuera de la clase. Lo único que el
controller puede hacer es pasar valores por el **constructor** o llamar a **mensajes** del
dominio (`recibirDanio`, `curar`, `recargar`, `disparar`), y todos esos mensajes validan lo
que reciben. Por eso un `vida=0` que llega por la URL no deja un objeto roto: la clase tira
`IllegalArgumentException`, el servicio la traduce a `DatosInvalidosException` y el controller
responde `400`. El estado solo cambia por los caminos que el dominio controla.
