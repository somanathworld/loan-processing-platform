#!/bin/bash

echo "🚀 Starting Minikube..."
minikube start

echo "🔧 Configuring Docker environment to use Minikube's Docker daemon..."
eval $(minikube docker-env)

echo "🐳 Building Docker images..."
docker build -t discovery-server:1.0 ./discovery-server
docker build -t loan-service:1.0 ./loan-service
docker build -t auth-service:1.0 ./auth-service
docker build -t user-service:1.0 ./user-service
docker build -t api-gateway:1.0 ./api-gateway

echo "📦 Applying Kubernetes deployment and service files from ./k8s/..."

# Discovery
kubectl apply -f k8s/discovery-deployment.yaml
kubectl apply -f k8s/discovery-service.yaml

# Loan
kubectl apply -f k8s/loan-deployment.yaml
kubectl apply -f k8s/loan-service.yaml

# Auth
kubectl apply -f k8s/auth-deployment.yaml
kubectl apply -f k8s/auth-service.yaml

# User
kubectl apply -f k8s/user-deployment.yaml
kubectl apply -f k8s/user-service.yaml

# API Gateway
kubectl apply -f k8s/gateway-deployment.yaml
kubectl apply -f k8s/gateway-service.yaml

# Zipkin
kubectl apply -f k8s/zipkin-deployment.yaml
kubectl apply -f k8s/zipkin-service.yaml

# Zookeeper & Kafka
kubectl apply -f k8s/zookeeper-deployment.yaml
kubectl apply -f k8s/zookeeper-service.yaml
sleep 75
kubectl apply -f k8s/kafka-deployment.yaml
kubectl apply -f k8s/kafka-service.yaml

echo "⏳ Listing Pods..."
kubectl get pods

echo "🌐 API Gateway URL:"
minikube service gateway --url
