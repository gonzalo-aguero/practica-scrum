
# 🧩 Proyecto Práctica SCRUM: Spring Boot + PostgreSQL + React

Este proyecto combina:
- Backend: Spring Boot
- Base de datos: PostgreSQL (contenedor Docker)
- Frontend: React (ubicado en `front/`)

---

## 📁 Estructura del Proyecto
~~~markdown
root/
├── front/               # Aplicación React
├── docker-compose.yml   # Configuración del contenedor PostgreSQL
├── README.md
├── ...                  # Código backend Spring Boot (fuera del scope de este README)
~~~

---

## 🧰 Requisitos Previos

Asegúrese de tener instalado lo siguiente:

- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/products/docker-desktop)
- [Java 21.0.4](https://adoptium.net/)
- [Node.js 20.4.0 y npm 9.7.2](https://nodejs.org/) (para ejecutar React)
- [Maven 3.9.9](https://maven.apache.org/) o su herramienta de compilación Java preferida

Las versiones de Java, Node, Npm y Maven son las que se probaron. Las mismas pueden variar siempre que sean compatibles.
---

## 🚀 Pasos para levantar el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/gonzalo-aguero/practica-scrum.git
cd tu_repositorio
````

---

### 2. Levantar la base de datos con Docker

Ejecutar el siguiente comando para levantar el contenedor de PostgreSQL en segundo plano:

```bash
docker compose up -d 
```
o si es una versión moderna de Docker:
```bash
docker-compose up -d 
```

Esto ejecuta la imagen `postgres:17.4` con los siguientes datos:

* **Base de Datos**: `project`
* **Usuario**: `postgres`
* **Contraseña**: `postgres`
* **Puerto local**: `5432`

Verificá que esté corriendo con:

```bash
docker ps
```

---

### 3. Configurar Spring Boot

Asegurate de que tu archivo `application.properties` o `application.yml` tenga la configuración correcta para conectarse a la base de datos:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/project
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
```

---

### 4. Ejecutar el backend (Spring Boot)

Podés compilar y ejecutar con Maven:

```bash
./mvnw spring-boot:run
```

O si usás un IDE (IntelliJ, Eclipse), podés correr directamente la clase `main` del proyecto.

---

### 5. Ejecutar el frontend (React)

Desde el directorio raíz del proyecto:

```bash
cd front/
npm install      # Instala dependencias

# Dos altenativas:
npm start        # Para lanzar el frontend en http://localhost:3000
npm run dev     # Igual que el anterior, pero se reinicia cada vez que haces cambios (mejor para desarrollar)
```

> Asegurate de que tu React apunte a la API de Spring Boot en el puerto correcto, por ejemplo `http://localhost:8080`.

---

## 🧼 Cómo apagar todo

Para detener y eliminar el contenedor de PostgreSQL:

```bash
docker compose down
```

Si querés borrar también los volúmenes de datos (⚠️ elimina la base de datos):

```bash
docker compose down -v
```

---

## 📎 Notas Adicionales

* Docker se encarga de persistir los datos en un volumen independiente, no se borran al reiniciar el contenedor.
* Este entorno es compatible con sistemas **Linux**, **Windows** (via Docker Desktop), y **Mac**.

---

## ✅ ¡Listo para programar!

Con estos pasos tendrás:

* La base de datos corriendo en Docker.
* El backend de Spring Boot sirviendo la lógica del negocio.
* El frontend de React ejecutándose y conectado a tu API.


---
## Conectarse a PostGreeSQL desde Terminal:
~~~bash
docker exec -it postgres_db psql -U postgres -d project
~~~
## Conectarse a PostGreeSQL desde Navegador:
[http://0.0.0.0:8081/](http://0.0.0.0:8081/)