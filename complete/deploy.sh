#!/bin/bash

# Crear directorio para la aplicación
mkdir -p ~/app/backups
cd ~/app

# Descargar docker-compose.prod.yml
# Reemplaza la URL con la ruta correcta a tu archivo docker-compose.prod.yml
curl -o docker-compose.prod.yml https://raw.githubusercontent.com/treswilmer3-dotcom/Evalucion/main/complete/docker-compose.prod.yml

# Detener y eliminar contenedores existentes
docker-compose -f docker-compose.prod.yml down

# Limpiar recursos no utilizados
docker system prune -f

# Extraer las imágenes más recientes
docker pull wilinvestiga/spring-boot-app:v1.0.0
docker pull postgres:15-alpine

# Iniciar los servicios
docker-compose -f docker-compose.prod.yml up -d

echo "¡La aplicación se ha desplegado correctamente en el puerto 8282!"
