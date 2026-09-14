# Frontend (HTML/CSS/JS) para el backend Bruno-Negro

> Documento de diseño. Escrito el 2026-09-14, antes de implementar nada del front.
> Estado del back al momento de escribirlo: sin gateway, sin CORS, sin validaciones, sin auth.

## Contexto

El backend son 5 módulos Maven independientes (`eureka-server`, `client-service`, `product-service`, `cart-service`, `sale-service`), Spring Boot 4.1.1 / Java 25, cada uno en su puerto y con su propia base MySQL. Hoy no hay ningún frontend ni archivo `.html`/`.css`/`.js` en el repo.

Querés armar un front en HTML/CSS/JS puro y tenés dos dudas concretas:
1. Si los archivos del front tienen que compartir directorio con los del back.
2. Cómo se conecta un front de una tecnología con un back de otra.

Este documento responde las dos, define la estructura de carpetas y deja anotado qué falta en el back antes de poder arrancar. **No se escribe código todavía** — el API Gateway lo vas a implementar vos primero, y después conviene volver a revisar este plan.

---

## Parte 1 — Las dos dudas

### 1.1 ¿Comparten directorio?

**No tienen por qué, y no deberían compartir *build*.** La confusión típica es mezclar dos cosas distintas:

- **Repositorio git** = qué archivos versionás juntos. Es una decisión organizativa.
- **Unidad de build** = qué compila Maven. Es una decisión técnica.

Un front de HTML/CSS/JS no se compila: son archivos de texto que el navegador lee tal cual. Maven ni se entera de que existen. Así que pueden convivir en el mismo repo sin acoplarse en absoluto.

**Decisión tomada: carpeta hermana `frontend/` dentro de este mismo repo.**

```
Bruno-Negro/
├── eureka-server/        ← módulo Maven
├── gateway-service/      ← módulo Maven (a crear)
├── client-service/       ← módulo Maven
├── product-service/      ← módulo Maven
├── cart-service/         ← módulo Maven
├── sale-service/         ← módulo Maven
└── frontend/             ← NO es módulo Maven. Archivos sueltos.
```

Ventajas para este proyecto: un solo repo, un solo historial, un solo `git clone`, y el front queda visible al lado de lo que consume. Ningún `pom.xml` lo referencia, ningún `mvn package` lo toca.

La alternativa que **conviene evitar** es meter el front en `src/main/resources/static/` de alguno de los servicios. Resuelve CORS de una (mismo origen), pero acopla el front a un microservicio arbitrario — ¿el carrito vive en cart-service o en product-service? — y te obliga a recompilar un `.jar` de Java cada vez que tocás un color en el CSS.

**Acción concreta:** el `.gitignore` de la raíz hoy tiene una sola línea (`/.atl/`). Cuando crees `frontend/`, si en algún momento sumás herramientas de Node, va a haber que agregar `node_modules/`.

### 1.2 ¿Cómo se conecta un front de JS con un back de Java?

Ésta es la parte conceptual que vale la pena que quede clara: **el frontend nunca "se conecta a Java".** No hay ninguna integración entre lenguajes. Lo que pasa es que ambos hablan un idioma neutral: **HTTP + JSON**.

El flujo real, paso a paso:

1. El navegador carga `index.html` desde algún lado (un servidor de archivos estáticos, ver 3.1).
2. El JS de esa página ejecuta `fetch('http://localhost:8080/products/find/all')`.
3. Eso emite un mensaje HTTP de texto plano por la red. Nada de eso es JavaScript — es un `GET /products/find/all HTTP/1.1` que podría haberlo mandado curl, Postman, una app de Android o un script de Python.
4. Spring recibe ese mensaje, lo enruta al `ProductController`, ejecuta el método y devuelve un `List<ProductDTO>`.
5. Jackson (dentro de Spring) serializa esa lista de objetos Java a un texto JSON: `[{"idProduct":1,"code":"A1",...}]`.
6. El navegador recibe ese texto. `response.json()` lo convierte en objetos JavaScript.

