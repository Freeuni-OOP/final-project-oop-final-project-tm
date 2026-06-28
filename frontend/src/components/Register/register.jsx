import { useState } from 'react';
import './Register.css';

const Register = () => {
    const [first_name, setFirst_name] = useState('');
    const [last_name, setLast_name] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleRegister = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');

        const registerPayload = {first_name, last_name, email, password };

        try {
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(registerPayload),
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Registration failed');
            }

            const savedUser = await response.json();
            setSuccessMessage(`Account created successfully for ${savedUser.name || 'you'}! You can now log in.`);

            setFirst_name('');
            setLast_name('');
            setEmail('');
            setPassword('');

        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    return (
        <div className="register-container">
            <form className="register-card" onSubmit={handleRegister}>
                <h2>Create Account</h2>

                {errorMessage && <div className="alert error">{errorMessage}</div>}
                {successMessage && <div className="alert success">{successMessage}</div>}

                <div className="input-group">
                    <label htmlFor="first_name">First Name</label>
                    <input
                        type="text"
                        id="first_name"
                        value={first_name}
                        onChange={(e) => setFirst_name(e.target.value)}
                        required
                        placeholder="Enter your First name"
                    />
                </div>

                <div className="input-group">
                    <label htmlFor="last_name">Last Name</label>
                    <input
                        type="text"
                        id="last_name"
                        value={last_name}
                        onChange={(e) => setLast_name(e.target.value)}
                        required
                        placeholder="Enter your Last name"
                    />
                </div>

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
                        placeholder="Create a password"
                    />
                </div>

                <button type="submit" className="register-btn">Register</button>
            </form>
        </div>
    );
};

export default Register;