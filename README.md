# Perfulandia App

Bienvenido al repositorio, esto es una aplicción móvil nativa Android desarrollada en Kotlin y Jetpack Compose para la compra y gestión de perfumes.

## Integrantes del equipo

* Moises Rodriguez
* Diego Jaramillo

## Funcionalidades

* **Autenticación de Usuarios:**
  * Inicio de sesión (para cliente, admin e invitado).
  * Registro de nuevos usuarios.
  * Recuperación de contraseña (no funcional).
* **Catálogo de Productos:**
  * Visualización de perfumes destacados en el Home.
  * Filtrado por categorías.
  * Detalle profundo de cada perfume (imágenes, precio, descripción...).
* **Gestión de Compras:**
  * Carrito de compras (Agregar, eliminar, ver total).
  * Proceso de Checkout y confirmación de orden.
  * Historial de "Mis Órdenes".
* **Interacción Social:**
  * Creación de reseñas y valoraciones para los productos.
* **Perfil:**
  * Gestión de datos de usuario y avatar.
* **Administración:**
  * Creación de nuevos perfumes (Pantalla de usuario Admin).
* **Persistencia local** (CRUD) y **almacenamiento de imagen de perfil**
  * DataStore guarda token de sesión y URI (dirección de archivo) de avatar, persisten al cerrar la aplicación.
  *Al cerrar sesión, se borra la info guardada
* **Recursos nativos**: cámara/galería (permisos y fallback)
  * Accesos a recursos con permisos dinámicos gracias al modificar AndroidManifest
  * Selección o captura de imagen como avatar de usuario

## Endpoints Usados

La aplicación se comunica con una API REST (Microservicios).
No se utilizan APIs públicas externas, todo es gestionado por el backend propio.
A continuación, se detallan los endpoints principales utilizados en la capa de datos:

* **Auth:**
  * `POST /auth/login` - Iniciar sesión.
  * `POST /auth/register` - Registrar usuario.
* **Perfumes:**
  * `GET /perfumes` - Obtener lista de perfumes.
  * `GET /perfumes/{id}` - Obtener detalle.
  * `POST /perfumes` - Crear perfume.
* **Categorías:**
  * `GET /categories` - Listar categorías disponibles.
* **Órdenes:**
  * `POST /orders` - Generar una nueva orden de compra.
  * `GET /orders/my-orders` - Historial de usuario.
* **Reseñas (Reviews):**
  * `POST /reviews` - Publicar una reseña.

## Instrucciones para ejecutar el proyecto

- **Instalación:**
  1. Clonar el repositorio
  2. Abrir el proyecto en Android Studio (fijarse bien en la versión)
  3. Y sincronizar dependencias de librerías en el build.gradle.kts (hacer clic en sync now)

- **Ejecución:**
  - Desde Android Studio hacer clic en "Run App"
  - Emulador o dispositivo físico con Android 8.0 o superior (en nuestro caso, usamos mi telefono android y le active la opción de "Depuración por USB")

### APK firmado y ubicación del archivo .jks
* **APK Firmado (Descarga directa):** https://github.com/moi-rodriguez/Perfulandia/releases/download/0.1.0/app-release.apk
* **Ubicación archivo .jks:** https://github.com/moi-rodriguez/Perfulandia/blob/main/keystore1

### Código fuente de microservicios y app móvil.
* **Repositorio App Móvil (Este repositorio):** https://github.com/moi-rodriguez/Perfulandia
* **Repositorio Backend (Microservicios):** https://github.com/roberto-arce-dev/perfulandia-api

### Evidencia de Trabajo Colaborativo
* **Historial de Commits:** https://github.com/moi-rodriguez/Perfulandia/commits/main/
