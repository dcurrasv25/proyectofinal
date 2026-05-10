from rest_framework import viewsets, status
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from django.shortcuts import get_object_or_404
from django.db.models import Count
from .models import Usuario, Perfume, Categoria, PerfumeFavorito, Nota, Compra, LineaPedido
from .serializers import (
    UsuarioSerializer, PerfumeSerializer, CategoriaSerializer, 
    PerfumeFavoritoSerializer, NotaSerializer, CompraSerializer, LineaPedidoSerializer,
    InicioSesionSerializer
)

class CategoriaViewSet(viewsets.ModelViewSet):
    queryset = Categoria.objects.all()
    serializer_class = CategoriaSerializer

    @action(detail=True, methods=['get'])
    def perfumes(self, request, pk=None):
        categoria = self.get_object()
        perfumes = categoria.perfumes.all()
        serializer = PerfumeSerializer(perfumes, many=True)
        return Response(serializer.data)

class PerfumeFavoritoViewSet(viewsets.ModelViewSet):
    queryset = PerfumeFavorito.objects.all()
    serializer_class = PerfumeFavoritoSerializer

class UsuarioViewSet(viewsets.ModelViewSet):
    queryset = Usuario.objects.all()
    serializer_class = UsuarioSerializer

    @action(detail=True, methods=['get'])
    def favoritos(self, request, pk=None):
        usuario = self.get_object()
        favoritos = usuario.perfumes_favoritos.all()
        # Obtenemos los objetos Perfume a partir de la tabla intermedia
        perfumes = [fav.perfume for fav in favoritos]
        serializer = PerfumeSerializer(perfumes, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['post', 'delete'], url_path=r'favoritos/(?P<perfume_id>\d+)')
    def modificar_favorito(self, request, pk=None, perfume_id=None):
        usuario = self.get_object()
        perfume = get_object_or_404(Perfume, pk=perfume_id)

        if request.method == 'POST':
            PerfumeFavorito.objects.get_or_create(usuario=usuario, perfume=perfume)
            return Response({'status': 'Perfume añadido a favoritos'}, status=status.HTTP_201_CREATED)
        elif request.method == 'DELETE':
            PerfumeFavorito.objects.filter(usuario=usuario, perfume=perfume).delete()
            return Response({'status': 'Perfume eliminado de favoritos'}, status=status.HTTP_204_NO_CONTENT)

    @action(detail=True, methods=['get'])
    def compras(self, request, pk=None):
        usuario = self.get_object()
        compras = usuario.compras.all()
        serializer = CompraSerializer(compras, many=True)
        return Response(serializer.data)

class PerfumeViewSet(viewsets.ModelViewSet):
    queryset = Perfume.objects.all()
    serializer_class = PerfumeSerializer

    @action(detail=True, methods=['get'])
    def notas(self, request, pk=None):
        perfume = self.get_object()
        notas = perfume.notas.all()
        serializer = NotaSerializer(notas, many=True)
        return Response(serializer.data)
    
    @action(detail=False, methods=['get'])
    def populares(self, request):
        perfumes = Perfume.objects.annotate(num_favoritos=Count('perfumefavorito')).order_by('-num_favoritos')[:5]
        serializer = PerfumeSerializer(perfumes, many=True)
        return Response(serializer.data)

class NotaViewSet(viewsets.ModelViewSet):
    queryset = Nota.objects.all()
    serializer_class = NotaSerializer

class CompraViewSet(viewsets.ModelViewSet):
    queryset = Compra.objects.all()
    serializer_class = CompraSerializer

class LineaPedidoViewSet(viewsets.ModelViewSet):
    queryset = LineaPedido.objects.all()
    serializer_class = LineaPedidoSerializer

class IniciarSesionView(ObtainAuthToken):
    serializer_class = InicioSesionSerializer

    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        token, created = Token.objects.get_or_create(user=user)
        return Response({
            'token': token.key,
            'id': user.id,
            'nombre_de_usuario': user.username
        })
