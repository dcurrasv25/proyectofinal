from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import UsuarioViewSet, PerfumeViewSet, PerfumeFavoritoViewSet

router = DefaultRouter()
router.register(r'users', UsuarioViewSet, basename='user')
router.register(r'perfumes', PerfumeViewSet, basename='perfume')
router.register(r'favoritos', PerfumeFavoritoViewSet, basename='perfumefavorito')

urlpatterns = [
    path('', include(router.urls)),
]
