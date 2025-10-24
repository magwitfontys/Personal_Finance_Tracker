<script>
	import { post } from '$lib/api';
	import { auth } from '$lib/stores/authStore';
	import { goto } from '$app/navigation';

	let username = '';
	let password = '';
	let loading = false;
	let msg = '';
	let err = '';

	async function submit() {
		loading = true;
		msg = '';
		err = '';
		const res = await post('/api/auth/login', { username, password });
		if (res.ok && res.data?.success) {
			auth.set({ username });
			msg = 'Logged in!';
			setTimeout(() => goto('/'), 400);
		} else {
			err = res.error ?? 'Invalid credentials';
		}
		loading = false;
	}
</script>

<svelte:head><title>Log in</title></svelte:head>

<h1>Log in</h1>
<form on:submit|preventDefault={submit}>
	<label>Username <input bind:value={username} required /></label>
	<label>Password <input type="password" bind:value={password} required /></label>
	<button disabled={loading}>Log in</button>
</form>

{#if msg}<p style="color:#0a7">{msg}</p>{/if}
{#if err}<p style="color:#b00">{err}</p>{/if}

<style>
	form {
		display: grid;
		gap: 0.75rem;
		max-width: 360px;
	}
	input,
	button {
		padding: 0.6rem;
		font: inherit;
	}
</style>
