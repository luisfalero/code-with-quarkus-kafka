# code-with-quarkus-kafka

https://es.quarkus.io/guides/podman

```bash
export DOCKER_HOST=unix:///run/user/1000/podman/podman.sock
export TESTCONTAINERS_RYUK_DISABLED=true
```

Para empezar a gestionar el cluster, primero hay que descargar el certificado de la siguente manera:

Openshift login
```bash
oc login --token=sha256~xxxyyyzzz --server=https://api.cluster-xl8x9.xl8x9.sandbox2914.opentlc.com:6443
```

Variables de entorno necesarias:

```bash
BOOTSTRAP_SERVER_URL=$(oc get routes my-cluster-kafka-tls-bootstrap -n amq-streams -o=jsonpath='{.status.ingress[0].host}{"\n"}')
```

Obtenemos el certificado autofirmado del cluster
```bash
oc extract secret/my-cluster-cluster-ca-cert -n amq-streams --keys=ca.crt --to=- > ca.crt
```

Creamos el truststore.jks

```bash
keytool -keystore truststore.jks -alias CARoot -import -file ca.crt -keypass redhat01 -storepass redhat01
```

Creamos archivo de configuración del cliente

```bash
echo "
bootstrap.servers=${BOOTSTRAP_SERVER_URL}:443
security.protocol=SSL
ssl.truststore.location=$PWD/truststore.jks
ssl.truststore.password=redhat01
" > client.properties
```

Crear topic

```bash
./kafka-3.7.0-src/bin/kafka-topics.sh --create --bootstrap-server ${BOOTSTRAP_SERVER_URL}:443 \
  --command-config client.properties --replication-factor 3 --partitions 3 --topic my-topic
```

Listado de topics

```bash
./kafka-3.7.0-src/bin/kafka-topics.sh --bootstrap-server ${BOOTSTRAP_SERVER_URL}:443  \
  --command-config client.properties --list
```