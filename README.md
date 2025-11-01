# Perfulandia App Mobile

## 1. Caso elegido y alcance
- **Caso:** Perfulandia SPA
- **Alcance EP3:** Diseño/UI, validaciones, navegación, estado, persistencia, recursos nativos, animaciones
  
## 2. Requisitos y ejecución
  
- **Stack:** <framework, librerías>
- **Instalación:** comandos
- **Ejecución:** comandos y perfiles

## 3. Arquitectura y flujo

- **Estructura carpetas** (pantallas, servicios, state, data/repos, navigation, components)
- **Gestión de estado**: estrategia (local/global), flujo de datos
- **Navegación**: stack/tabs/deep link

## 4. Funcionalidades

- Formulario validado (registro/otra entidad)
- Navegación y backstack
- Gestión de estado (carga/éxito/error)
- **Persistencia local** (CRUD) y **almacenamiento de imagen de perfil**
- **Recursos nativos**: cámara/galería (permisos y fallback)
- **Animaciones** con propósito
- **Consumo de API** (incluye `/me`)

## 5. Endpoints

**Base URL:** `https://x8ki-letl-twmt.n7.xano.io/api:Rfm_61dW`

| Método | Ruta         | Body                              | Respuesta 
| ------ | ------------ | --------------------------------- | ------------------------------------------- 
| POST   | /auth/signup | { name, email, password }         | 200 { authToken, user_id }         
| POST   | /auth/login  | { email, password }               | 200 { authToken, user_id }        
| GET    | /auth/me     | - (requiere header Authorization) | 200 { id, created_at, name, email, account_id, role }

## 6. User flows

- Diagramas/Descripción del flujo principal y casos de error
