<script>
	import { LOGIN_URL, postJson } from '$lib/api';
	import { authToken } from '$lib/stores/auth';

	let username = '';
	let password = '';

	let loading = false;
	let result = null;
	let error = null;

	async function submit() {
		loading = true;
		result = null;
		error = null;
		try {
			const data = await postJson(LOGIN_URL, { username, password });
			result = data;
			if (data && typeof data === 'object' && 'token' in data) {
				authToken.set(data.token);
			}
		} catch (e) {
			error = e;
		} finally {
			loading = false;
		}
	}
</script>

<h1>Login</h1>

<form on:submit|preventDefault={submit}>
	<div>
		<label>Username</label><br />
		<input bind:value={username} autocomplete="username" />
	</div>

	<div>
		<label>Password</label><br />
		<input type="password" bind:value={password} autocomplete="current-password" />
	</div>

	<button disabled={loading} type="submit">{loading ? '...' : 'Login'}</button>
</form>

<h3>Request</h3>
<pre>{JSON.stringify(
		{ url: LOGIN_URL, body: { username, password: password ? '***' : '' } },
		null,
		2
	)}</pre>

<h3>Response</h3>
{#if result}<pre>{JSON.stringify(result, null, 2)}</pre>{/if}
{#if error}<pre>{JSON.stringify(error, null, 2)}</pre>{/if}
