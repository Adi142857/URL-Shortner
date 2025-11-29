## URL Shortener UI

This Next.js 15 (App Router + Tailwind) frontend consumes the Spring Boot API in `../` to provide:

- URL shortening with optional custom aliases.
- Instant view of the generated short link with copy-to-clipboard.
- Recent activity feed with click counts.

### Requirements

- Node.js 20+
- npm 10+

### Environment variables

Copy `env.example` to `.env.local` (not committed) and point it at the backend API:

```
cp env.example .env.local
```

| Variable | Description | Default |
| --- | --- | --- |
| `NEXT_PUBLIC_API_BASE_URL` | Base URL of the Spring Boot API | `http://localhost:8082` |

### Local development

```
npm install
npm run dev
# open http://localhost:3000
```

### Production build

```
npm run build
npm start
```

Deploy anywhere that supports Next.js (Vercel, Netlify, Render, etc.). Remember to set `NEXT_PUBLIC_API_BASE_URL` to the publicly accessible backend domain.
