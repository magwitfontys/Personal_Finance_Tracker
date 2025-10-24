<script>
	import { auth } from '$lib/auth/authStore'; // <-- updated path
	import { goto } from '$app/navigation';

	let me = { username: null };
	const unsub = auth.subscribe((v) => (me = v));
	function logout() {
		auth.set({ username: null });
		goto('/login');
	}

	import { onDestroy } from 'svelte';
	onDestroy(unsub);
</script>
