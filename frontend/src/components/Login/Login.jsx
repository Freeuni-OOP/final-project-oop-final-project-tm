import { useState } from 'react';
import './Login.css';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');

        const loginPayload = { email, password };

        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginPayload),
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Invalid credentials');
            }

            const userData = await response.json();
            setSuccessMessage(`Welcome back, ${userData.name || 'User'}!`);

            // TODO: Save user data to context or localStorage here
            console.log('Login successful:', userData);

        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    return (
        <div className="login-container">
            <form className="login-card" onSubmit={handleLogin}>
                <h2>Sign In</h2>

                {errorMessage && <div className="alert error">{errorMessage}</div>}
                {successMessage && <div className="alert success">{successMessage}</div>}

                <div className="input-group">
                    <label htmlFor="email">Email Address</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        placeholder="Enter your email"
                    />
                </div>

                <div className="input-group">
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        placeholder="Enter your password"
                    />
                </div>

                <button type="submit" className="login-btn">Login</button>
            </form>
        </div>
    );
};

export default Login;