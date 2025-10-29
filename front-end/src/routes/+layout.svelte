<script>
	import { auth } from '$lib/stores/authStore';
	import { goto } from '$app/navigation';

	// use Svelte's $store syntax instead of manual subscribe/unsubscribe
	$: me = $auth;

	function logout() {
		auth.set({ username: null, token: null });
		goto('/auth');
	}
</script>

<nav style="display:flex;gap:1rem;margin:1rem 0">
	<!-- Home goes to dashboard if logged in, otherwise to auth -->
	<a href={$auth?.token ? '/dashboard' : '/auth'}>Home</a>

	{#if !$auth?.token}
		<!-- Not signed in: show Login/Register (both handled in /auth; #register opens the tab) -->
		<a href="/auth">Log in</a>
		<a href="/auth#register">Register</a>
	{/if}

	<span style="margin-left:auto">
		{#if $auth?.username}
			Signed in as <strong>{$auth.username}</strong> •
			<button on:click={logout}>Logout</button>
		{/if}
	</span>
</nav>

<slot />
