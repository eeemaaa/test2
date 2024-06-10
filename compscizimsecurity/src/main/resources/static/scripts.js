// Регистрация
document.getElementById('register-form')?.addEventListener('submit', async function(event) {
    event.preventDefault();
    const email = document.getElementById('register-email').value;
    const firstname = document.getElementById('register-firstname').value;
    const lastname = document.getElementById('register-lastname').value;
    const password = document.getElementById('register-password').value;

    const response = await fetch('http://localhost:9002/api/v1/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, firstname, lastname, password })
    });

    const result = await response.json();
    alert(result.response);

    if (response.ok) {
        window.location.href = 'login.html'; // Перенаправление на страницу авторизации
    }
});

// Авторизация
document.getElementById('login-form')?.addEventListener('submit', async function(event) {
    event.preventDefault();
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    const response = await fetch('http://localhost:9002/api/v1/authenticate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
    });

    const result = await response.json();
    if (response.ok) {
        alert(result.response);
        localStorage.setItem('token', result.token);
    } else {
        alert('Authentication failed: ' + result.response);
    }
});
