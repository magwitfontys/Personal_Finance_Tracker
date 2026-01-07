# Security Configuration Guide

## Overview
This application implements OWASP A02: Security Misconfiguration best practices.

## Environment Variables

The application uses environment variables for sensitive configuration. 

### Required Environment Variables

- `DB_URL` - Database connection URL (default: H2 file database)
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `H2_CONSOLE_ENABLED` - Enable/disable H2 console (default: false)
- `CORS_ALLOWED_ORIGINS` - Comma-separated list of allowed CORS origins
- `SPRING_PROFILES_ACTIVE` - Active Spring profile (use 'dev' for development)

### Setup for Local Development

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Edit `.env` with your local values (the defaults work for local dev)

3. **Option A**: Use an IDE run configuration to load environment variables from `.env`

4. **Option B**: Set environment variables manually:
   - **IntelliJ IDEA**: Run → Edit Configurations → Environment Variables
   - **VS Code**: Add to launch.json
   - **Command Line**: Export variables before running

5. Run with dev profile for development features:
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=dev'
   ```

## Security Features

### ✅ Profile-Based H2 Console
- **Default**: H2 console is DISABLED for security
- **Development**: Enable via `application-dev.yml` or `H2_CONSOLE_ENABLED=true`
- Access at: `http://localhost:8081/h2-console` (only when enabled)

### ✅ Secure CORS Configuration
- Centralized in `CorsConfig.java` and `application.yml`
- No "null" origin allowed (prevents local file attacks)
- Explicit allowed origins, methods, and headers
- Different origins per environment via configuration

**Development origins** (in application-dev.yml):
- http://localhost:5173
- http://127.0.0.1:5173
- http://localhost:4173
- http://127.0.0.1:4173

**Production**: Override `CORS_ALLOWED_ORIGINS` with your deployed front-end URL

### ✅ Environment-Based Secrets
- Database credentials use environment variables
- Default values provided for local development
- Production requires explicit environment variables
- `.env` file ignored by git to prevent credential leaks

## Validation

### Test CORS Configuration
1. Start the application with dev profile
2. Front-end at `http://localhost:5173` should work normally
3. Requests from other origins should be blocked by the browser

### Test H2 Console Security
1. Start without dev profile:
   ```bash
   ./gradlew bootRun
   ```
   - Visit `http://localhost:8081/h2-console` → Should return 404 or be disabled

2. Start with dev profile:
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=dev'
   ```
   - Visit `http://localhost:8081/h2-console` → Should be accessible

### Test Environment Variables
1. Remove/rename `.env` file
2. Start application without environment variables
3. Application should use defaults or show clear error for missing required variables

## Production Deployment

When deploying to production:

1. **Never** commit `.env` file to version control
2. Set environment variables in your deployment platform:
   - Heroku: Settings → Config Vars
   - AWS: Elastic Beanstalk environment properties
   - Docker: docker-compose.yml environment section
   - Kubernetes: ConfigMaps and Secrets

3. Override these variables for production:
   ```
   DB_URL=<your-production-database-url>
   DB_USERNAME=<your-production-db-user>
   DB_PASSWORD=<secure-password>
   H2_CONSOLE_ENABLED=false
   CORS_ALLOWED_ORIGINS=https://your-production-domain.com
   SPRING_PROFILES_ACTIVE=prod
   ```

4. **Never** enable H2 console in production (`H2_CONSOLE_ENABLED` must be false)

