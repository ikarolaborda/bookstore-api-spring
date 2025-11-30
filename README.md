# Book Search Application

A Spring Boot application for searching books, built with PostgreSQL database and managed with Lando.

## Tech Stack

- **Backend**: Spring Boot 3.2.0 (Java 17)
- **Database**: PostgreSQL 16
- **Build Tool**: Maven 3.9
- **Development Environment**: Lando
- **Database Management**: pgAdmin 4

## Quick Start

### Prerequisites

- [Lando](https://lando.dev/) installed
- Docker Desktop running

### Setup

1. **Start the environment**:
```bash
lando start
```

2. **Build the application**:
```bash
lando mvn clean install
```

3. **Run the Spring Boot application**:
```bash
lando spring-boot
```

4. **Access pgAdmin**: 
   - Open http://localhost:5050 in your browser
   - Login: `admin@admin.com` / `admin`

5. **Test the health endpoint**:
```bash
curl http://localhost:8080/api/health
```

Expected response:
```json
{"status":"UP","uptimeSeconds":123}
```

## Database Access

### From within Lando containers
- Host: `database`
- Port: `5432`
- Database: `book_search`
- Username: `postgres`
- Password: (empty)

### From your host machine
- Host: `localhost`
- Port: `54320`
- Database: `book_search`
- Username: `postgres`
- Password: (empty)

### Using psql via Lando
```bash
lando psql
```

## Available Commands

- `lando mvn [maven-command]` - Run any Maven command
- `lando java [args]` - Run Java
- `lando spring-boot` - Start the Spring Boot application
- `lando psql` - Connect to PostgreSQL via psql
- `lando start` - Start all services
- `lando stop` - Stop all services
- `lando restart` - Restart all services
- `lando rebuild` - Rebuild all services
- `lando destroy` - Destroy all services and volumes
- `lando info` - Show service information

## Project Structure

```
book-search/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── tech/aerolambda/
│   │   │       ├── App.java
│   │   │       └── controllers/
│   │   │           └── HealthController.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── config/
│   └── postgres.conf
├── .lando.yml
├── pom.xml
└── README.md
```

## Services

The Lando environment includes:

1. **PostgreSQL 16** - Primary database
2. **pgAdmin 4** - Database administration interface
3. **Maven/Java Container** - For building and running the Spring Boot application

## Development Workflow

1. Make changes to your code
2. Build with `lando mvn clean install`
3. Run with `lando spring-boot`
4. Access the application at `http://localhost:8080`
5. Manage the database via pgAdmin at `http://localhost:5050`

## Troubleshooting

### Services not starting
```bash
lando rebuild -y
```

### View logs
```bash
lando logs -s [service-name]
# Example: lando logs -s database
```

### Check service status
```bash
lando info
```


