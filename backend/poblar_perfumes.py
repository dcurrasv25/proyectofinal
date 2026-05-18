import os
import django
import requests

# 1. Configurar el entorno de Django para poder usar los modelos
# Asegúrate de que 'backend.settings' sea el nombre correcto de tu archivo settings
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'backend.settings')
django.setup()

from tienda.models import Categoria, Nota, Perfume

def fetch_and_populate():
    """
    Función que hace una petición a una API de perfumes y guarda los datos
    en la base de datos de la aplicación teniendo en cuenta los modelos
    (Categoria, Nota, y Perfume).
    """
    
    # URL de ejemplo de tu API (sustitúyela por la real)
    api_url = "https://api.example.com/v1/perfumes"
    
    # Headers opcionales en caso de que la API requiera autenticación (ej: API Key)
    headers = {
        # "Authorization": "Bearer TU_TOKEN_AQUI"
    }
    
    print("Iniciando la descarga e inserción de datos...")

    try:
        # --- DESCOMENTA LAS SIGUIENTES LÍNEAS PARA USAR LA API REAL ---
        # response = requests.get(api_url, headers=headers)
        # response.raise_for_status() # Verifica que la petición fue exitosa (código 200)
        # data = response.json()
        
        # --- DATOS DE EJEMPLO ---
        # Como no indicaste qué API exacta usas, aquí tienes un ejemplo de la estructura 
        # que podría devolver (modifica los nombres de los campos de acuerdo a tu API real)
        data = [
            {
                "name": "Sauvage",
                "brand": "Dior",
                "type": "Eau de Toilette",
                "gender": "Hombre",
                "price": "95.50",
                "category": "Cítrico",
                "notes": ["Bergamota", "Pimienta", "Ambroxan"],
                "image": "https://m.media-amazon.com/images/I/61k99O1X5BL._SL1000_.jpg"
            },
            {
                "name": "La Vie Est Belle",
                "brand": "Lancôme",
                "type": "Eau de Parfum",
                "gender": "Mujer",
                "price": "80.00",
                "category": "Floral",
                "notes": ["Iris", "Jazmín", "Flor de azahar"],
                "image": "https://m.media-amazon.com/images/I/61B1v0D2YDL._SL1500_.jpg"
            },
            {
                "name": "Acqua di Giò",
                "brand": "Giorgio Armani",
                "type": "Eau de Toilette",
                "gender": "Hombre",
                "price": "75.00",
                "category": "Acuático",
                "notes": ["Notas marinas", "Lima", "Madera de cedro"],
                "image": "https://m.media-amazon.com/images/I/51rP0XwX7oL._SL1000_.jpg"
            },
            {
                "name": "Baccarat Rouge 540",
                "brand": "Maison Francis Kurkdjian",
                "type": "Eau de Parfum",
                "gender": "Unisex",
                "price": "250.00",
                "category": "Amaderado",
                "notes": ["Azafrán", "Jazmín", "Ámbar gris"],
                "image": "https://m.media-amazon.com/images/I/61Xz9X7l9oL._SL1500_.jpg"
            },
            {
                "name": "Black Opium",
                "brand": "Yves Saint Laurent",
                "type": "Eau de Parfum",
                "gender": "Mujer",
                "price": "90.00",
                "category": "Oriental",
                "notes": ["Café", "Vainilla", "Flor de azahar"],
                "image": "https://m.media-amazon.com/images/I/61r5qZgA5vL._SL1500_.jpg"
            },
            {
                "name": "1 Million",
                "brand": "Paco Rabanne",
                "type": "Eau de Toilette",
                "gender": "Hombre",
                "price": "85.00",
                "category": "Especiado",
                "notes": ["Pomelo", "Canela", "Cuero"],
                "image": "https://m.media-amazon.com/images/I/51d6vA2wJ8L._SL1000_.jpg"
            },
            {
                "name": "Chanel No 5",
                "brand": "Chanel",
                "type": "Eau de Parfum",
                "gender": "Mujer",
                "price": "140.00",
                "category": "Aldehídico",
                "notes": ["Aldehídos", "Rosa", "Sándalo"],
                "image": "https://m.media-amazon.com/images/I/71R2I2J1wZL._SL1500_.jpg"
            },
            {
                "name": "Terre d'Hermès",
                "brand": "Hermès",
                "type": "Eau de Toilette",
                "gender": "Hombre",
                "price": "110.00",
                "category": "Amaderado",
                "notes": ["Naranja", "Pimienta", "Vetiver"],
                "image": "https://m.media-amazon.com/images/I/61uYgVjOa3L._SL1500_.jpg"
            }
        ]
        
        # Recorremos cada perfume que nos devuelve la API
        for item in data:
            # === 1. Gestionar Categoría (Relación OneToMany / ForeignKey) ===
            # Extraemos el nombre de la categoría (ajusta 'category' por el nombre de la key de tu API)
            categoria_nombre = item.get('category')
            categoria_obj = None
            
            if categoria_nombre:
                # get_or_create busca la categoría. Si no existe, la crea.
                categoria_obj, created = Categoria.objects.get_or_create(nombre=categoria_nombre)
            
            # === 2. Gestionar Perfume ===
            # Creamos o actualizamos el perfume. 
            # Usamos 'nombre' y 'marca' como identificadores únicos para no duplicar.
            perfume_obj, created = Perfume.objects.get_or_create(
                nombre=item.get('name'),
                marca=item.get('brand'),
                defaults={
                    'tipo': item.get('type', 'Desconocido'),
                    'genero': item.get('gender', 'Unisex'),
                    'precio': item.get('price', 0.00),
                    'categoria': categoria_obj,
                    'imagen': item.get('image', '')
                }
            )
            
            if created:
                print(f"[OK] Nuevo perfume insertado: {perfume_obj.nombre} ({perfume_obj.marca})")
            else:
                print(f"[INFO] El perfume ya existía: {perfume_obj.nombre} ({perfume_obj.marca})")
                
            # === 3. Gestionar Notas (Relación ManyToMany) ===
            # Extraemos la lista de notas del perfume
            notas_lista = item.get('notes', [])
            
            for nombre_nota in notas_lista:
                # Buscamos o creamos la nota
                nota_obj, created = Nota.objects.get_or_create(nombre=nombre_nota)
                
                # Añadimos la nota a la relación ManyToMany del perfume
                perfume_obj.notas.add(nota_obj)

        print("\n[EXITO] ¡Proceso de importación finalizado con éxito!")

    except requests.exceptions.RequestException as e:
        print(f"[ERROR] Error al conectar con la API: {e}")
    except Exception as e:
        print(f"[ERROR] Ocurrió un error inesperado: {e}")

if __name__ == '__main__':
    fetch_and_populate()
