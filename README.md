# credit-system

Tasarım Kabulleri:

- Kredi görüntüleme işlemlerinin, kredi yaratma işlemlerinden fazla kez yapılacağı kabul edilerek CQRS mimari seçildi.
- Writing işlemleri için postgresql, reading için mongo db kullanıldı
- Application mimari olarak DD tabanlı Clean Architecture kullanıldı
- Data Consistency için CDC tool Debezzium ve Kafka Kullanıldı
- Idempotency checking için cache olarak redis kullanıldı.
- Distributed lock mechanism ile ileride multiple instance olarak çalışma durumu göz önüne alındı ve concurrent read write engellendi.

Uygulamayı ayağa kaldırabilmek için docker compose kullanılıp sonrasında debezium'a aşşağıdaki istek gönderilmelidir

Debezium Curl:

curl --location 'http://localhost:8083/connectors/' \
--header 'Content-Type: application/json' \
--data '{
"name": "postgre-connector",
"config": {
"connector.class": "io.debezium.connector.postgresql.PostgresConnector",
"plugin.name": "pgoutput",
"database.hostname": "postgres",
"database.port": "5432",
"database.user": "postgres",
"database.password": "postgres",
"database.dbname": "creditdb",
"database.server.name": "postgres",
"table.include.list": "public.credits,public.installments",
"heartbeat.interval.ms": 5000,
"database.history.kafka.bootstrap.servers": "kafka:9092",
"key.converter": "org.apache.kafka.connect.json.JsonConverter",
"key.converter.schemas.enable": false,
"value.converter": "org.apache.kafka.connect.json.JsonConverter",
"value.converter.schemas.enable": false,
"topic.prefix": "postgres"
}
}'

Örnek Curller:

Register Credit:

curl --location 'http://localhost:4567/api/v1/credit/register' \
--header 'Content-Type: application/json' \
--data '{
"userId": "b9e661d3-4ca3-4be1-9887-0bfb128a7b15",
"amount": 1000.00,
"installmentCount": 3
}'

Pay Installment:

curl --location 'http://localhost:4567/api/v1/credit/pay' \
--header 'UUID: d24b5d5e-9f1a-4e16-8fd0-d16ae5fbb7d6' \
--header 'Content-Type: application/json' \
--data '{
"creditId": "c142b87a-d3e4-47e0-8a8a-f3cd01200cea",
"installmentId": "e981ed68-4e6f-4f64-b19b-8c74ee760bbc",
"amount": 333.33
}'

curl --location 'http://localhost:4568/api/v1/credit/list' \
--header 'UUID: d6bb68f0-ba8c-4985-afb0-6750b0990905' \
--header 'Content-Type: application/json' \
--data '{
"userId": "b9e661d3-4ca3-4be1-9887-0bfb128a7b15",
"status": "OPEN",
"dateFrom": "2024-09-08T20:00:00.000",
"dateTo": "2024-09-12T01:07:00.000",
"page": 1,
"size": 4
}'

