# URL Shortener Platform

A full-stack URL shortener composed of:

- **Backend:** Spring Boot 3 REST API with MySQL/PostgreSQL (local or managed cloud DB).
- **Frontend:** Next.js 15 (App Router + Tailwind) for link creation, management, and quick stats.

## Project structure

```
URL-Shortner/
├── backend (this repository root - Spring Boot app)
└── frontend (Next.js app generated with create-next-app)
```

## Backend (Spring Boot)

### Prerequisites

- Java 17+
- Maven 3.9+
- MySQL 8/PostgreSQL (local or hosted). You can point `spring.datasource.url` to any managed DB (AWS RDS, Azure DB, etc.).

### Configuration

All sensitive values are externalized via environment variables. The defaults (for local dev) live in `src/main/resources/application.properties`.

Key variables:

| Variable | Description | Default |
| --- | --- | --- |
| `DB_URL` | JDBC URL to your database | `jdbc:mysql://localhost:3306/mysqldev` |
| `DB_USERNAME` / `DB_PASSWORD` | Database credentials | `root` / `mysqladmin` |
| `SERVER_PORT` | Backend port | `8082` |
| `APP_SHORT_BASE_URL` | Base domain used to build short links | `http://localhost:8082/r` |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed origins for the API | `http://localhost:3000` |

### Run locally

```bash
./mvnw spring-boot:run
```

API highlights:

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST /api/v1/urls` | Create/lookup short URL (optional custom alias) |
| `GET /api/v1/urls?limit=10` | List recent short URLs |
| `GET /api/v1/urls/{code}` | Fetch metadata for a short code |
| `PUT /api/v1/urls/{code}` | Update the destination URL |
| `DELETE /api/v1/urls/{code}` | Delete a short code |
| `GET /r/{code}` | Redirect to the original URL |

## Frontend (Next.js)

### Prerequisites

- Node.js 20+
- npm 10+

### Setup

```bash
cd frontend
npm install
cp env.example .env.local   # create your env file if it does not exist
# edit NEXT_PUBLIC_API_BASE_URL to match the backend URL
```

> If `env.example` is missing, create `.env.local` manually with `NEXT_PUBLIC_API_BASE_URL=http://localhost:8082`.

### Run locally

```bash
cd frontend
npm run dev
# visit http://localhost:3000
```

The UI provides:

- URL form with optional custom alias and validation.
- Summary card for the latest short link (with copy button).
- Recent activity feed with click counts and quick copy actions.

## Deployment notes

- Deploy the backend to any Spring-friendly host (Heroku, Render, AWS Elastic Beanstalk, Azure App Service, etc.).
- Use a managed MySQL/PostgreSQL instance. Only environment variables need to change between environments.
- Deploy the Next.js app on Vercel, Netlify, or any Node-compatible host. Set `NEXT_PUBLIC_API_BASE_URL` to the public API base (e.g., `https://api.myshort.ly`).
- Update `APP_SHORT_BASE_URL` to match the public redirect domain (e.g., `https://myshort.ly/r`).

## Testing

- Backend: `./mvnw test`
- Frontend: `npm run lint` (add component/page tests with your preferred framework such as Playwright or Vitest).

---

Feel free to extend the platform with authentication, rate limiting, analytics dashboards, or link expiration rules.
