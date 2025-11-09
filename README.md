# Perfulandia App

## 1. Caso elegido y alcance

- **Caso:** Perfulandia SPA / App de e-commerce especializada en perfumes
  
- **Alcance EP3:** Diseño/UI, validaciones, navegación, estado, persistencia, recursos nativos, animaciones

## 2. Requisitos y ejecución

- **Stack:** 
  - Framework: Android Studio con lenguaje Kotlin y Jetpack compose
  - Librerías:
    - Retrofit / Gson: Consumo de API REST
    - Coil: carga de imágenes (avatar)
    - Accompanist Permissions: manejo de permisos
    - DataStore: persistencia de sesión y avatar
    - Navigation Compose: navegación
    - Material3: interfaz moderna y responsiva.

- **Instalación:**
  1. Clonar el repositorio
  2. Abrir el proyecto en Android Studio (fijarse bien en la versión)
  3. Y sincronizar dependencias de librerías en el build.gradle.kts (hacer clic en sync now)

- **Ejecución:**
  - Desde Android Studio hacer clic en "Run App"
  - Emulador o dispositivo físico con Android 8.0 o superior (en mi caso, use mi telefono y le active la opción de "Depuración por USB")

## 3. Arquitectura y flujo

- **Estructura carpetas**:
  
  ├── Manifest/ (AndroidManifest)
  
  ├── data/
  
  │   ├── local/ (SessionManager)
  
  │   ├── remote/ (ApiService, DTOs)
  
  │   └── repository/ (UserRepository, AvatarRepository)
  
  ├── ui/
  
  │   ├── screens/ (Login, Signup, Home, Profile)
  
  │   ├── components/ (ImagePickerDialog)
  
  │   └── navigation/ (AppNavigation, Screen)
  
  ├── viewmodel/ (LoginViewModel, SignupViewModel, ProfileViewModel)
  
  AppDependencies
  
  MainActivity

- **Gestión de estado**:
  - Estrategia local
  - Los estados reflejan carga/exito/error en la UI

- **Navegación**:
  - Estructura stack con cuatro rutas principales: Login, Signup, Home y Profile
  - Verifica sesión activa al iniciar y redirige a Home si el usurio ya tiene token guardado

## 4. Funcionalidades

- **Formulario validado (registro/otra entidad)**
  - Login con validación de formato de email y contraseña
  - Registro con validación de nombre, correo y contraseña segura (mínimo 8 caracteres, una letra y un número)

- **Navegación y backstack**
  - 1. Login - 2. Home - 3. Profile
  - Home y Profile accesibles mediante NavBar inferior
  
- **Gestión de estado (carga/éxito/error)**
  - Uso de StateFlow para mostrar animaciones de carga, errores y estados de éxito.

- **Persistencia local** (CRUD) y **almacenamiento de imagen de perfil**
  - DataStore guarda token de sesión y URI (dirección de archivo) de avatar, persisten al cerrar la aplicación.
  - Al cerrar sesión, se borra la info guardada
  
- **Recursos nativos**: cámara/galería (permisos y fallback)
  - Accesos a recursos con permisos dinámicos gracias al modificar AndroidManifest
  - Selección o captura de imagen como avatar de usuario

- **Animaciones** con propósito
  - Animación de carga inicial (loader con delay)
  - Transiciones suaves entre pantallas
  
- **Consumo de API** (incluye `/me`)
  - Endpoints /auth/signup, /auth/login, /auth/me
  - Autenticación JWT automática con AuthInterceptor

## 5. Endpoints

  **Base URL:** `https://x8ki-letl-twmt.n7.xano.io/api:Rfm_61dW`

  1. POST /auth/login (login en retrieve an auth token) 
Parameters example: 
{
  "email": "user@example.com",
  "password": "string"
}
Responses: 
{
  "authToken": "string",
  "user_id": "string"
}

2. GET /auth/me (Get the user record belonging to the authentication token)
Parameters example: 
{
  "id": 0,
  "created_at": "now",
  "name": "string",
  "email": "user@example.com",
  "account_id": 0,
  "role": "admin"
}

3. POST /auth/signup (Signup and retrieve an authentication token) 
Parameters example: 
{
  "name": "string",
  "email": "user@example.com",
  "password": "string"
}
Responses:
{
  "authToken": "string",
  "user_id": "string"
}

## 6. User flows

- Descripción del flujo principal:
  1. Usuario abre la app -> pantalla de carga 3s
  2. Si hay token -> Home; si no -> Login
  3. Desde Login puede registrarse o iniciar sesión
  4. Tras login o signup exitoso -> Home
  5. Desde Home puede ir a Perfil
  6. En Perfil:
     - cambiar avatar (cámara o galería)
     - ver su nombre y correo
     - y cerrar sesión (borra token y avatar)

- Casos de error:
  - Campos vacíos o inválidos -> mensajes de error locales
  - Credenciales incorrectas -> mensajes según código HTTP
  - Token expirado -> redirección a Login
