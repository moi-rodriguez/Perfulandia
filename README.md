# Perfulandia App Mobile

## 1. Caso elegido y alcance
- **Caso:** Perfulandia SPA
- **Alcance EP3:** Diseño/UI, validaciones, navegación, estado, persistencia, recursos nativos, animaciones
  
## 2. Requisitos y ejecución
  
- **Stack:** Kotlin, Jetpack Compose, Material 3, Coil.  
- **Instalación:** Abrir el proyecto en Android Studio y sincronizar Gradle.  
- **Ejecución:** Ejecutar en un emulador o dispositivo físico con Android 8.0 o superior.  
  No requiere configuración adicional.

## 3. Arquitectura y flujo
El proyecto sigue una estructura basada en el patrón **MVVM (Model - View - ViewModel)**.

- **Estructura carpetas**
  /ui
  ┣ /screens → Contiene las pantallas principales (Login, Register, Profile, Home)
  ┣ /components → Elementos reutilizables como botones, tarjetas y diálogos
  ┣ /theme → Colores, tipografía y estilos
  /viewmodel → Maneja el estado y la lógica de cada pantalla
  /model → Clases de datos del usuario y entidades
  /repository → Fuente de datos y persistencia local
  /navigation → Controla el flujo de pantallas dentro de la app
  
- **Gestión de estado**:
Cada pantalla tiene su propio ViewModel que expone un estado inmutable (UiState).  
Se usan funciones collectAsState() para reaccionar a los cambios y mostrar contenido dinámico (como carga, error o éxito).

- **Navegación**: 
Se utiliza NavHostController con rutas definidas para moverse entre Login, Registro, Home y Perfil.

## 4. Funcionalidades

- **Formulario validado:** el registro y login validan que los campos no estén vacíos y muestran errores visuales.
- **Navegación fluida:** entre pantallas de login, registro, perfil y home.
- **Gestión de estado:** cada ViewModel maneja los estados de carga y éxito.
- **Persistencia local:** guarda temporalmente la sesión y datos del usuario.
- **Almacenamiento de imagen de perfil:** permite seleccionar una imagen desde la galería o tomar una foto con la cámara.
- **Recursos nativos:** usa los permisos de cámara y almacenamiento con `ActivityResultLauncher`.
- **Animaciones:** transiciones suaves y efectos en botones o cambios de imagen.

## 5. Endpoints

No supe como conectarme a la api

## 6. User flows

El flujo principal de la aplicación es el siguiente:

1. El usuario abre la app y llega a la pantalla de **Login**.  
2. Si no tiene cuenta, puede ir a **Registro**, llenar el formulario y volver al Login.  
3. Una vez dentro, accede a la **Home**, desde donde puede ir a su **Perfil**.  
4. En el **Perfil**, puede cambiar su foto seleccionando una imagen o usando la cámara.  
5. El usuario puede cerrar sesión desde el perfil y volver al inicio.
