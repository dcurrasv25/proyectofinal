from rest_framework import serializers
from .models import Usuario, Perfume

class UsuarioSerializer(serializers.ModelSerializer):
    class Meta:
        model = Usuario
        fields = '__all__'

class PerfumeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Perfume
        fields = '__all__'

from .models import PerfumeFavorito
class PerfumeFavoritoSerializer(serializers.ModelSerializer):
    class Meta:
        model = PerfumeFavorito
        fields = '__all__'
