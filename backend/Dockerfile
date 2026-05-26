# Usar una imagen base de Python oficial, ligera y segura
FROM python:3.11-slim

# Evitar que Python escriba archivos .pyc en disco y asegurar logs en tiempo real
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Instalar dependencias del sistema necesarias
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Copiar el archivo de dependencias e instalarlas
COPY requirements.txt /app/
RUN pip install --no-cache-dir -r requirements.txt

# Copiar el código fuente del backend al contenedor
COPY . /app/

# Exponer el puerto de desarrollo de Django
EXPOSE 8000

# Ejecutar migraciones, autopoblar si la BD está vacía y arrancar el servidor
CMD ["sh", "-c", "python manage.py migrate && python -c \"from tienda.models import Perfume; print(Perfume.objects.count())\" | grep -q '^0$' && python poblar_perfumes.py || echo 'Database already populated'; python manage.py runserver 0.0.0.0:8000"]
