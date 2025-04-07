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
