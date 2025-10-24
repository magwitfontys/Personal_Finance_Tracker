<script>
	import { auth } from '$lib/stores/authStore';
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

<nav style="display:flex;gap:1rem;margin:1rem 0">
	<a href="/">Home</a>
	<a href="/signup">Sign up</a>
	<a href="/login">Log in</a>
	<span style="margin-left:auto">
		{#if me.username}
			Signed in as <strong>{me.username}</strong> • <button on:click={logout}>Logout</button>
		{/if}
	</span>
</nav>

<slot />
