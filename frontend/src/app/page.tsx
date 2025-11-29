"use client";

import { FormEvent, useEffect, useState } from "react";

type ShortUrl = {
  shortCode: string;
  originalUrl: string;
  shortUrl: string;
  accessCount: number;
  createdAt: string;
  updatedAt: string;
};

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8082";

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers ?? {}),
    },
  });

  if (!response.ok) {
    const body = await response.json().catch(() => ({}));
    const message = body?.message ?? "Something went wrong. Please try again.";
    throw new Error(message);
  }

  return response.json();
}

export default function Home() {
  const [originalUrl, setOriginalUrl] = useState("");
  const [customAlias, setCustomAlias] = useState("");
  const [recentUrls, setRecentUrls] = useState<ShortUrl[]>([]);
  const [latestUrl, setLatestUrl] = useState<ShortUrl | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [copiedCode, setCopiedCode] = useState<string | null>(null);

  useEffect(() => {
    request<ShortUrl[]>(`/api/v1/urls?limit=10`)
      .then(setRecentUrls)
      .catch((err) => setError(err.message));
  }, []);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsSubmitting(true);
    setError(null);

    try {
      const payload = {
        originalUrl,
        customAlias: customAlias.trim() || undefined,
      };

      const data = await request<ShortUrl>(`/api/v1/urls`, {
        method: "POST",
        body: JSON.stringify(payload),
      });

      setLatestUrl(data);
      setRecentUrls((prev) => {
        const filtered = prev.filter((item) => item.shortCode !== data.shortCode);
        return [data, ...filtered].slice(0, 10);
      });
      setOriginalUrl("");
      setCustomAlias("");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to shorten URL.");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCopy = async (shortUrl: string, shortCode: string) => {
    await navigator.clipboard.writeText(shortUrl);
    setCopiedCode(shortCode);
    setTimeout(() => setCopiedCode(null), 2000);
  };

  return (
    <div className="min-h-screen bg-slate-950/95 py-12 text-slate-100">
      <main className="mx-auto max-w-5xl space-y-8 px-4">
        <section className="rounded-3xl border border-slate-800 bg-slate-900/70 p-8 shadow-2xl shadow-slate-900/50 backdrop-blur">
          <p className="text-sm uppercase tracking-[0.3em] text-sky-400">
            Link Platform
          </p>
          <h1 className="mt-2 text-4xl font-semibold leading-tight text-white">
            Create short, shareable links in seconds.
          </h1>
          <p className="mt-4 text-slate-300">
            Paste any URL, optionally customize the slug, and we will do the rest.
            Track recent links and click counts in real time.
          </p>

          <form className="mt-8 space-y-4" onSubmit={handleSubmit}>
            <label className="block text-sm font-semibold uppercase tracking-wide text-slate-400">
              Original URL
              <input
                type="url"
                className="mt-2 w-full rounded-2xl border border-slate-700 bg-slate-950/40 p-4 text-base text-white outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-500/30"
                placeholder="https://example.com/article"
                value={originalUrl}
                onChange={(event) => setOriginalUrl(event.target.value)}
                required
              />
            </label>
            <label className="block text-sm font-semibold uppercase tracking-wide text-slate-400">
              Custom Alias (optional)
              <input
                type="text"
                className="mt-2 w-full rounded-2xl border border-slate-700 bg-slate-950/40 p-4 text-base text-white outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-500/30"
                placeholder="4-20 characters, letters & numbers"
                value={customAlias}
                onChange={(event) => setCustomAlias(event.target.value)}
                minLength={4}
                maxLength={20}
              />
            </label>
            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full rounded-2xl bg-gradient-to-r from-sky-500 to-blue-500 py-4 text-lg font-semibold text-white transition hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-60"
            >
              {isSubmitting ? "Shortening..." : "Shorten URL"}
            </button>
          </form>

          {error && (
            <p className="mt-4 rounded-xl border border-red-500/40 bg-red-500/10 px-4 py-3 text-sm text-red-200">
              {error}
            </p>
          )}

          {latestUrl && (
            <div className="mt-6 rounded-2xl border border-slate-800 bg-slate-950/40 p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-slate-500">
                Latest Link
              </p>
              <div className="mt-2 flex flex-col gap-2 md:flex-row md:items-center md:justify-between">
                <div>
                  <p className="text-lg font-semibold text-white">
                    {latestUrl.shortUrl}
                  </p>
                  <p className="text-sm text-slate-400">{latestUrl.originalUrl}</p>
                </div>
                <button
                  type="button"
                  className="rounded-full border border-slate-700 px-4 py-2 text-sm font-semibold text-slate-200 transition hover:border-sky-500 hover:text-white"
                  onClick={() => handleCopy(latestUrl.shortUrl, latestUrl.shortCode)}
                >
                  {copiedCode === latestUrl.shortCode ? "Copied!" : "Copy Link"}
                </button>
              </div>
            </div>
          )}
        </section>

        <section className="rounded-3xl border border-slate-800 bg-slate-900/40 p-6 backdrop-blur">
          <div className="flex flex-col gap-2 md:flex-row md:items-center md:justify-between">
            <div>
              <p className="text-sm uppercase tracking-[0.3em] text-sky-400">
                Recent Links
              </p>
              <h2 className="text-2xl font-semibold text-white">Activity Feed</h2>
            </div>
            <p className="text-sm text-slate-400">
              Showing the last {recentUrls.length} short URLs
            </p>
          </div>

          <div className="mt-6 space-y-4">
            {recentUrls.length === 0 && (
              <p className="rounded-2xl border border-dashed border-slate-700 p-6 text-center text-slate-400">
                No URLs yet. Shorten your first link above!
              </p>
            )}
            {recentUrls.map((item) => (
              <article
                key={item.shortCode}
                className="rounded-2xl border border-slate-800 bg-slate-950/40 p-4 text-sm text-slate-300"
              >
                <div className="flex flex-col gap-2 md:flex-row md:items-center md:justify-between">
                  <div>
                    <p className="text-base font-semibold text-white">
                      {item.shortUrl}
                    </p>
                    <p className="break-all text-slate-400">{item.originalUrl}</p>
                  </div>
                  <div className="flex flex-wrap items-center gap-3">
                    <span className="rounded-full bg-slate-800 px-3 py-1 text-xs font-semibold text-slate-200">
                      {item.accessCount} clicks
                    </span>
                    <button
                      onClick={() => handleCopy(item.shortUrl, item.shortCode)}
                      className="rounded-full border border-slate-700 px-4 py-1 text-xs font-semibold text-slate-200 transition hover:border-sky-500 hover:text-white"
                    >
                      {copiedCode === item.shortCode ? "Copied!" : "Copy"}
                    </button>
                  </div>
                </div>
              </article>
            ))}
          </div>
        </section>
      </main>
    </div>
  );
}
