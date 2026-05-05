from django.db import models

class Categoria(models.Model):
    id_categoria = models.AutoField(primary_key=True)
    nombre = models.CharField(max_length=100, unique=True)

    def __str__(self):
        return self.nombre

class Usuario(models.Model):
    # Django crea el campo 'id' de forma automática y lo asigna como PK por defecto.
    nombre_de_usuario = models.CharField(max_length=150, unique=True)
    gmail = models.EmailField(unique=True)
    
    # Aquí el usuario guarda sus perfumes favoritos a través de la tabla intermedia explícita
    perfumes_favoritos = models.ManyToManyField('Perfume', through='PerfumeFavorito', related_name='favorito_de', blank=True)

    def __str__(self):
        return self.nombre_de_usuario

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
    
    # Relaciones
    categoria = models.ForeignKey(Categoria, on_delete=models.SET_NULL, null=True, blank=True, related_name='perfumes')
    notas = models.ManyToManyField(Nota, related_name='perfumes')

    def __str__(self):
        return f"{self.nombre} ({self.marca})"

# TABLA INTERMEDIA EXPLÍCITA PARA FAVORITOS
class PerfumeFavorito(models.Model):
    id_favorito = models.AutoField(primary_key=True)
    usuario = models.ForeignKey(Usuario, on_delete=models.CASCADE)
    perfume = models.ForeignKey(Perfume, on_delete=models.CASCADE)
    fecha_agregado = models.DateTimeField(auto_now_add=True)

    class Meta:
        unique_together = ('usuario', 'perfume')

    def __str__(self):
        return f"{self.usuario.nombre_de_usuario} - {self.perfume.nombre}"

class Compra(models.Model):
    id_compra = models.AutoField(primary_key=True)
    fecha = models.DateTimeField(auto_now_add=True)
    usuario = models.ForeignKey(Usuario, on_delete=models.CASCADE, related_name='compras')

    def __str__(self):
        return f"Compra {self.id_compra} - {self.usuario.nombre_de_usuario}"

class LineaPedido(models.Model):
    id_linea = models.AutoField(primary_key=True)
    compra = models.ForeignKey(Compra, on_delete=models.CASCADE, related_name='lineas')
    perfume = models.ForeignKey(Perfume, on_delete=models.RESTRICT, related_name='lineas_pedido')
    cantidad = models.IntegerField()
    precio_unitario = models.DecimalField(max_digits=10, decimal_places=2)

    def __str__(self):
        return f"Línea {self.id_linea} (Compra {self.compra.id_compra})"
