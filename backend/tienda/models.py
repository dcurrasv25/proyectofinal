from django.db import models
from django.contrib.auth.models import AbstractUser

class Usuario(AbstractUser):
    # AbstractUser ya incluye campos como 'username' (para el nombre de usuario), 
    # 'email' (para el correo), y 'password' de forma nativa.
    ROL_CHOICES = (
        ('admin', 'Administrador'),
        ('usuario', 'Usuario'),
    )
    rol = models.CharField(max_length=20, choices=ROL_CHOICES, default='usuario')

class Categoria(models.Model):
    id_categoria = models.AutoField(primary_key=True)
    nombre = models.CharField(max_length=100, unique=True)

    def __str__(self):
        return self.nombre

class Nota(models.Model):
    id_nota = models.AutoField(primary_key=True)
    nombre = models.CharField(max_length=100)

    def __str__(self):
        return self.nombre

class Perfume(models.Model):
    id_perfume = models.AutoField(primary_key=True)
    nombre = models.CharField(max_length=200)
    marca = models.CharField(max_length=100)
    tipo = models.CharField(max_length=100)
    genero = models.CharField(max_length=50)
    precio = models.DecimalField(max_digits=10, decimal_places=2)
    imagen = models.URLField(max_length=500, null=True, blank=True)
    
    # Relaciones
    categoria = models.ForeignKey(Categoria, on_delete=models.SET_NULL, null=True, blank=True, related_name='perfumes')
    notas = models.ManyToManyField(Nota, related_name='perfumes')

    def __str__(self):
        return f"{self.nombre} ({self.marca})"

# TABLA INTERMEDIA EXPLÍCITA PARA FAVORITOS
class PerfumeFavorito(models.Model):
    id_favorito = models.AutoField(primary_key=True)
    usuario = models.ForeignKey(Usuario, on_delete=models.CASCADE, related_name='perfumes_favoritos')
    perfume = models.ForeignKey(Perfume, on_delete=models.CASCADE)
    fecha_agregado = models.DateTimeField(auto_now_add=True)

    class Meta:
        unique_together = ('usuario', 'perfume')

    def __str__(self):
        return f"{self.usuario.username} - {self.perfume.nombre}"

class Compra(models.Model):
    id_compra = models.AutoField(primary_key=True)
    fecha = models.DateTimeField(auto_now_add=True)
    usuario = models.ForeignKey(Usuario, on_delete=models.CASCADE, related_name='compras')

    def __str__(self):
        return f"Compra {self.id_compra} - {self.usuario.username}"

class LineaPedido(models.Model):
    id_linea = models.AutoField(primary_key=True)
    compra = models.ForeignKey(Compra, on_delete=models.CASCADE, related_name='lineas')
    perfume = models.ForeignKey(Perfume, on_delete=models.RESTRICT, related_name='lineas_pedido')
    cantidad = models.IntegerField()
    precio_unitario = models.DecimalField(max_digits=10, decimal_places=2)

    def __str__(self):
        return f"Línea {self.id_linea} (Compra {self.compra.id_compra})"
