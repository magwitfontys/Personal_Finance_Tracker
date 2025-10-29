<script>
	import './auth.css';
	import { goto } from '$app/navigation';
	import { onMount } from 'svelte';
	import eye from '$lib/pictures/eye.png';
	import crossedEye from '$lib/pictures/crossed-eye.png';
	import chart from '$lib/pictures/chart.png';

	let mode = 'login';

	let login = { username: '', password: '', show: false, busy: false, error: '' };
	let signup = {
		username: '',
		password: '',
		confirm: '',
		show: false,
		show2: false,
		busy: false,
		error: '',
		ok: ''
	};

	onMount(() => {
		if (location.hash === '#register') mode = 'register';
	});

	async function postJSON(path, body) {
		try {
			const res = await fetch(`/api${path}`, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(body)
			});
			const text = await res.text();
			let data = {};
			try {
				data = text ? JSON.parse(text) : {};
			} catch {
				data = { raw: text };
			}
			if (!res.ok) {
				const msg = data.message || data.error || `HTTP ${res.status}`;
				throw new Error(msg);
			}
			return data;
		} catch (e) {
			if (e instanceof TypeError && e.message === 'Failed to fetch') {
				throw new Error(
					'Failed to reach API via /api proxy. Is the backend on :8081 and Vite proxy set?'
				);
			}
			throw e;
		}
	}

	$: loginValid = login.username.trim().length >= 3 && login.password.length >= 3 && !login.busy;
	$: signupValid =
		signup.username.trim().length >= 3 &&
		signup.password.length >= 6 &&
		signup.password === signup.confirm &&
		!signup.busy;

	async function handleLogin(e) {
		e.preventDefault();
		if (!loginValid) return;
		login.error = '';
		login.busy = true;
		try {
			const data = await postJSON('/auth/login', {
				username: login.username,
				password: login.password
			});

			// Your controller returns { success: true } on OK. Use token if you later add it.
			if (data.token) localStorage.setItem('token', data.token);
			if (data.success === true || data.token) {
				localStorage.setItem('auth', '1');
				localStorage.setItem('username', login.username);
				goto('/dashboard');
			} else {
				login.error = 'Login failed.';
			}
		} catch (err) {
			login.error = err.message;
		} finally {
			login.busy = false;
		}
	}

	async function handleSignup(e) {
		e.preventDefault();
		if (!signupValid) {
			signup.error = 'Please fix the fields.';
			return;
		}
		signup.error = '';
		signup.ok = '';
		signup.busy = true;
		try {
			await postJSON('/auth/register', { username: signup.username, password: signup.password });
			signup.ok = 'Account created. Log in now.';
			mode = 'login';
			login.username = signup.username;
			login.password = '';
		} catch (err) {
			signup.error = err.message;
		} finally {
			signup.busy = false;
		}
	}
</script>

<div class="auth">
	<div class="wrap">
		<div class="card">
			<div class="logo">
				<img src={chart} alt="icon" width="25px" height="25px" />
			</div>
			<h1 class="title">Personal Finance Tracker</h1>
			<p class="tagline">Manage your finances with ease</p>

			<div class="tabs">
				<button
					class:active={mode === 'login'}
					on:click={() => (mode = 'login')}
					aria-pressed={mode === 'login'}
				>
					Login
				</button>
				<button
					class:active={mode === 'register'}
					on:click={() => (mode = 'register')}
					aria-pressed={mode === 'register'}
				>
					Register
				</button>
			</div>

			{#if mode === 'login'}
				<form on:submit|preventDefault={handleLogin} class="form">
					<label>
						<span>Username</span>
						<input
							name="username"
							autocomplete="username"
							placeholder="Enter your username"
							bind:value={login.username}
							required
						/>
					</label>

					<label class="password">
						<span>Password</span>
						<div class="pw-field">
							<input
								type={login.show ? 'text' : 'password'}
								name="password"
								autocomplete="current-password"
								placeholder="Enter your password"
								bind:value={login.password}
								required
							/>
							<button
								type="button"
								class="eye"
								aria-label={login.show ? 'Hide password' : 'Show password'}
								aria-pressed={login.show}
								on:click={() => (login.show = !login.show)}
							>
								<img
									src={login.show ? crossedEye : eye}
									alt={login.show ? 'Hide password' : 'Show password'}
									width="25"
									height="25"
								/>
							</button>
						</div>
					</label>

					{#if login.error}<p class="msg error">{login.error}</p>{/if}
					<button class="primary" disabled={login.busy}
						>{login.busy ? 'Logging in…' : 'Login'}</button
					>
				</form>
			{:else}
				<form on:submit|preventDefault={handleSignup} class="form">
					<label>
						<span>Username</span>
						<input
							name="username"
							autocomplete="username"
							placeholder="Choose a username"
							bind:value={signup.username}
							required
						/>
					</label>

					<label class="password">
						<span>Password</span>
						<div class="pw-field">
							<input
								type={signup.show ? 'text' : 'password'}
								name="password"
								autocomplete="new-password"
								placeholder="Create a password"
								bind:value={signup.password}
								minlength="6"
								required
							/>
							<button
								type="button"
								class="eye"
								aria-label={signup.show ? 'Hide password' : 'Show password'}
								aria-pressed={signup.show}
								on:click={() => (signup.show = !signup.show)}
							>
								<img
									src={signup.show ? crossedEye : eye}
									alt={signup.show ? 'Hide password' : 'Show password'}
									width="25"
									height="25"
								/>
							</button>
						</div>
					</label>

					<label class="password">
						<span>Confirm Password</span>
						<div class="pw-field">
							<input
								type={signup.show2 ? 'text' : 'password'}
								name="confirm"
								autocomplete="new-password"
								placeholder="Confirm your password"
								bind:value={signup.confirm}
								minlength="6"
								required
							/>
							<button
								type="button"
								class="eye"
								aria-label={signup.show2 ? 'Hide password' : 'Show password'}
								aria-pressed={signup.show2}
								on:click={() => (signup.show2 = !signup.show2)}
							>
								<img
									src={signup.show2 ? crossedEye : eye}
									alt={signup.show2 ? 'Hide password' : 'Show password'}
									width="25"
									height="25"
								/>
							</button>
						</div>
					</label>

					{#if signup.error}<p class="msg error">{signup.error}</p>{/if}
					{#if signup.ok}<p class="msg ok">{signup.ok}</p>{/if}

					<button class="primary" disabled={signup.busy}
						>{signup.busy ? 'Creating…' : 'Create Account'}</button
					>
				</form>
			{/if}
		</div>
	</div>
</div>
