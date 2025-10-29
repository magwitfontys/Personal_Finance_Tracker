<script>
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';

	let username = '';

	onMount(() => {
		// treat either a token or an 'auth' flag as logged-in
		const isAuthed = localStorage.getItem('token') || localStorage.getItem('auth') === '1';
		if (!isAuthed) {
			goto('/auth');
			return;
		}
		username = localStorage.getItem('username') || 'user';
	});

	function logout() {
		localStorage.removeItem('token');
		localStorage.removeItem('auth');
		localStorage.removeItem('username');
		goto('/auth');
	}
</script>

<h1>Dashboard</h1>
<p>Welcome, {username}.</p>
<button on:click={logout}>Logout</button>
