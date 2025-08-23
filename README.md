# Database Setup with Docker Compose

## Overview
This project runs PostgreSQL, MongoDB, and Adminer using Docker Compose.

## Database Configurations

### PostgreSQL
- **Host:** localhost:5432
- **Username:** postgres
- **Password:** x3akqrtww
- **Database:** auth_db
- **Container Name:** postgres-db

### MongoDB
- **Host:** localhost:27017
- **Username:** admin
- **Password:** pass
- **Container Name:** mongodb

### Adminer (Database Management)
- **URL:** http://localhost:8888
- **Container Name:** adminer

## Quick Start

### 1. Start All Services
```bash
docker-compose up -d
```

### 2. Stop All Services
```bash
docker-compose down
```

### 3. View Logs
```bash
# All services logs
docker-compose logs

# Specific service logs
docker-compose logs postgres-db
docker-compose logs mongodb
```

## docker-compose.yml

```yaml
services:
  db:
    image: postgres:latest
    container_name: postgres-db
    ports:
      - "5432:5432"
    restart: always
    environment:
      POSTGRES_PASSWORD: x3akqrtww
      POSTGRES_USER: postgres
      POSTGRES_DB: auth_db
    volumes:
      - postgresdata:/var/lib/postgresql/data
    networks:
      - backend

  adminer:
    image: adminer:latest
    container_name: adminer
    restart: always
    ports:
      - "8888:8080"
    networks:
      - backend

  mongo:
    image: mongo:latest
    container_name: mongodb
    ports:
      - "27017:27017"
    restart: always
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: pass
    volumes:
      - mongodata:/data/db
    networks:
      - backend

networks:
  backend:
    driver: bridge

volumes:
  mongodata:
  postgresdata:
```

## Adminer Connection Settings

### PostgreSQL Connection
- **System:** PostgreSQL
- **Server:** postgres-db
- **Username:** postgres
- **Password:** x3akqrtww
- **Database:** auth_db

### MongoDB Connection
- **System:** MongoDB
- **Server:** mongodb
- **Username:** admin
- **Password:** pass

## Useful Commands

```bash
# Connect to containers
docker exec -it postgres-db psql -U postgres -d auth_db
docker exec -it mongodb mongosh

# Check container status
docker-compose ps

# Remove volumes (careful, this deletes data!)
docker-compose down -v
```

# Additional Resources and Information

# 🚀 Kafka Scaling Guide: Single to Multi-Broker

## Current Setup (Single Broker)

Your current configuration uses:
- ✅ 1 Kafka Broker (port 9092)
- ✅ Replication Factor: 1
- ✅ Min In-Sync Replicas: 1
- ✅ Good for: Development, testing, low-traffic applications

## 📈 When to Scale to Multiple Brokers

Consider scaling when you experience:
- **High Traffic**: >10K messages/second
- **Data Loss Concerns**: Need fault tolerance
- **Downtime Issues**: Single broker failure affects entire system
- **Performance Bottlenecks**: CPU/disk/network saturation
- **Production Requirements**: High availability needed

## 🔄 Scaling Steps

### Step 1: Planning Your Scale
```bash
# Determine your needs:
# - Message throughput requirements
# - Fault tolerance level (how many broker failures to survive)
# - Consumer group parallelism needs

# Recommended configurations:
# 3 brokers = survive 1 broker failure
# 5 brokers = survive 2 broker failures
# 7 brokers = survive 3 broker failures (enterprise level)
```

### Step 2: Topic and Partition Strategy

#### Single Broker Topics (Current)
```bash
# Example topics for single broker
docker exec kafka kafka-topics \
  --create \
  --bootstrap-server kafka:9092 \
  --topic user-events \
  --partitions 3 \
  --replication-factor 1
```

#### Multi-Broker Topics (After Scaling)
```bash
# Same topics with higher replication
docker exec kafka-broker-1 kafka-topics \
  --create \
  --bootstrap-server kafka-broker-1:9092,kafka-broker-2:9093,kafka-broker-3:9094 \
  --topic user-events \
  --partitions 6 \
  --replication-factor 3
```

### Step 3: Consumer Group Scaling

#### Current Consumer Setup (Single Broker)
```javascript
const kafka = Kafka({
  clientId: 'my-app',
  brokers: ['localhost:9092'],  // Single broker
});

// Consumer group for single broker
const consumer = kafka.consumer({ 
  groupId: 'my-consumer-group' 
});
await consumer.subscribe({ topic: 'user-events' });
```

#### Scaled Consumer Setup (Multi-Broker)
```javascript
const kafka = Kafka({
  clientId: 'my-app',
  brokers: [
    'localhost:9092', 
    'localhost:9093', 
    'localhost:9094'
  ],  // Multiple brokers
  retry: {
    initialRetryTime: 100,
    retries: 8
  }
});

// Multiple consumer instances for better parallelism
const createConsumerGroup = async (groupId, consumerCount) => {
  const consumers = [];
  
  for (let i = 0; i < consumerCount; i++) {
    const consumer = kafka.consumer({
      groupId,
      sessionTimeout: 30000,
      heartbeatInterval: 3000,
    });
    
    consumers.push(consumer);
  }
  
  return consumers;
};

// Example: 6 partitions = 6 consumers for maximum parallelism
const consumers = await createConsumerGroup('user-events-processors', 6);
```

## 🛠️ Migration Scripts

