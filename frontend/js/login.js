document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const btn = document.getElementById('submitBtn');
    const btnText = btn.querySelector('.btn-text');
    const loader = btn.querySelector('.loader');
    const msgBox = document.getElementById('message');
    
    const payload = {
        email: document.getElementById('email').value.trim(),
        password: document.getElementById('password').value
    };

    // UI Loading state
    btn.disabled = true;
    btnText.classList.add('hidden');
    loader.classList.remove('hidden');
    msgBox.className = 'message hidden';

    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const data = await response.json();

        if (response.status === 200 && data.success) {
            msgBox.textContent = data.message || 'Login successful!';
            msgBox.className = 'message success';
        } else {
            msgBox.textContent = data.message || 'Invalid email or password.';
            msgBox.className = 'message error';
        }
    } catch (error) {
        msgBox.textContent = 'Unable to connect to the server.';
        msgBox.className = 'message error';
    } finally {
        btn.disabled = false;
        btnText.classList.remove('hidden');
        loader.classList.add('hidden');
    }
});
