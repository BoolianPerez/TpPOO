# FutbolStats – Gestión y analítica de torneos de fútbol

Trabajo Práctico Obligatorio de Paradigma Orientado a Objetos – Propuesta C.
Ingeniería en Informática · Comisión Martes Mañana · Profesor: León María Angela.

## Descripción del sistema
Aplicación Java de consola con menú para administrar torneos de fútbol: clubes, estadios, sponsors, jugadores, árbitros y
entrenadores; contratos y transferencias; inscripción de clubes y jugadores en un torneo; generación del fixture; asignación
de terna arbitral y estadio; carga de nómina y titulares; registro de eventos del partido en vivo (goles, tarjetas, remates,
faltas, cambios y anulaciones); sanciones; notificaciones a suscriptores por varios canales; tabla de posiciones, ranking de
goleadores, valla menos vencida, xG y probabilidad de clasificación (por puntos o por simulación). Todas las reglas de negocio
RN01 a RN17 de la propuesta C están implementadas. Los datos se guardan en un archivo y se conservan entre ejecuciones.

## Integrantes
| Integrante | Legajo |
|---|---|
| Fuentes Santiago | 1207617 |
| Pérez Julián | 1209024 |
| Sánchez Ciro | 1209329 |

## Requisitos
- Java 17 o superior (JDK)
- Maven 3.8 o superior (opcional: también se puede compilar solo con `javac`)

## Instalación y ejecución
Con Maven:
```bash
mvn compile
mvn exec:java -Dexec.mainClass=com.tppoo.Main.Main
```
Solo con `javac` (desde la raíz del proyecto):
```bash
javac --release 17 -encoding UTF-8 -d out $(find src/main -name "*.java")
java -cp out com.tppoo.Main.Main
```
El programa carga los datos de `futbolstats.dat` al iniciar y los guarda al salir del menú principal (también con la opción
"Guardar ahora"). Para usar otro archivo se pasa la ruta como argumento: `java -cp out com.tppoo.Main.Main otro.dat`.
Formatos: fecha `dd/MM/aaaa`; fecha y hora `dd/MM/aaaa HH:mm`. En cada lista, `0` cancela o vuelve.

Orden típico de uso: estadios y clubes → personas → contratos → torneo → inscribir clubes y jugadores → generar fixture →
avanzar el torneo a EN_CURSO → preparar el partido (estadio, terna, nómina, titulares) → iniciar, registrar eventos y finalizar
→ reportes y proyecciones.

## Pruebas
Escenario automático que recorre todas las reglas de negocio (sin dependencias externas):
```bash
javac --release 17 -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out EscenarioCompleto
```
Resultado esperado: `RESULTADO: 86 OK, 0 FAIL`.

## Datos de prueba
`datos-de-prueba/demo.txt` contiene todas las respuestas que se escriben en la consola para armar un escenario completo.
Se carga redirigiendo la entrada (con un archivo nuevo para no mezclar datos):
```bash
java -cp out com.tppoo.Main.Main demo.dat < datos-de-prueba/demo.txt
java -cp out com.tppoo.Main.Main demo.dat      # reabre el escenario guardado y permite consultarlo
```
Qué crea el escenario:

| Dato | Valor |
|---|---|
| Estadio | Monumental (80.000 lugares), estadio local de los cuatro clubes |
| Clubes | Alfa, Beta, Gamma, Delta |
| Jugadores | 72 (18 por club, DNI D001 a D072), con contratos del 01/01/2026 al 31/12/2030 |
| Árbitros | 4 (DNI A0 a A3), categoría nacional |
| Torneo | Copa, del 01/01/2027 al 30/06/2027, nacional; 4 clubes inscriptos con 18 jugadores cada uno |
| Fixture | Generado de una vuelta; el torneo queda EN_CURSO |
| Primer partido | Alfa – Delta con terna, nómina y 22 titulares; 1 gol, 1 tarjeta y 1 remate; finaliza Alfa 1 – 0 Delta |
| Consultas | Tabla de posiciones, goleadores, valla menos vencida, xG promedio y probabilidad de clasificación |

## Estructura del proyecto
| Ruta | Contenido |
|---|---|
| `src/main/java/com/tppoo/model/torneos_entidades` | Entidades del dominio y eventos (Torneo, Partido, Club, Jugador, Gol, Tarjeta, ...) |
| `src/main/java/com/tppoo/model/servicios` | Notificaciones: `ServicioNotificaciones`, `Suscriptor` y canales |
| `src/main/java/com/tppoo/model/competencia` | Reportes, xG y proyecciones (Strategy) |
| `src/main/java/com/tppoo/model/excepciones` | Excepciones propias (`ReglaNegocioException` y derivadas) |
| `src/main/java/com/tppoo/persistencia` | Guardado y carga en archivo (`RepositorioArchivo`) |
| `src/main/java/com/tppoo/controller` | Un controlador por área y `ControladorSistema` |
| `src/main/java/com/tppoo/view` | `Consola` y una vista por área |
| `src/test/java` | `EscenarioCompleto` |
| `Docs` | Documento de análisis y diseño y diagramas UML |
| `datos-de-prueba` | Entrada de consola para el escenario de demostración |

## Supuestos
- Los partidos del fixture se programan a las 16:00, una fecha cada 7 días y una sola vuelta.
- Si dos clubes comparten estadio y chocan en horario, el partido del fixture queda sin estadio y se asigna a mano.
- Cada 5 amarillas acumuladas en el torneo suman 1 fecha de suspensión; la doble amarilla equivale a roja (expulsa, suspende 1 fecha y se notifica como roja).
- Desempate de la tabla: puntos, diferencia de gol, goles a favor y mini-tabla entre los clubes empatados.
- El xG se calcula solo con remates registrados; un gol sin remate no suma xG. El puntaje de corte de la proyección lo ingresa el usuario.
- Un gol exige jugador; el club al que suma se deduce de la inscripción del jugador en el torneo.
- Las reglas de 48 horas entre partidos y de un partido por día por árbitro se controlan dentro de cada torneo.
- No hay duplicados: `ModeloSistema` rechaza clubes, estadios y torneos con el mismo nombre, personas con el mismo DNI y sponsors con el mismo nombre y marca.
