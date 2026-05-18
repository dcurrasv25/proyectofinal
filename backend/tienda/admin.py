from django.contrib import admin
from .models import Usuario, Nota, Perfume, Compra, LineaPedido, Categoria, PerfumeFavorito

# Registrar los modelos en el panel de administración
admin.site.register(Categoria)
admin.site.register(Usuario)
admin.site.register(PerfumeFavorito)
admin.site.register(Nota)
admin.site.register(Perfume)
admin.site.register(Compra)
admin.site.register(LineaPedido)
