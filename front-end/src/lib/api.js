import { PUBLIC_API_BASE } from '$env/static/public';

export const API_BASE = (PUBLIC_API_BASE || 'http://localhost:8081').replace(/\/+$/, '');
export const LOGIN_URL = `${API_BASE}/api/auth/login`;
export const REGISTER_URL = `${API_BASE}/api/auth/register`;

export async function postJson(url, body) {
  const res = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body)
  });

  const text = await res.text();
  let data = null;
  try { data = text ? JSON.parse(text) : null; } catch { data = text; }

  if (!res.ok) throw { status: res.status, statusText: res.statusText, data };
  return data;
}
