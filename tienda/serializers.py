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
