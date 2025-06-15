<h1 align="center" id="title">El Buen Sabor - API</h1>

<p align="center"><img src="https://i.postimg.cc/T3tMRcGx/El-Buen-Sabor-sin-fondo-nueva-3.png" alt="project-image"></p>

<p id="description">El Buen Sabor es un proyecto fullstack desarrollado en equipo para la gestión integral de un restaurante. Esta API RESTful, desarrollada en Java con Spring Boot, permite administrar productos, insumos, clientes, empleados, promociones, pedidos y su facturación.</p>

<p align="center">
  <img src="https://img.shields.io/badge/language-Java-blue.svg" alt="shields">
  <img src="https://img.shields.io/github/last-commit/Alvaro-Rubina/ElBuenSabor-API" alt="shields">
</p>

<h2>🛠️ Tecnologías y Arquitectura</h2>

El backend de <strong>El Buen Sabor</strong> está construido en Java utilizando Spring Boot y una arquitectura por capas bien definida para separar responsabilidades y facilitar el mantenimiento y la escalabilidad. Entre las principales tecnologías y patrones implementados se encuentran:

- <strong>Spring Boot</strong>: Framework principal utilizado para el desarrollo de la API.
- <strong>Base de datos MySQL</strong>: Persistencia de datos en una base MySQL.
- <strong>Arquitectura por capas</strong>: Separación entre controladores, servicios, repositorios, entidades y DTOs.
- <strong>MapStruct</strong>: Para el mapeo eficiente entre entidades y DTOs.
- <strong>Swagger</strong> <em>(En desarrollo)</em>: Documentación interactiva de la API.
- <strong>Redis</strong> <em>(En desarrollo)</em>: Implementación de cache para optimizar el acceso y respuesta de los datos.
- <strong>WebSockets</strong> <em>(En desarrollo)</em>: Para notificaciones en tiempo real y experiencia interactiva.
- <strong>Auth0</strong> <em>(En desarrollo)</em>: Integración para autenticación segura y centralizada.
- <strong>Mercado Pago Checkout Pro</strong> <em>(En desarrollo)</em>: Para la facturación y el cobro de pedidos a través de Mercado Pago.
- <strong>Docker</strong> <em>(En desarrollo)</em>: Para facilitar el despliegue y la portabilidad del proyecto.

---

<h2>✨ Características principales</h2>

- Gestión de productos, insumos y rubros.
- Administración de clientes, empleados y usuarios.
- Registro y seguimiento de pedidos.
- Facturación y emisión de comprobantes.
- Promociones y descuentos.
- Manejo de domicilios y direcciones de entrega.
- Búsquedas avanzadas (por denominación, rubro, etc).

---

<h2>🧩 Proyectos relacionados</h2>

El frontend de este sistema se encuentra en el siguiente repositorio:  
👉 <a href="https://github.com/diegoCardenas03/ElBuenSabor" target="_blank">ElBuenSabor (Frontend)</a>

---

<h2>⚡ Requisitos previos</h2>

- Java 17 o superior
- Maven
- MySQL en ejecución (configurar el acceso en <code>application.properties</code>)

---

<h2>📦 Cómo clonar y correr el proyecto</h2>

```bash
git clone https://github.com/Alvaro-Rubina/ElBuenSabor-API.git
cd ElBuenSabor-API
# Importante tener Java 17+ y Maven instalados
mvn clean install
mvn spring-boot:run
```

---

<h2>🗺️ Roadmap</h2>

- ✅ Arquitectura por capas y DTOs
- ✅ Mapeo entidades/DTOs con MapStruct
- ✅ CRUD de entidades principales
- ✅ Integración Mercado Pago Checkout Pro
- ✅ Autenticación con Auth0
- ✅ Implementación de WebSockets
- ⏳ Cache con Redis
- ⏳ Documentación con Swagger
- ⏳ Dockerización del proyecto

---

<h2>🎬 Próximamente</h2>

Se agregarán demostraciones del funcionamiento del proyecto y ejemplos de uso en futuras actualizaciones.
