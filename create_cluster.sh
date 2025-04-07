#!/bin/bash
set -e

K3D_STORAGE_DIR="${K3D_STORAGE_DIR:-$HOME/.k3d/storage}"

echo "Creating storage folders if not exists..."
mkdir -p "${K3D_STORAGE_DIR}/postgres"
mkdir -p "${K3D_STORAGE_DIR}/jenkins"

echo "Creating k3d cluster..."
k3d cluster create dev-cluster \
  --api-port 6443 \
  --servers 1 \
  --agents 2 \
  --port "8000:80@loadbalancer" \
  --port "8443:443@loadbalancer" \
  --volume "${K3D_STORAGE_DIR}/postgres:/mnt/data/postgres@server:0" \
  --volume "${K3D_STORAGE_DIR}/jenkins:/mnt/data/jenkins@server:0"

# Create namespaces for postgres, jenkins, and traefik
kubectl create namespace postgres
kubectl create namespace jenkins
kubectl create namespace ingress

echo "Applying PVs and Secrets..."
kubectl apply -f manifests/pv-postgres.yaml
kubectl apply -f manifests/pv-jenkins.yaml
kubectl apply -f manifests/postgres-secret.yaml
kubectl apply -f manifests/jenkins-secret.yaml
kubectl apply -f manifests/configmap-init.sql.yaml

echo "Installing PostgreSQL via Helm..."
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm upgrade --install postgres bitnami/postgresql \
  --namespace postgres \
  --create-namespace \
  -f values/postgres-values.yaml

echo "Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=Ready --timeout=600s pod/postgres-postgresql-0 -n postgres


echo "Installing Jenkins via Helm..."
helm repo add jenkins https://charts.jenkins.io
helm repo update
helm upgrade --install jenkins jenkins/jenkins \
  --namespace jenkins \
  -f values/jenkins-values.yaml

echo "Waiting for Jenkins to be ready..."
#kubectl wait --for=condition=Ready --timeout=600s deployment/jenkins -n jenkins 
kubectl wait --for=condition=Ready --timeout=600s pod/jenkins-0 -n jenkins 

echo "Applying Ingress..."
kubectl apply -f manifests/ingress-jenkins.yaml

echo "Done. Jenkins should be available at: http://localhost:8000/jenkins"

