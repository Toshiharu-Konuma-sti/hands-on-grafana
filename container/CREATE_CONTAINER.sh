#!/bin/sh

# {{{ start_banner()
start_banner()
{
	echo "############################################################"
	echo "# START SCRIPT"
	echo "############################################################"
}
# }}}

# {{{ destory_container()
destory_container()
{
echo "\n### START: Destory existing containers ##########"
docker-compose \
	-f docker-compose.yml \
	-f docker-compose-app.yml \
	down -v --remove-orphans
}
# }}}

# {{{ clean_up_volume()
# $1: a current directory
# $2: a volume folder to clean up
clean_up_volume()
{
	CUR_DIR=$1
	shift
	VOL_DIR="$@"
	echo "\n### START: Clean up a volume folder ##########"
	for VDIR in $VOL_DIR; do
		RM_DIR="$CUR_DIR/$VDIR"
		if [ -d $RM_DIR ]; then
			# sort a processing depend on a directory's owner(yourself or other).
			if [ "$(ls -ld $RM_DIR | awk '{print $3}')" = $USER ]; then
				rm -rf $RM_DIR
			else
				sudo rm -rf $RM_DIR
			fi
		fi
	done
}
# }}}

# {{{ install_plugin_collectiong_log()
install_plugin_collectiong_log()
{
	echo "\n### START: Install a plugin to collect container's logs ##########"
	docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions
}
# }}}

# {{{ create_container()
create_container()
{
echo "\n### START: Create new containers ##########"
docker-compose \
	-f docker-compose.yml \
	-f docker-compose-app.yml \
	up -d -V --remove-orphans
}
# }}}

# {{{ show_list_container()
show_list_container()
{
	echo "\n### START: Show a list of container ##########"
	docker ps -a
}
# }}}

# {{{ show_url()
show_url()
{
	cat << EOS

/************************************************************
 * Information:
 * - Access to Web ui tools with the URL below.
 *   - Grafana:    http://localhost:3000
 *     - dashboard(Prometheus):    https://grafana.com/grafana/dashboards/4701-jvm-micrometer/
 *     - dashboard(OpenTelemetry): https://grafana.com/grafana/dashboards/20352-opentelemetry-jvm-micrometer/
 *     - dashboard(Node Exporter): https://grafana.com/grafana/dashboards/1860-node-exporter-full/
 *   - Pyroscope:  http://localhost:4040
 *   - Alloy:      http://localhost:12345
 *   - Prometheus: http://localhost:9090 for Mimir
 *   - Mimir:      http://localhost:9009
 *   - MINIO:      http://localhost:9001 for Mimir
 *   - MINIO:      http://localhost:9002 for Loki
 *   - MINIO:      http://localhost:9003 for Tempo
 *   - MailDev:    http://localhost:1080
 *   - Jaeger:     http://localhost:16686
 *   - Zipkin:     http://localhost:9411
 * - Access to Monitored servers with the URL below.
 *   - webui:      http://localhost:8081
 *   - webapi:     http://localhost:8082/api/dice/v1/roll
 *   - webapi:     http://localhost:8082/api/dice/v1/list
 *   - micrometer: http://localhost:8081/actuator for webui
 *   - micrometer: http://localhost:8082/actuator for webapi
 ***********************************************************/

EOS
}
# }}}

# {{{ finish_banner()
# $1: time to start this script
finish_banner()
{
	S_TIME=$1
	E_TIME=$(date +%s)
	DURATION=$((E_TIME - S_TIME))
	echo "############################################################"
	echo "# FINISH SCRIPT ($DURATION seconds)"
	echo "############################################################"
}
# }}}

S_TIME=$(date +%s)
CUR_DIR=$(cd $(dirname $0); pwd)
VOL_DIR="
	mimir/data/mimir-1
	mimir/data/mimir-2
	tempo/data/tempo-1
	minio/data/mimir
	minio/data/loki
	minio/data/tempo
"
case "$1" in
	"down")
		clear
		start_banner
		destory_container
		clean_up_volume $CUR_DIR $VOL_DIR
		show_list_container
		finish_banner $S_TIME
		;;
	"up")
		clear
		start_banner
		create_container
		show_list_container
		show_url
		finish_banner $S_TIME
		;;
	"list")
		clear
		show_list_container
		;;
	"")
		clear
		start_banner
		destory_container
		clean_up_volume $CUR_DIR $VOL_DIR
		create_container
		show_list_container
		show_url
		finish_banner $S_TIME
		;;
	*)
		echo "Usage: $0 [down|up|list]"
		echo ""
		exit 1
		;;
esac
