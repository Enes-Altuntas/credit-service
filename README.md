# credit-system

Tasarım Kabulleri:

- Kredi görüntüleme işlemlerinin, kredi yaratma işlemlerinden fazla kez yapılacağı kabul edilerek CQRS mimari seçildi.
- Writing işlemleri için postgresql, reading için mongo db kullanıldı
- Application mimari olarak DD tabanlı Clean Architecture kullanıldı
- Data Consistency için CDC tool Debezzium ve Kafka Kullanıldı
- Idempotency checking için cache olarak redis kullanıldı.
- Distributed lock mechanism ile ileride multiple instance olarak çalışma durumu göz önüne alındı ve concurrent read write engellendi.

