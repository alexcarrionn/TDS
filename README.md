# TDS

**Prácticas TDS 2024/2025**

Realización de una aplicación de mensajería llamada **APPCHAT**, inspirada en WhatsApp.

---

## Realizado por:
- Alejandro Carrión Jordán  
- Juan Pedro Jiménez Dato

## Profesor:
- Jesús Joaquín García Molina

## Grupo:
- 3.1

## Asignatura:
- Tecnologías de Desarrollo de Software

---

## ✅ Correcto Funcionamiento

Para la correcta ejecución de la aplicación desde **Eclipse**, es necesario instalar previamente las dependencias `chatWindowLib` y `driverPersistencia`.

Dichas dependencias han sido proporcionadas por los profesores de la asignatura y se instalan usando **Maven**, ejecutando los siguientes comandos desde la raíz del proyecto:

```bash
mvn install:install-file -Dfile=lib/chatWindowLib.jar -DgroupId=tds -DartifactId=chatWindowLib -Dversion=1 -Dpackaging=jar

mvn install:install-file -Dfile=lib/DriverPersistencia.jar -DgroupId=umu.tds -DartifactId=driverPersistencia -Dversion=2.0 -Dpackaging=jar