Los `ProductDTO` que ya tenés son exactamente eso: **el contrato**. Definen qué campos viaja el JSON. Java del lado del servidor, objetos planos del lado del navegador, misma forma. Por eso el backend puede ser Java, Node, Go o PHP y el front no cambia una línea.

Dos consecuencias prácticas de esto:

- **Son dos servidores distintos.** Uno sirve los archivos estáticos (HTML/CSS/JS), otro sirve JSON. No es un único programa.
- **El navegador ejecuta el JS en la máquina del usuario**, no en el servidor. Todo lo que pongas en el JS es público y modificable. Nunca validés sólo en el front, ni guardes secretos ahí.

### 1.3 El obstáculo que vas a encontrar sí o sí: CORS

Si servís el front desde `http://localhost:5500` y llamás a `http://localhost:8080`, el navegador **bloquea la respuesta**. No porque el back falle — el back responde 200 perfecto — sino porque el navegador aplica la *same-origin policy*: dos URLs son "el mismo origen" sólo si coinciden protocolo, host **y puerto**. Puerto distinto = origen distinto.

Para que el navegador acepte la respuesta, el servidor tiene que incluir cabeceras que digan explícitamente "acepto pedidos desde ese origen":

```
Access-Control-Allow-Origin: http://localhost:5500
```

Además, para métodos que no sean GET/POST simples (o sea: todos tus `PUT` y `DELETE`, y cualquier `POST` con `Content-Type: application/json`), el navegador manda primero un **preflight**: un `OPTIONS` automático preguntando si el pedido real está permitido. Si el servidor no lo contesta bien, el pedido real ni se envía.

**Hoy no hay una sola línea de configuración CORS en todo el repo.** Ni `@CrossOrigin`, ni `WebMvcConfigurer`, ni nada. Así que el primer `fetch` que escribas va a fallar. Esto no es un bug — es configuración que falta, y el lugar natural para ponerla es el gateway.

---

## Parte 2 — El API Gateway (lo que el front necesita de él)

Sin gateway, el front tendría que conocer 4 puertos distintos (7000, 8000, 9000, 10000) y habría que configurar CORS 4 veces. Con gateway, el front conoce **una sola URL** y CORS se configura **una sola vez**.

Cuando lo implementes, esto es lo que el front espera:

**Puerto:** `8080` (queda libre; ninguno de los servicios actuales lo usa).

**Rutas** — los controllers ya tienen prefijos limpios y distintos entre sí, así que el ruteo es directo, sin `StripPrefix`:

| Predicate | Destino (vía Eureka) | Puerto real |
|---|---|---|
| `Path=/clients/**` | `lb://client-service` | 8000 |
| `Path=/products/**` | `lb://product-service` | 9000 |
| `Path=/carts/**` | `lb://cart-service` | 7000 |
| `Path=/sales/**` | `lb://sale-service` | 10000 |

**CORS global en el gateway**, con:
- `allowedOrigins`: el origen del servidor estático de desarrollo (ej. `http://localhost:5500`). **No uses `*`** si más adelante vas a mandar cookies o credenciales — son mutuamente excluyentes.
- `allowedMethods`: `GET, POST, PUT, DELETE, OPTIONS`.
- `allowedHeaders`: al menos `Content-Type`, y `Authorization` cuando sumes auth.

**A verificar al implementarlo** (no lo doy por sentado): en Spring Cloud 2025.1.3 conviven dos variantes del gateway — la reactiva (WebFlux) y la de WebMVC. Los 5 poms actuales usan `spring-boot-starter-webmvc`. Si elegís la variante reactiva, el módulo del gateway **no debe** incluir `spring-boot-starter-webmvc` o van a chocar los stacks. Confirmá el `artifactId` correcto contra la documentación de esa versión antes de agregarlo.

---

## Parte 3 — Estructura del frontend

### 3.1 Cómo se sirve en desarrollo

**No abras el HTML con doble clic.** Un archivo abierto como `file:///C:/.../index.html` tiene origen `null`, y CORS con origen `null` es un dolor de cabeza innecesario.

