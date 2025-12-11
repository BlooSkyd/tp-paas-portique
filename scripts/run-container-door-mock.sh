docker run -d \
  -e MQTT_BROKER_HOST=172.31.249.144 \
  -e MQTT_BROKER_PORT=8883 \
  -e MQTT_BADGE_TOPIC="door/open" \
  smailliwdocker/entrance-door-lock:latest
