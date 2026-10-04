document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    // UI Elements
    const btn = document.getElementById('submitBtn');
    const btnText = btn.querySelector('.btn-text');
    const loader = btn.querySelector('.loader');
    const msgBox = document.getElementById('message');
    
    // Collect data
    const payload = {
        name: document.getElementById('name').value,
        phone: document.getElementById('phone').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    };

    // Show loading state
    btn.disabled = true;
    btnText.classList.add('hidden');
    loader.classList.remove('hidden');
    msgBox.className = 'message hidden';

    try {
        const response = await fetch('http://localhost:8080/api/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.status === 201) {
            msgBox.textContent = 'Account created successfully!';
            msgBox.className = 'message success';
            document.getElementById('registerForm').reset();
        } else {
            msgBox.textContent = 'Registration failed. Email might already exist.';
            msgBox.className = 'message error';
        }
    } catch (error) {
        msgBox.textContent = 'Unable to connect to the server.';
        msgBox.className = 'message error';
    } finally {
        // Reset loading state
        btn.disabled = false;
        btnText.classList.remove('hidden');
        loader.classList.add('hidden');
    }
});