Usá un servidor estático. Opciones, cualquiera sirve:
- Extensión **Live Server** de VS Code → sirve en `http://localhost:5500`.
- El servidor embebido de IntelliJ (clic derecho en el HTML → *Open in Browser*) → `http://localhost:63342`.
- `python -m http.server 5500` parado en `frontend/`.

Elegí uno y **fijá ese origen** — es el valor exacto que va en `allowedOrigins` del gateway.

### 3.2 Árbol de carpetas

```
frontend/
├── index.html                 ← catálogo / home
├── pages/
│   ├── login.html
│   ├── producto.html          ← detalle de un producto
│   ├── carrito.html
│   └── checkout.html          ← confirmación de venta
├── css/
│   ├── base.css               ← reset, variables CSS, tipografía
│   └── components.css         ← card de producto, botones, tabla
├── js/
│   ├── config.js              ← const API_BASE = 'http://localhost:8080'
│   ├── api/                   ← UNA CAPA POR MICROSERVICIO
│   │   ├── http.js            ← wrapper de fetch + parseo de errores
│   │   ├── products.js
│   │   ├── clients.js
│   │   ├── carts.js
│   │   └── sales.js
│   └── pages/                 ← lógica de cada pantalla
│       ├── catalogo.js
│       ├── carrito.js
│       └── checkout.js
└── assets/                    ← imágenes, íconos
```

La idea central de la organización: **`js/api/` espeja los microservicios**. Un archivo por servicio, y cada función de ese archivo es un endpoint. `products.js` expone `findAll()`, `findById(id)`, `findByCode(code)`, `filter(brand, category)` — y nada más. Ninguna página hace `fetch` directo; todas pasan por esta capa.

Eso te da lo mismo que te dan los Feign clients del lado de Java: si cambia una URL, la tocás en un solo lugar.

Usá **ES modules** (`<script type="module">` + `import`/`export`). Evita variables globales y funciona nativo en el navegador, sin build ni bundler.

### 3.3 `js/api/http.js` — por qué es la pieza más importante

Este wrapper resuelve un problema real de tu backend: **los 4 servicios devuelven formatos de error incompatibles entre sí.**

| Servicio | Campo del código | Campo del mensaje | Extras |
|---|---|---|---|
| `product-service` | `status` | **`mensaje`** (en español) | — |
| `client-service` | `status` | `message` | — |
| `cart-service` | `status` | `message` | `timestamp`, `path` |
| `sale-service` | **`statusCode`** | `message` | — |

Si cada página parsea errores por su cuenta, vas a repetir ese enredo en todos lados. El wrapper lo normaliza una vez: recibe la respuesta, y si no es OK, produce siempre un objeto con la misma forma (`{ status, message }`) sin importar de qué servicio vino.

Lo ideal sería unificarlo en el back (ver 4.2), pero el wrapper te protege igual mientras tanto.

---

## Parte 4 — Qué falta en el back antes de arrancar el front

Ordenado por cuánto te bloquea.

### 4.1 Bloqueantes — sin esto el front no funciona

1. **Gateway + CORS.** Ya cubierto arriba. Es el paso 1.

2. **`GET /clients/loginRequest` con `@RequestBody`** — `client-service/src/main/java/com/brunonegro/client_service/controller/ClientController.java:51`. La spec de `fetch()` **prohíbe** mandar body en un GET; tira `TypeError` antes de salir a la red. Cualquier pantalla de login es imposible hasta que esto sea un `POST`. Además hoy devuelve un `boolean` pelado: el front no recibe nada que pueda guardar como sesión (ni token, ni el `idClient`). Como mínimo debería devolver el `ClientDTO` del cliente logueado.

### 4.2 Molestos — se puede trabajar con esto, pero te complica el front

3. **`findAll` que tira 404 cuando no hay datos.** `SaleService.java:54-56` lanza `SaleNotFoundException` con la lista vacía, y `ProductService.java:47-56` hace lo mismo en `find/filter`. Semánticamente una colección vacía es `200 []`, no un 404 — el recurso "todas las ventas" existe, sólo que no tiene elementos. Tal como está, el front tiene que tratar un 404 como "lista vacía" en unos endpoints y como "error real" en otros, sin forma de distinguirlos.

