from rest_framework import viewsets, status
from rest_framework.decorators import action
from rest_framework.response import Response
from django.shortcuts import get_object_or_404
from .models import Usuario, Perfume, PerfumeFavorito
from .serializers import UsuarioSerializer, PerfumeSerializer, PerfumeFavoritoSerializer

class PerfumeFavoritoViewSet(viewsets.ModelViewSet):
    queryset = PerfumeFavorito.objects.all()
    serializer_class = PerfumeFavoritoSerializer

class UsuarioViewSet(viewsets.ModelViewSet):
    queryset = Usuario.objects.all()
    serializer_class = UsuarioSerializer

    @action(detail=True, methods=['get'])
    def favorites(self, request, pk=None):
        usuario = self.get_object()
        favoritos = usuario.perfumes_favoritos.all()
        serializer = PerfumeSerializer(favoritos, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['post', 'delete'], url_path=r'favorites/(?P<perfume_id>\d+)')
    def modify_favorite(self, request, pk=None, perfume_id=None):
        usuario = self.get_object()
        perfume = get_object_or_404(Perfume, pk=perfume_id)
        if request.method == 'POST':
            PerfumeFavorito.objects.get_or_create(usuario=usuario, perfume=perfume)
            return Response({'status': 'Añadido'}, status=status.HTTP_201_CREATED)
        elif request.method == 'DELETE':
            PerfumeFavorito.objects.filter(usuario=usuario, perfume=perfume).delete()
            return Response({'status': 'Eliminado'}, status=status.HTTP_204_NO_CONTENT)

class PerfumeViewSet(viewsets.ModelViewSet):
    queryset = Perfume.objects.all()
    serializer_class = PerfumeSerializer
