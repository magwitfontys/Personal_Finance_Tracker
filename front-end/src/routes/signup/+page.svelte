<script>
	import { post } from '$lib/api';
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
		const res = await post('/api/auth/register', { username, password });
		if (res.ok) {
			msg = 'Registered! You can now log in.';
			setTimeout(() => goto('/login'), 400);
		} else if (res.status === 409) {
			err = 'Username already exists.';
		} else {
			err = res.error ?? `HTTP ${res.status}`;
		}
		loading = false;
	}
</script>

<svelte:head><title>Sign up</title></svelte:head>

<h1>Sign up</h1>
<form on:submit|preventDefault={submit}>
	<label>Username <input bind:value={username} required /></label>
	<label>Password <input type="password" bind:value={password} required /></label>
	<button disabled={loading}>Create account</button>
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
