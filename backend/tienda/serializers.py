from rest_framework import serializers
from .models import Usuario, Perfume, Nota, Categoria, PerfumeFavorito, Compra, LineaPedido

class CategoriaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Categoria
        fields = '__all__'

class UsuarioSerializer(serializers.ModelSerializer):
    contrasena = serializers.CharField(write_only=True, source='password')
    correo = serializers.EmailField(source='email', required=False)
    nombre_de_usuario = serializers.CharField(source='username')
    rol = serializers.CharField(default='usuario', required=False)

    class Meta:
        model = Usuario
        fields = ('id', 'nombre_de_usuario', 'correo', 'contrasena', 'rol')

    def create(self, validated_data):
        user = Usuario.objects.create_user(
            username=validated_data['username'],
            email=validated_data.get('email', ''),
            password=validated_data['password'],
            rol=validated_data.get('rol', 'usuario')
        )
        return user

class InicioSesionSerializer(serializers.Serializer):
    nombre_de_usuario = serializers.CharField()
    contrasena = serializers.CharField(write_only=True)

    def validate(self, attrs):
        username = attrs.get('nombre_de_usuario')
        password = attrs.get('contrasena')

        from django.contrib.auth import authenticate
        if username and password:
            user = authenticate(request=self.context.get('request'),
                                username=username, password=password)
            if not user:
                raise serializers.ValidationError('Las credenciales son incorrectas.')
        else:
            raise serializers.ValidationError('Debe incluir "nombre_de_usuario" y "contrasena".')

        attrs['user'] = user
        return attrs

class NotaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Nota
        fields = '__all__'

class PerfumeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Perfume
        fields = '__all__'

class PerfumeFavoritoSerializer(serializers.ModelSerializer):
    class Meta:
        model = PerfumeFavorito
        fields = '__all__'

class LineaPedidoSerializer(serializers.ModelSerializer):
    perfume_detalle = PerfumeSerializer(source='perfume', read_only=True)
    
    class Meta:
        model = LineaPedido
        fields = '__all__'

class CompraSerializer(serializers.ModelSerializer):
    lineas = LineaPedidoSerializer(many=True, read_only=True)

    class Meta:
        model = Compra
        fields = '__all__'
