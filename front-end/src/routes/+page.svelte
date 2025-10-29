<script>
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';

	onMount(() => {
		const token = localStorage.getItem('token'); // set this on successful login
		goto(token ? '/dashboard' : '/auth');
	});
</script>

<p>Redirecting…</p>