4. **Los 4 formatos de error distintos** (tabla en 3.3). Unificarlos en el back es media hora de trabajo y te ahorra ese `if` feo en `http.js` para siempre.

5. **Cero validación de entrada.** No existe un solo `@Valid`, `@NotBlank` ni `@Email` en todo el repo, ni la dependencia `spring-boot-starter-validation`. Hoy un `POST /clients/create` con body `{}` persiste una fila con todo en null. Validar en el front **no alcanza** — el JS corre en la máquina del usuario y es trivial saltearlo. Esto no bloquea el front, pero significa que cualquier bug tuyo en el JS ensucia la base directamente.

6. **El fallback del circuit breaker miente.** `CartService.java:101` tiene `@CircuitBreaker(fallbackMethod = "fallbackCartDTO")`, y el fallback devuelve un `CartDTO` con `idCart=0, total=0, productList=[]` y **HTTP 200**. Desde el front, un carrito que falló es indistinguible de un carrito vacío: el usuario vería "tu carrito está vacío" cuando en realidad product-service se cayó. Ya está anotado como pendiente en `cart-service/CIRCUIT_BREAKER.md`.

### 4.3 Decisión pendiente — sesión de usuario

No hay Spring Security ni JWT en ningún pom. El front necesita saber "quién soy" para pedir `GET /carts/find/user?idUser=X`.

Dos caminos, y conviene que lo decidas antes de escribir la pantalla de login:

- **Simple (suficiente para práctica integradora):** el login devuelve el `ClientDTO`, el front guarda el `idClient` en `localStorage`. Sin seguridad real — cualquiera edita el `localStorage` y se hace pasar por otro — pero te deja avanzar con todo el flujo funcional.
- **Con JWT:** el back emite un token firmado, el front lo manda en `Authorization: Bearer ...`, el gateway lo valida. Es el camino correcto, pero es un proyecto en sí mismo.

Aparte: `Client.java` guarda el **password en texto plano** y `ClientService.loginAutentification` lo compara tal cual. Independientemente del camino que elijas, eso merece un `BCryptPasswordEncoder`.

---

## Parte 5 — Orden de implementación sugerido

1. **Gateway** (vos, ahora) → puerto 8080, 4 rutas `lb://`, CORS global.
2. **Verificar el gateway antes de tocar el front** (ver Parte 6). Si el gateway no anda, vas a creer que el problema es tu JavaScript.
3. Arreglar `loginRequest` → `POST` que devuelve `ClientDTO`.
4. Crear `frontend/` con el árbol de 3.2, `config.js` y `http.js`.
5. Primera pantalla: **catálogo** (`index.html` + `js/api/products.js`). Es la más simple — sólo GET, sin estado, sin login — y sirve para confirmar que la cadena navegador → gateway → servicio → MySQL funciona de punta a punta.
6. Login → carrito → checkout, en ese orden (cada uno depende del anterior).

---

## Parte 6 — Verificación

**Antes de escribir una línea de front,** con los 5 servicios y el gateway levantados:

Que el gateway enrute bien:
```bash
curl -i http://localhost:8080/products/find/all
```
Tiene que devolver el mismo JSON que `curl http://localhost:9000/products/find/all`.

Que el preflight de CORS esté contestado (ésta es la que más se olvida):
```bash
curl -i -X OPTIONS http://localhost:8080/products/find/all -H "Origin: http://localhost:5500" -H "Access-Control-Request-Method: GET"
```
Buscá `Access-Control-Allow-Origin: http://localhost:5500` en la respuesta. Si no está, el navegador va a bloquear todo.

Que Eureka tenga los 4 servicios + el gateway registrados: abrí `http://localhost:8761` y confirmá que aparecen los 5 `spring.application.name`.

**Con el front andando:** abrí las DevTools del navegador en la pestaña **Network**. Ahí ves el pedido real, las cabeceras de respuesta, el JSON crudo y el preflight `OPTIONS`. El 90% de los problemas de un front contra un back se diagnostican en esa pestaña — un error de CORS aparece en rojo en la consola con un mensaje bastante explícito.
