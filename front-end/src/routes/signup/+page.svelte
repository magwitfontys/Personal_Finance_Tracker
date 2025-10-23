<script>
	import { REGISTER_URL, postJson } from '$lib/api';

	let username = '';
	let password = '';
	let confirm = '';
	let loading = false;
	let result = null;
	let error = null;

	async function submit() {
		if (password !== confirm) {
			error = { data: { error: "Passwords don't match" } };
			return;
		}
		loading = true;
		result = null;
		error = null;
		try {
			result = await postJson(REGISTER_URL, { username, password });
		} catch (e) {
			error = e;
		} finally {
			loading = false;
		}
	}
</script>

<h2>Sign up (Smoke Test)</h2>
<form on:submit|preventDefault={submit}>
	<div><label>Username</label><br /><input bind:value={username} autocomplete="username" /></div>
	<div>
		<label>Password</label><br /><input
			type="password"
			bind:value={password}
			autocomplete="new-password"
		/>
	</div>
	<div>
		<label>Confirm</label><br /><input
			type="password"
			bind:value={confirm}
			autocomplete="new-password"
		/>
	</div>
	<button disabled={loading} type="submit">{loading ? '...' : 'Create account'}</button>
</form>

<h3>Response</h3>
{#if result}<pre>{JSON.stringify(result, null, 2)}</pre>{/if}
{#if error}<pre>{JSON.stringify(error, null, 2)}</pre>{/if}
