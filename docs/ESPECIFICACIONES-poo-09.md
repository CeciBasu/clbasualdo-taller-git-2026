# Especificaciones — POO-09 aplicado a la entrega de Cecilia Basualdo

**Ejercicio:** POO-09 — APIs cruzadas (OpenAPI, round robin, consumo y escenario persistido)
**Dominio elegido:** Minecraft
**Alumna:** Cecilia Basualdo
**Repositorio:** <https://github.com/CeciBasu/clbasualdo-taller-git-2026>
**Rama:** `poo-09-apis-cruzadas`
**Commit de la solución:** <https://github.com/CeciBasu/clbasualdo-taller-git-2026/commit/4d09e0b131e8980163102483787410a6e570e699>

---

## Enunciado aplicado a esta entrega

### A1, A2 y A3

| Rol | Integrante | Repositorio / commit | Servicio | OpenAPI | Reglas que citan |
|---|---|---|---|---|---|
| **A1** | Cecilia Basualdo (esta entrega) | [CeciBasu/clbasualdo-taller-git-2026](https://github.com/CeciBasu/clbasualdo-taller-git-2026), rama `poo-09-apis-cruzadas`, commit [`4d09e0b`](https://github.com/CeciBasu/clbasualdo-taller-git-2026/commit/4d09e0b131e8980163102483787410a6e570e699) | `http://100.122.114.103:8083` (local `http://localhost:8083`) | <http://100.122.114.103:8083/v3/api-docs> | Un Creeper solo detona si está vivo (422) y solo una vez (409). Publicado en `POST /reglas/creeper-vivo`, `POST /reglas/detonacion-unica` y `POST /explosiones/validar`. |
| **A2** | Irina Insfrán | [IrinaInsfran/ii-taller-git-2026](https://github.com/IrinaInsfran/ii-taller-git-2026), commit `9ace19beefdf2ee0a3deaf20ebf61e7a074b32eb` (rama `feature/rest`) | `http://100.87.95.24:8082` (local `http://localhost:8082`) | <http://100.87.95.24:8082/v3/api-docs> | **Mecha corta:** un Creeper vivo solo enciende la mecha si hay un Jugador vivo a 3 bloques o menos (422 `fuera_de_alcance`). Publicado en `POST /api/minecraft/reglas/mecha-corta`, que no escribe en su H2. |
| **A3** | María Sol Ferreira | [MariaSol45/msferreira-tplp3-2026](https://github.com/MariaSol45/msferreira-tplp3-2026), commit `6e37f2608f311a46b00893af00af4fc186494bb4` (rama `feature/poo-09-apis-cruzadas`) | `http://100.87.131.50:8080` (local `http://localhost:8080`) | <http://100.87.131.50:8080/v3/api-docs> | No detona antes de `tiempoExplosion` (409) y todos los objetivos deben estar dentro de `radioExplosion` (422). Publicado en `POST /explosiones/validar`. |

El grupo es un trío de Minecraft: cada una es A1 de su propia entrega y consulta a las otras dos.
Las tres anotan las mismas tres URLs. No se clonó código ajeno para copiar reglas: las reglas y
las rutas salen de los README y de los OpenAPI de las compañeras. La URL base y la ruta de cada
cliente se configuran en `application.properties` (`A2_BASE_URL`, `A2_RUTA`, `A3_BASE_URL`,
`A3_RUTA`), así que un cambio de nombre en un OpenAPI se resuelve sin tocar Java.

### Escenario en una frase

Un Creeper guardado en mi H2 se acerca a un Jugador guardado y detona: la jugada solo es legal
si Irina (A2) confirma que la mecha se enciende y Sol (A3) acepta el tiempo y el radio, y recién
entonces se detona y se guarda en mi H2; si alguna rechaza, la base queda igual y la respuesta
dice `rechazadoPor`.

Orden: mis reglas (vivo y sin detonar) → A2 (mecha corta) → A3 (tiempo y radio) → transacción
que detona, daña y guarda en `EMBOSCADAS`. Las llamadas HTTP se hacen antes de la transacción.

## Cómo probarlo

1. Clonar y arrancar los tres commits, primero A2 y A3, después A1:

   ```bash
   # A2 — Irina Insfrán, puerto 8082
   git clone https://github.com/IrinaInsfran/ii-taller-git-2026.git
   cd ii-taller-git-2026
   git checkout 9ace19beefdf2ee0a3deaf20ebf61e7a074b32eb
   ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8082

   # A3 — María Sol Ferreira, puerto 8080
   git clone https://github.com/MariaSol45/msferreira-tplp3-2026.git
   cd msferreira-tplp3-2026
   git checkout 6e37f2608f311a46b00893af00af4fc186494bb4
   cd minecraft && ./mvnw spring-boot:run

   # A1 — este repositorio, puerto 8083
   git clone https://github.com/CeciBasu/clbasualdo-taller-git-2026.git
   cd clbasualdo-taller-git-2026
   git checkout 4d09e0b131e8980163102483787410a6e570e699
   cd minecraft && A2_BASE_URL=http://localhost:8082 A3_BASE_URL=http://localhost:8080 ./mvnw spring-boot:run
   ```

   Esas variables son para levantar los tres servicios en una máquina. Si se usan los servicios
   reales de A2 y A3 por Tailscale, no hace falta exportarlas: los valores por defecto ya apuntan
   a `http://100.87.95.24:8082` y `http://100.87.131.50:8080`.

2. Importar [`docs/insomnia/minecraft-poo09.json`](insomnia/minecraft-poo09.json) y elegir el
   entorno «Grupo en una máquina» (`a1_url` 8083, `a2_url` 8082, `a3_url` 8080) o «Grupo por
   Tailscale (servicios reales)». Recorrer las carpetas 0 (los tres OpenAPI), 1 (preparar
   entidades) y 2 (feliz, rechazo de A2, rechazo de A3 y el `GET` que comprueba la base).

3. Camino feliz directo, con una base preparada con el CRUD (Creeper id 1 y Jugador id 2):

   ```bash
   curl -X POST http://localhost:8083/escenarios/emboscada \
     -H "Content-Type: application/json" \
     -d '{"creeperId": 1, "jugadorId": 2, "tiempoTranscurrido": 3}'
   curl http://localhost:8083/escenarios/emboscadas/1
   ```

   Responde `201` y el `GET` muestra la jugada guardada con las respuestas de A2 y A3.

4. Rechazo de A2: repetir con el Jugador id 4, a 10 bloques. Responde `422` con
   `rechazadoPor: A2`; `GET /entidades/3` muestra el Creeper sin detonar y
   `GET /escenarios/emboscadas` sigue con la misma cantidad de filas.

5. Rechazo de A3: usar el Creeper id 5 y el Jugador id 6 con `tiempoTranscurrido: 1`. Responde
   `422` con `rechazadoPor: A3` y la base queda igual.

6. Si un compañero está apagado, el servicio responde `503 CompaneroNoDisponibleException` y no
   guarda nada. Las respuestas reales de cada caso están en
   [`docs/insomnia/respuestas/`](insomnia/respuestas/), para probar sin los tres servicios vivos.

## Corrección de POO-08

POO-08 tuvo observaciones (16 de 24); todas quedaron cerradas y están detalladas en la tabla
«Situación de POO-08» del README. Por eso se aplican los 3 puntos de la corrección en esta entrega.

## Entregables de esta entrega

- [README](https://github.com/CeciBasu/clbasualdo-taller-git-2026#poo-09--apis-cruzadas)
- [Bitácora de IA](BITACORA.md)
- [Colección de Insomnia](insomnia/minecraft-poo09.json) y [respuestas de ejemplo](insomnia/respuestas/)
- [OpenAPI exportado](../docs/openapi/) (JSON y YAML)
