Отправить сообщение

```shell
docker exec -ti kafka /usr/bin/kafka-console-producer --topic metric-mind-v1-in --bootstrap-server localhost:9092
```

Сообщение

```
{"requestType": "readTrack", "debug": {"mode": "real","stub": "success"},"track": {"id": 1}}
```