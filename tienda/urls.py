from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import UsuarioViewSet, PerfumeViewSet

router = DefaultRouter()
router.register(r'users', UsuarioViewSet, basename='user')
router.register(r'perfumes', PerfumeViewSet, basename='perfume')

urlpatterns = [
    path('', include(router.urls)),
]
