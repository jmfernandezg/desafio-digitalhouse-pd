# Desafío Digital House PD

Desafío Professional Digital House para la certificación Professional Developer

## Estructura del Proyecto

El proyecto está dividido en tres partes principales:

1. **Aplicación Spring Boot**: Es el backend de la aplicación, responsable de manejar la lógica de negocio y la persistencia de datos.
2. **Biblioteca Core**: Contiene código compartido y utilidades usadas tanto por el backend como por el frontend.
3. **Frontend en React JS**: Es el frontend de la aplicación, responsable de la interfaz de usuario y la experiencia del usuario.

## Scripts de Inicio

### Windows

Para iniciar la aplicación en Windows, puedes usar los siguientes scripts:

1. **Backend**:
    ```batch
    @echo off
    cd backend
    gradlew bootRun
    ```

2. **Frontend**:
    ```batch
    @echo off
    cd frontend
    npm install
    npm start
    ```

### Unix/Linux

Para iniciar la aplicación en sistemas Unix/Linux, puedes usar los siguientes scripts:

1. **Backend**:
    ```sh
    #!/bin/sh
    cd backend
    ./gradlew bootRun
    ```

2. **Frontend**:
    ```sh
    #!/bin/sh
    cd frontend
    npm install
    npm start
    ```

## Docker

También puedes usar Docker para ejecutar la aplicación. Aquí están los pasos:

1. **Construir y ejecutar el backend**:
    ```sh
    docker build -t backend -f Dockerfile.backend .
    docker run -p 8080:8080 backend
    ```

2. **Construir y ejecutar el frontend**:
    ```sh
    docker build -t frontend -f Dockerfile.frontend .
    docker run -p 80:80 frontend
    ```

3. **Ejecutar con Docker Compose**:
    ```sh
    docker-compose up --build
    ```

Este comando construirá e iniciará todos los servicios definidos en el archivo docker-compose.yml.

### Acceso a la aplicación:
- El backend estará disponible en http://localhost:8080
- El frontend estará disponible en http://localhost

Para detener los servicios:
1. Presiona Ctrl+C en la terminal donde se está ejecutando Docker Compose
2. Ejecuta: `docker-compose down` para detener y eliminar los contenedores creados

## Variables de Entorno

Asegúrate de configurar las variables de entorno necesarias en el archivo `.env` ubicado en el directorio `frontend`.

## Solución de Problemas Comunes

1. **Error de puertos en uso**:
   - Verifica que los puertos 8080 y 80 no estén siendo utilizados por otras aplicaciones
   - Puedes cambiar los puertos en el archivo docker-compose.yml si es necesario

2. **Problemas de permisos en Linux/Unix**:
   - Asegúrate de dar permisos de ejecución a los scripts:
     ```sh
     chmod +x ./gradlew
     ```

3. **Errores de npm**:
   - Limpia la caché de npm:
     ```sh
     npm clean-cache --force
     ```
   - Borra la carpeta node_modules y vuelve a instalar:
     ```sh
     rm -rf node_modules
     npm install
     ```

## Licencia

Este proyecto está bajo la Licencia MIT.

## Contacto y Soporte

Si encuentras algún problema o tienes sugerencias, por favor:
1. Abre un issue en el repositorio
2. Envía un correo al equipo de soporte
3. Consulta la documentación adicional en la wiki del proyecto