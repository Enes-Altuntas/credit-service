db = db.getSiblingDB("creditdb");

db.createCollection("credits");
db.createCollection("installments");
db.createCollection("payments");