### Script 1: Current Single Broker Setup
```bash
#!/bin/bash
# single-broker-setup.sh

echo "🔧 Setting up single broker topics..."

KAFKA_CONTAINER="kafka"
BOOTSTRAP_SERVER="kafka:9092"

# Create topics for single broker
docker exec $KAFKA_CONTAINER kafka-topics \
  --create --bootstrap-server $BOOTSTRAP_SERVER \
  --topic user-events --partitions 3 --replication-factor 1

docker exec $KAFKA_CONTAINER kafka-topics \
  --create --bootstrap-server $BOOTSTRAP_SERVER \
  --topic order-processing --partitions 4 --replication-factor 1

docker exec $KAFKA_CONTAINER kafka-topics \
  --create --bootstrap-server $BOOTSTRAP_SERVER \
  --topic notifications --partitions 2 --replication-factor 1

echo "✅ Single broker setup complete!"
```

### Script 2: Multi-Broker Migration
```bash
#!/bin/bash
# multi-broker-migration.sh

echo "🚀 Migrating to multi-broker setup..."

# Step 1: Stop current services
echo "⏸️  Stopping current services..."
docker-compose down

# Step 2: Update docker-compose.yml (manually uncomment multi-broker sections)
echo "📝 Please uncomment multi-broker sections in docker-compose.yml"
echo "   - Replace single 'kafka' service with 'kafka-broker-1/2/3'"
echo "   - Update kafka-ui environment variables"
echo "   - Add kafka1data, kafka2data, kafka3data volumes"
read -p "Press Enter after updating docker-compose.yml..."

# Step 3: Start multi-broker setup
echo "🔄 Starting multi-broker setup..."
docker-compose up -d

# Step 4: Wait for brokers to start
echo "⏳ Waiting for brokers to start..."
sleep 60

# Step 5: Create new topics with replication
BOOTSTRAP_SERVERS="kafka-broker-1:9092,kafka-broker-2:9093,kafka-broker-3:9094"

echo "📊 Creating replicated topics..."
docker exec kafka-broker-1 kafka-topics \
  --create --bootstrap-server $BOOTSTRAP_SERVERS \
  --topic user-events-v2 --partitions 6 --replication-factor 3

docker exec kafka-broker-1 kafka-topics \
  --create --bootstrap-server $BOOTSTRAP_SERVERS \
  --topic order-processing-v2 --partitions 9 --replication-factor 3

docker exec kafka-broker-1 kafka-topics \
  --create --bootstrap-server $BOOTSTRAP_SERVERS \
  --topic notifications-v2 --partitions 3 --replication-factor 3

# Step 6: Verify setup
echo "🔍 Verifying multi-broker setup..."
docker exec kafka-broker-1 kafka-topics --list --bootstrap-server $BOOTSTRAP_SERVERS
docker exec kafka-broker-1 kafka-broker-api-versions --bootstrap-server $BOOTSTRAP_SERVERS

echo "✅ Multi-broker migration complete!"
echo "🌐 Access Kafka UI at: http://localhost:8080"
```

## 📊 Monitoring and Validation

### Single Broker Monitoring
```bash
# Check single broker health
docker exec kafka kafka-broker-api-versions --bootstrap-server kafka:9092

# Monitor single consumer group
docker exec kafka kafka-consumer-groups \
  --describe --group my-group \
  --bootstrap-server kafka:9092
```

### Multi-Broker Monitoring
```bash
# Check all brokers health
for port in 9092 9093 9094; do
  echo "Checking broker on port $port..."
  docker exec kafka-broker-1 kafka-broker-api-versions \
    --bootstrap-server localhost:$port
done

# Monitor partition distribution
docker exec kafka-broker-1 kafka-topics \
  --describe --bootstrap-server kafka-broker-1:9092,kafka-broker-2:9093,kafka-broker-3:9094
```

## 🎯 Performance Optimization

### Consumer Group Optimization
| Partitions | Recommended Consumers | Reason |
|------------|----------------------|---------|
| 3 | 3 | 1:1 ratio for max throughput |
| 6 | 6 | No idle consumers |
| 9 | 9 | Perfect partition alignment |
| 12 | 6-12 | Flexible based on processing speed |

### Scaling Best Practices
1. **Always use odd number of brokers** (3, 5, 7) for better leader election
2. **Match consumer count to partition count** for optimal resource usage
3. **Use replication factor = (broker_count + 1) / 2** for fault tolerance
4. **Set min.insync.replicas = replication_factor - 1** for durability
5. **Monitor consumer lag** to determine when to add more consumers

## 🚨 Common Pitfalls to Avoid

❌ **Don't:**
- Scale brokers without planning partition strategy
- Create too many small partitions (overhead)
- Use even number of brokers (split-brain risk)
- Forget to update client configurations
- Mix single and multi-broker topics

✅ **Do:**
- Plan partition count based on expected load
- Test scaling in development first
- Monitor consumer lag and broker metrics
- Update client connection strings
- Use Kafka UI for visual monitoring

## 🔧 Emergency Rollback Plan

If scaling causes issues:
```bash
# 1. Stop multi-broker setup
docker-compose down

# 2. Restore single broker docker-compose.yml
# 3. Start single broker
docker-compose up -d

# 4. Your data in volumes will be preserved
```

Your current single-broker setup is perfect for getting started. Scale when you need the additional performance and fault tolerance! 🚀