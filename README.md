# Ae5 · Refactorización respaldada por pruebas unitarias

**UCOM0310 – Diseño de Software · UEES**
**Estudiante:** Ruth Betsabe Caicedo Quintero (`ruth.caicedo@uees.edu.ec`)

Sistema de reservas usado como código heredado para practicar refactorización
avanzada (Extract Class, Move Method, Value Object, Guard Clauses)
respaldada en todo momento por una suite de pruebas JUnit 5.

## 1. Estructura del proyecto

```
reservas-refactor/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/uees/reservas/
    │   ├── domain/
    │   │   ├── Usuario.java
    │   │   ├── Reserva.java
    │   │   └── Correo.java            (Value Object – Refactorización 2)
    │   ├── repository/
    │   │   └── ReservaRepository.java (Extract Class – Refactorización 1)
    │   ├── notificacion/
    │   │   └── NotificadorReserva.java(Extract Class – Refactorización 1)
    │   └── service/
    │       └── ReservaService.java    (orquestador; Guard Clauses – Refactorización 3)
    └── test/java/com/uees/reservas/
        ├── service/ReservaServiceTest.java  (12 pruebas – red de seguridad original)
        └── domain/CorreoTest.java           (7 pruebas – contrato del Value Object)
```

## 2. Requisitos

- Java 17 o superior.
- Maven 3.8+ (ver sección 4 si no puedes instalarlo por falta de permisos de administrador).

## 3. Cómo ejecutar las pruebas

```bash
mvn clean test
```

Resultado esperado (capturado en este entorno, ver `evidencia/mvn-test-final.txt`):

```
Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 4. Si no tienes permisos de administrador para instalar Maven

Tienes tres alternativas, ninguna requiere privilegios de administrador:

1. **Maven "portable" (recomendada):** descarga el .zip de Maven desde
   `https://maven.apache.org/download.cgi`, descomprímelo en cualquier
   carpeta de tu usuario (por ejemplo `C:\Users\ruth\herramientas\maven`)
   y ejecútalo directamente desde ahí:
   ```
   C:\Users\ruth\herramientas\maven\bin\mvn.cmd clean test
   ```
   No hay instalador de por medio: es solo descomprimir una carpeta.

2. **IntelliJ IDEA Community Edition o Eclipse:** ambos traen Maven
   integrado. Abre el proyecto como "proyecto Maven existente"
   (`Open` → selecciona la carpeta que contiene `pom.xml`) y usa el panel
   de Maven del IDE para correr el goal `test`. La instalación del IDE
   tampoco requiere permisos de administrador (existen instaladores
   portables/"per-user").

3. **Sin Maven, solo con `javac`/`java` (modo de emergencia):** si además
   no tienes forma de instalar/descomprimir Maven, puedes compilar y
   correr las pruebas manualmente contra el jar `junit-platform-console-standalone`
   (se descarga una sola vez, sin instalación):
   ```bash
   javac -d out -cp junit-platform-console-standalone.jar $(find src/main -name "*.java")
   javac -d out -cp "junit-platform-console-standalone.jar:out" $(find src/test -name "*.java")
   java -jar junit-platform-console-standalone.jar --classpath out --scan-classpath
   ```

## 5. Ciclo de trabajo seguido (obligatorio según la guía de Ae5)

```
PRUEBA VERDE → CAMBIO PEQUEÑO → PRUEBA VERDE → COMMIT → SIGUIENTE CAMBIO
```

Cada una de las 3 refactorizaciones se hizo como un commit independiente,
solo después de confirmar `mvn test` en verde. El historial completo está
en `git log` (ver reporte técnico para el detalle comentado de cada commit).

## 6. Declaración de uso de IA

Este proyecto (código heredado de ejemplo, suite de pruebas, las tres
refactorizaciones, los commits y el reporte técnico) se desarrolló con
asistencia de Claude (Anthropic) como copiloto de programación, bajo
supervisión y decisión de la estudiante en cada paso. El detalle exacto
de qué se le pidió a la IA y qué se decidió manualmente está en la
sección "Declaración de uso de IA" del reporte técnico.
