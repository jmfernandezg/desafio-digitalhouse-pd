#!/bin/bash
echo "Building and starting the application..."
docker-compose up --build -d

echo "Application is starting..."
echo "Frontend will be available at http://localhost:3000"
echo "Backend will be available at http://localhost:8080"
echo "To stop the application, run: docker-compose down"