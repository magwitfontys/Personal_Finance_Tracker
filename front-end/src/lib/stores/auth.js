import { writable } from 'svelte/store';

const KEY = 'auth_token';

function createAuthStore() {
  const initial = typeof localStorage !== 'undefined' ? localStorage.getItem(KEY) : null;
  const { subscribe, set } = writable(initial);

  function save(token) {
    set(token);
    if (typeof localStorage !== 'undefined') {
      if (token) { localStorage.setItem(KEY, token); }
      else { localStorage.removeItem(KEY); }
    }
  }

  return { subscribe, set: save, clear: () => save(null) };
}

export const authToken = createAuthStore();
