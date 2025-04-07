# its-just-5-minutes

## Description

Create application within K3d cluster with Jenkins (dynamic worker pods), PostgreSQL, and 
Grafana - all exposed via Traefik.


## Running

$ ./create_cluster.sh

```
Creating storage folders if not exists...
Creating k3d cluster...
INFO[0000] portmapping '8000:80' targets the loadbalancer: defaulting to [servers:*:proxy agents:*:proxy]
INFO[0000] portmapping '8443:443' targets the loadbalancer: defaulting to [servers:*:proxy agents:*:proxy]
INFO[0000] Prep: Network
INFO[0000] Created network 'k3d-dev-cluster'
INFO[0000] Created image volume k3d-dev-cluster-images
INFO[0000] Starting new tools node...
INFO[0000] Starting node 'k3d-dev-cluster-tools'
INFO[0001] Creating node 'k3d-dev-cluster-server-0'
INFO[0001] Creating node 'k3d-dev-cluster-agent-0'
INFO[0001] Creating node 'k3d-dev-cluster-agent-1'
INFO[0001] Creating LoadBalancer 'k3d-dev-cluster-serverlb'
INFO[0001] Using the k3d-tools node to gather environment information
INFO[0001] Starting new tools node...
INFO[0001] Starting node 'k3d-dev-cluster-tools'
INFO[0002] Starting cluster 'dev-cluster'
INFO[0002] Starting servers...
INFO[0002] Starting node 'k3d-dev-cluster-server-0'
INFO[0004] Starting agents...
INFO[0004] Starting node 'k3d-dev-cluster-agent-0'
INFO[0004] Starting node 'k3d-dev-cluster-agent-1'
INFO[0006] Starting helpers...
INFO[0006] Starting node 'k3d-dev-cluster-serverlb'
INFO[0012] Injecting records for hostAliases (incl. host.k3d.internal) and for 5 network members into CoreDNS configmap...
INFO[0014] Cluster 'dev-cluster' created successfully!
INFO[0014] You can now use it like this:
```

## Explain and Modify

### Persistent Storage

define the location on the host where persistent storage lies. 
I am using local storage class, manually set up. Change the K3D_Storage_DIR to 
put this where you want, but by default it will go into user home directory under ".k3d/storage

*K3D_STORAGE_DIR="${K3D_STORAGE_DIR:-$HOME/.k3d/storage}"*

```
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
```

### Port usage on my MacBook

My MacBook is a piece of archeological history... it has evolved with me.
even when Apple called, apologizing to me on several occasions, telling me that they
were sorry but they could not longer support my previous MacBooks (because I was apparently
the only person left still using one) the solution was to migrate what I had rather than 
start clean.  So, all the glitches, personality, and former work with customers and clients
still remains!

Anyway... k3d had some trouble finding available ports it could use, so I made 3 available.  
I think, for a normal person, you can run without specifying ports and you will be good...
me? not so much.

But, just know that you cannot 'easily' change ports once the cluster is up and running.
You can do 'surgery' and mess with the docker containers and network and maybe get it working
but I don't have the attention span.  It is just easier to destroy and recreate with different ports.


## Project Layout

```
.
├── README.md
├── create_cluster.sh
├── jobs
│   └── dsl.groovy
├── manifests
│   ├── configmap-init.sql.yaml
│   ├── ingress-jenkins.yaml
│   ├── jenkins-secret.yaml
│   ├── postgres-secret.yaml
│   ├── pv-jenkins.yaml
│   └── pv-postgres.yaml
└── values
    ├── jenkins-values.yaml
    └── postgres-values.yaml

3 directories, 11 files
```
