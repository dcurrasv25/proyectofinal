# API Tienda de Perfumes

Backend desarrollado con **Django** y **Django REST Framework** para la gestion de una tienda de perfumes. Este proyecto proporciona una API robusta para la administracion de usuarios, autenticacion mediante tokens, gestion de un catalogo de perfumes (con categorias y notas aromaticas), sistema de favoritos, y procesamiento de compras.

---

## Caracteristicas Principales

- **Autenticacion Segura:** Registro de usuarios e inicio de sesion basado en `TokenAuthentication` nativo de Django REST Framework.
- **Catalogo de Perfumes:** Listado completo de perfumes, notas aromaticas y categorias.
- **Sistema de Favoritos:** Cada usuario puede agregar y eliminar perfumes de su lista personal de favoritos.
- **Perfumes Populares:** Endpoint estadistico que calcula en tiempo real los perfumes mas agregados a favoritos por la comunidad.
- **Gestion de Pedidos:** Registro de transacciones de compras y lineas de pedido detalladas.
- **Script de Autopoblado:** Incluye un script (`poblar_perfumes.py`) para popular facilmente la base de datos con un catalogo inicial desde una API externa o datos de prueba.

---

## Tecnologias Utilizadas

- **Python 3.x**
- **Django** (Framework Web)
- **Django REST Framework (DRF)** (Construccion de la API)
- **SQLite3** (Base de datos por defecto, facilmente escalable a PostgreSQL/MySQL)

---

## Instalacion y Configuracion

Sigue estos pasos para levantar el entorno de desarrollo en tu maquina local:

### 1. Clonar el repositorio
```bash
git clone https://github.com/dcurrasv25/proyectofinal
cd proyectofinal
```

### 2. Crear y activar el entorno virtual
Es una buena practica utilizar un entorno virtual para aislar las dependencias:
```bash
# En Windows:
python -m venv venv
.\venv\Scripts\activate

# En macOS/Linux:
python3 -m venv venv
source venv/bin/activate
```

### 3. Instalar dependencias
*(Asegurate de tener instalado Django y DRF. Si tienes un archivo requirements.txt usa pip install -r requirements.txt)*
```bash
pip install django djangorestframework
```

### 4. Aplicar las migraciones
Prepara la base de datos creando todas las tablas necesarias:
```bash
python manage.py makemigrations
python manage.py migrate
```

### 5. Poblar la base de datos (Opcional pero recomendado)
Ejecuta el script proporcionado para anadir categorias, notas y perfumes de ejemplo a tu base de datos:
```bash
python poblar_perfumes.py
```

### 6. Levantar el servidor
Inicia el servidor de desarrollo local:
```bash
python manage.py runserver
```
La API estara disponible en `http://127.0.0.1:8000/`.

---

## Documentacion de la API (Endpoints)

A continuacion se listan los endpoints principales de la API. **Recuerda:** Para las rutas protegidas, debes incluir el token en los headers de tu peticion:
`Authorization: Token <tu_token>`

### Usuarios y Autenticacion
- **POST `/usuarios/`** - Crear/registrar un nuevo usuario.
  *(Body: `nombre_de_usuario`, `correo`, `contrasena`)*
- **POST `/iniciar-sesion/`** - Iniciar sesion y obtener token de autenticacion.
  *(Body: `nombre_de_usuario`, `contrasena`)*

### Perfumes y Catalogo
- **GET `/perfumes/`** - Listar todos los perfumes.
- **GET `/perfumes/populares/`** - Obtener el Top 5 de perfumes con mas favoritos.
- **GET `/categorias/`** - Listar categorias de perfumes.
- **GET `/notas/`** - Listar notas aromaticas.

### Favoritos (Requiere Token)
- **GET `/usuarios/{id}/favoritos/`** - Listar los perfumes favoritos de un usuario.
- **POST `/usuarios/{id}/favoritos/{id_perfume}/`** - Anadir un perfume a la lista de favoritos.
- **DELETE `/usuarios/{id}/favoritos/{id_perfume}/`** - Eliminar un perfume de la lista de favoritos.

### Compras y Pedidos (Requiere Token)
- **POST `/compras/`** - Crear el encabezado de una compra.
  *(Body: `{"usuario": <id>}`)*
- **POST `/lineas_pedido/`** - Anadir una linea (producto) a una compra.
  *(Body: `compra`, `perfume`, `cantidad`, `precio_unitario`)*
