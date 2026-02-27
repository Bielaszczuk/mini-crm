# CRMApp – Mini CRM en JavaFX

Una aplicación de escritorio sencilla de gestión de clientes, proyectos y tareas. Desarrollada en Java 21 con JavaFX y arquitectura profesional (MVC, DTO, Repository, Service, Validation).

## 🚀 Instalación rápida

### macOS
## 📦 Descargas

- [ Descargar para macOS (.dmg)](https://github.com/1337B/mini-crm/releases/download/v1.0.0/CRMApp-1.0.dmg)

O
1. Ir a la sección [Releases](https://github.com/TU_USUARIO/TU_REPO/releases).
2. Descargar el archivo `CRMApp.dmg`.
3. Abrir el `.dmg` y arrastrar `CRMApp` a la carpeta **Aplicaciones**.
4. Hacer doble clic sobre la app para comenzar.

 No es necesario instalar Java ni JavaFX. Todo viene embebido.

>  Si al abrir la app aparece un mensaje de seguridad de Apple, hacé clic derecho → **Abrir** la primera vez.

---

### 🪟 Windows (próximamente)

Se publicará un instalador `.exe` cuando se compile desde una PC con Windows.

---

## 🔧 Requisitos para compilar desde código fuente

- Java 21 o superior
- Maven 3.9+
- JavaFX SDK (solo si querés correrlo manualmente)
- JavaFX JMODs (solo si querés empaquetarlo con `jpackage`)

## 💻 Compilar desde código

```bash
git clone https://github.com/1337B/mini-crm.git
cd crm
mvn clean package
```
## Para ejecutar:

```
java --module-path /ruta/a/javafx-sdk-XX/lib \
--add-modules javafx.controls,javafx.fxml \
-jar target/crm-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## 📦 Crear instalador (solo si querés empaquetar)
```
jlink \
--module-path "/ruta/a/javafx-jmods:$JAVA_HOME/jmods" \
--add-modules java.base,java.logging,java.sql,java.xml,javafx.controls,javafx.fxml \
--output custom-runtime
```

```
jpackage \
--type dmg \
--name CRMApp \
--input target \
--main-jar crm-1.0-SNAPSHOT-jar-with-dependencies.jar \
--main-class com.cbielaszczuk.crm.ui.MainApp \
--dest dist \
--app-version 1.0 \
--runtime-image custom-runtime
🧠 Arquitectura
Java 21 + JavaFX 20+
```
## H2 Database (persistente, archivo local)

DTOs, Validators, Repositories, Services, Mappers

JavaFX con FXML (separación de UI y lógica)

Testing con JUnit 5

📁 Estructura del proyecto
```
├── src/main/java/com/cbielaszczuk/crm
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── mapper/
│   ├── model/
│   ├── repository/
│   ├── service/
│   ├── ui/
│   └── validation/
├── src/main/resources/views/
├── target/
├── database/
└── dist/ 
```