import { useState } from 'react';
import { useOutletContext, useNavigate, Link } from 'react-router-dom';
import RegisterForm from './RegisterForm';
import VerifyForm from './VerifyForm';
import './Register.css';

const Register = () => {
    const { setCurrentUser } = useOutletContext();
    const navigate = useNavigate();

    const [first_name, setFirst_name] = useState('');
    const [last_name, setLast_name] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [verificationCode, setVerificationCode] = useState('');
    const [isRegistered, setIsRegistered] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleRegisterSubmit = async (e) => {
        e.preventDefault();
        if (isSubmitting) return;

        if (password !== confirmPassword) {
            setErrorMessage('Passwords do not match.');
            return;
        }
        setIsSubmitting(true);
        setErrorMessage('');
        setSuccessMessage('');

        try {
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ first_name, last_name, email, password }),
            });

            const responseText = await response.text();
            if (!response.ok) throw new Error(responseText || 'Registration failed!');

            setSuccessMessage(responseText);
            setIsRegistered(true);
        } catch (error) {
            setErrorMessage(error.message);
            setIsSubmitting(false);
        }
    };

    const handleVerifySubmit = async (e) => {
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

            if (!response.ok) throw new Error(await response.text() || 'Verification failed!');

            const userData = await response.json();

            setFirst_name('');
            setLast_name('');
            setEmail('');
            setPassword('');
            setConfirmPassword('');
            setVerificationCode('');
            setCurrentUser(userData);
            navigate('/');

        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    return (
        <div className="register-container">
            <div className={"go-back-div"}>
                <Link to={"/"} className={"go-back-link"}>
                    <button className={"go-back-button"}> ← </button>
                </Link>
            </div>
            <div className="register-card">
                {errorMessage && <div className="alert error">{errorMessage}</div>}
                {successMessage && <div className="alert success">{successMessage}</div>}

                {!isRegistered ? (
                    <RegisterForm
                        first_name={first_name} setFirst_name={setFirst_name}
                        last_name={last_name} setLast_name={setLast_name}
                        email={email} setEmail={setEmail}
                        password={password} setPassword={setPassword}
                        confirmPassword={confirmPassword} setConfirmPassword={setConfirmPassword}
                        isSubmitting={isSubmitting}
                        onSubmit={handleRegisterSubmit}
                    />
                ) : (
                    <VerifyForm
                        email={email}
                        verificationCode={verificationCode}
                        setVerificationCode={setVerificationCode}
                        onSubmit={handleVerifySubmit}
                    />
                )}
            </div>
        </div>
    );
};

export default Register;