
# {{{ start_banner()
start_banner()
{
	echo "############################################################"
	echo "# START SCRIPT"
	echo "############################################################"
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


# {{{ create_container()
# $1: the current directory
create_container()
{
	CUR_DIR=$1
	echo "\n### START: Create new containers ##########"
	git submodule update --init
	git submodule update --remote ./../webapp
	docker-compose \
		-f $CUR_DIR/docker-compose.yml \
		-f $CUR_DIR/docker-compose-webapp.yml \
		up -d -V --remove-orphans
}
# }}}

# {{{ create_container_except_webapp()
# $1: the current directory
create_container_except_webapp()
{
	CUR_DIR=$1
	echo "\n### START: Create new containers except for the webapps ##########"
	docker-compose \
		-f $CUR_DIR/docker-compose.yml \
		up -d -V
}
# }}}

# {{{ destory_container()
# $1: the current directory
destory_container()
{
	CUR_DIR=$1
	echo "\n### START: Destory existing containers ##########"
	docker-compose \
		-f $CUR_DIR/docker-compose.yml \
		-f $CUR_DIR/docker-compose-webapp.yml \
		down -v --remove-orphans
}
# }}}

# {{{ destory_container_except_webapp()
# $1: the current directory
destory_container_except_webapp()
{
	CUR_DIR=$1
	echo "\n### START: Destory existing containers except for the webapps ##########"
	docker-compose \
		-f $CUR_DIR/docker-compose.yml \
		down -v
}
# }}}

# {{{ rebuild_container()
# $1: the current directory
# $2: the name of container to rebuild
rebuild_container()
{
	CUR_DIR=$1
	CONTAINER_NM=$2
	echo "\n### START: Rebuild a container ##########"
	docker stop $CONTAINER_NM
	IMAGE_NM=$(docker inspect --format='{{.Config.Image}}' $CONTAINER_NM)
	docker rm $CONTAINER_NM
	docker rmi $IMAGE_NM
	docker-compose \
		-f $CUR_DIR/docker-compose.yml \
		-f $CUR_DIR/docker-compose-webapp.yml \
		up -d -V --build $CONTAINER_NM
}
# }}}


# {{{ install_plugin_collectiong_log()
install_plugin_collectiong_log()
{
	echo "\n### START: Install a plugin to collect container's logs ##########"
	docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions
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
 * - Access to Grafana Web ui tools with the URL below.
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
 * - Access to Monitored servers with the URL below.
 *   - webui:      http://localhost:8181
 *   - webapi:     http://localhost:8182/api/dice/v1/roll
 *   - webapi:     http://localhost:8182/api/dice/v1/list
 *   - micrometer: http://localhost:8181/actuator for webui
 *   - micrometer: http://localhost:8182/actuator for webapi
 * - Access to useful Web ui tools with the URL below.
 *   - MailDev:    http://localhost:1080
 *   - Jaeger:     http://localhost:16686
 *   - Zipkin:     http://localhost:9411
 ***********************************************************/

EOS
}
# }}}

# {{{ show_usage()
show_usage()
{
	cat << EOS
Usage: $(basename $0) [options]

Start the containers needed for the hands-on. If there are any containers
already running, stop them and remove resources beforehand.

Options:
  up                    Start the containers.
  up-to-jenkins         Start the containers except for the webapps. (Use this
                        option when adding containers to the Jenkins hands-on.)
  down                  Stop the containers and remove resources.
  down-from-jenkins     Stop the containers except for the webapps. (Use this
                        option when removing containers from the Jenkins hands-on.)
  rebuild {container}   Stop the specified container, removes its image, and
                        restarts it.
  list                  Show the list of containers.
  info                  Show the information such as URLs.

EOS
}
# }}}
