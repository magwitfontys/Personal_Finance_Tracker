import { PUBLIC_API_BASE } from '$env/static/public';

export async function post(path, body) {
  try {
    const res = await fetch(`${PUBLIC_API_BASE}${path}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });
    const text = await res.text();
    let data;
    try { data = text ? JSON.parse(text) : undefined; } catch { data = { raw: text }; }
    return res.ok
      ? { ok: true, status: res.status, data }
      : { ok: false, status: res.status, error: data?.message ?? text };
  } catch (e) {
    return { ok: false, status: 0, error: e?.message ?? 'Network error' };
  }
}
