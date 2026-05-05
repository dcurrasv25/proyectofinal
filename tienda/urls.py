from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import (
    UsuarioViewSet, PerfumeViewSet, CategoriaViewSet, 
    PerfumeFavoritoViewSet, NotaViewSet, CompraViewSet, LineaPedidoViewSet
)

router = DefaultRouter()
router.register(r'users', UsuarioViewSet, basename='user')
router.register(r'perfumes', PerfumeViewSet, basename='perfume')
router.register(r'categorias', CategoriaViewSet, basename='categoria')
router.register(r'favoritos', PerfumeFavoritoViewSet, basename='perfumefavorito')
router.register(r'notas', NotaViewSet, basename='nota')
router.register(r'compras', CompraViewSet, basename='compra')
router.register(r'lineas_pedido', LineaPedidoViewSet, basename='lineapedido')

urlpatterns = [
    path('', include(router.urls)),
]
