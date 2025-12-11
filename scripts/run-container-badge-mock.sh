docker run -d \
  --name badge-sensor-mock \
  -e MQTT_BROKER_HOST=172.31.249.144 \
  -e MQTT_BROKER_PORT=8883 \
  -e MQTT_BADGE_TOPIC="sensor/data" \
  smailliwdocker/badge-sensor-mock:latest
