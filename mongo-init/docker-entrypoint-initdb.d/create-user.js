db = db.getSiblingDB("admin");

db.createUser({
  user: "yana",
  pwd: "1234",
  roles: [
    { role: "readWrite", db: "payments" },
    { role: "dbAdmin", db: "payments" }
  ]
});

db.createUser({
  user: "mongo_exporter",
  pwd: "exporter_password",
  roles: [
    { role: "clusterMonitor", db: "admin" },
    { role: "read", db: "local" }
  ]
});