#!/usr/bin/env bash
cf cs p-rabbitmq standard rabbit
cf cs p-circuit-breaker-dashboard standard breaker
cf cs p-service-registry standard registry
cf cs p-config-server standard config -c config-server-setup.json
