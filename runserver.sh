git pull origin main
./gradlew clean build
docker-compose down
docker-compose up -d