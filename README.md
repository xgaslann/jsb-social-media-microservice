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