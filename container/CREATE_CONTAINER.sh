#!/bin/sh

clear
S_TIME=$(date +%s)
CUR_DIR=$(cd $(dirname $0); pwd)

echo "############################################################"
echo "# START SCRIPT"
echo "############################################################"

echo "\n### START: Destory existing containers ##########"
docker-compose down -v --remove-orphans

#	echo "\n### START: Delete the existing data directory for Tempo ##########"
#	#	TMP_DAT_DIR=$CUR_DIR/tempo/data/
#	#	if [ -d $TMP_DAT_DIR ]; then
#	#		sudo rm -rf $TMP_DAT_DIR
#	#	fi
#	#	sudo rm -rf $CUR_DIR/mimir/data/
#	#	sudo rm -rf $CUR_DIR/minio/data/
#
#	sudo rm -rf $CUR_DIR/mimir/data/
#	sudo rm -rf $CUR_DIR/minio/data/
#	sudo rm -rf $CUR_DIR/tempo/data/
#
#	#	echo "\n### START: Install a plugin to collect container's logs ##########"
#	#	docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions

echo "\n### START: Create new containers ##########"
docker-compose up -d -V --remove-orphans

echo "\n### START: Show a list of container ##########"
docker ps -a

cat << EOS

/************************************************************
 * Information:
 * - Access to Web ui tools with the URL below.
 *   - Grafana:    http://localhost:3000
 *     - dashboard(Prometheus): https://grafana.com/grafana/dashboards/4701-jvm-micrometer/
 *     - dashboard(Micrometer): https://grafana.com/grafana/dashboards/20352-opentelemetry-jvm-micrometer/
 *   - Prometheus: http://localhost:9090 for Mimir
 *   - Mimir:      http://localhost:9009
 *   - MINIO:      http://localhost:9001 for Mimir
 *   - MINIO:      http://localhost:9002 for Loki
 *   - MINIO:      http://localhost:9003 for Tempo
 *   - Pyroscope   http://localhost:4040
 *   - Jaeger:     http://localhost:16686
 *   - Zipkin:     http://localhost:9411
 * - Access to Monitored servers with the URL below.
 *   - webui:      http://localhost:8081
 *   - micrometer: http://localhost:8081/actuator for webui
 *   - webapi:     http://localhost:8082/api/dice/v1/roll
 *   - webapi:     http://localhost:8082/api/dice/v1/list
 ***********************************************************/

EOS


E_TIME=$(date +%s)
DURATION=$((E_TIME - S_TIME))

echo "############################################################"
echo "# FINISH SCRIPT ($DURATION seconds)"
echo "############################################################"
