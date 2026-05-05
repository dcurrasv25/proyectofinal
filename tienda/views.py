from rest_framework import viewsets
from .models import Usuario, Perfume
from .serializers import UsuarioSerializer, PerfumeSerializer

class UsuarioViewSet(viewsets.ModelViewSet):
    queryset = Usuario.objects.all()
    serializer_class = UsuarioSerializer

class PerfumeViewSet(viewsets.ModelViewSet):
    queryset = Perfume.objects.all()
    serializer_class = PerfumeSerializer
