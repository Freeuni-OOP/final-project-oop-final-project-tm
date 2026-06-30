import { useState } from 'react';
import { useOutletContext, useNavigate } from 'react-router-dom';
import VerifyForm from './VerifyForm';
import './Login.css';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [verificationCode, setVerificationCode] = useState('');

    const [isVerifying, setIsVerifying] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const { setCurrentUser } = useOutletContext();
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');

        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ email, password }),
            });

            const errorText = await response.text();

            if (response.status === 403 && errorText.startsWith("NOT_VERIFIED:")) {
                setSuccessMessage("A new Email has been sent to you, please verify it");
                setIsVerifying(true);
                return;
            }

            if (!response.ok) {
                throw new Error(errorText || 'Invalid Email or Password');
            }

            const userData = JSON.parse(errorText);
            setCurrentUser(userData);
            navigate('/');
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const handleVerify = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');

        try {
            const response = await fetch('http://localhost:8080/api/auth/verify', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ email, code: verificationCode }),
            });

            if (!response.ok) {
                throw new Error(await response.text() || 'Verification failed');
            }

            const userData = await response.json();
            setSuccessMessage("Verification completed!");

            setIsVerifying(false);
            setVerificationCode('');
            setPassword('');

            setCurrentUser(userData);
            navigate('/');

        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                {errorMessage && <div className="alert error">{errorMessage}</div>}
                {successMessage && <div className="alert success">{successMessage}</div>}

                {!isVerifying ? (
                    <form onSubmit={handleLogin}>
                        <h2>Sign In</h2>

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

                        <button type="submit" className="login-btn">Log In</button>
                    </form>
                ):(
                    <>
                        <VerifyForm
                            email={email}
                            verificationCode={verificationCode}
                            setVerificationCode={setVerificationCode}
                            onSubmit={handleVerify}
                        />
                    </>
                )}
            </div>
        </div>
    );
};

export default Login;