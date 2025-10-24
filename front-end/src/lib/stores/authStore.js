import { writable } from 'svelte/store';

const initialUsername =
  typeof localStorage !== 'undefined' ? localStorage.getItem('username') : null;

export const auth = writable({ username: initialUsername });

auth.subscribe((v) => {
  if (typeof localStorage === 'undefined') return;
  if (v.username) localStorage.setItem('username', v.username);
  else localStorage.removeItem('username');
});
