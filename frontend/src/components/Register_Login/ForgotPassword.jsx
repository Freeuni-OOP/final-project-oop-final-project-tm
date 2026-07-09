import { useState } from 'react';
import {Link, useNavigate} from 'react-router-dom';
import VerifyForm from './VerifyForm';
import './ForgotPassword.css';

const ForgotPassword = () => {
    const navigate = useNavigate();

    const [step, setStep] = useState(1);
    const [email, setEmail] = useState('');
    const [code, setCode] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleEmailSubmit = async (e) => {
        e.preventDefault();
        if (isSubmitting) return;
        setIsSubmitting(true);
        setErrorMessage('');
        try {
            const response = await fetch('http://localhost:8080/api/auth/forgot-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email }),
            });

            if (!response.ok) throw new Error(await response.text());

            setStep(2);
        } catch (error) {
            setErrorMessage(error.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleCodeSubmit = (e) => {
        e.preventDefault();
        if (!code) {
            setErrorMessage('Please enter the code');
            return;
        }
        setErrorMessage('');
        setSuccessMessage('');
        setStep(3);
    };

    const handleResetSubmit = async (e) => {
        e.preventDefault();
        if (isSubmitting) return;

        if (newPassword !== confirmPassword) {
            setErrorMessage('Passwords do not match');
            return;
        }

        setIsSubmitting(true);
        setErrorMessage('');

        try {
            const response = await fetch('http://localhost:8080/api/auth/reset-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, code, newPassword }),
            });

            const text = await response.text();
            if (!response.ok) throw new Error(text);

            setSuccessMessage(text);
            setTimeout(() => navigate('/login'), 2000);
        } catch (error) {
            setErrorMessage(error.message);
            if (error.message.includes('Invalid')) setStep(2);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="login-container">
            <div className={"go-back-div"}>
                <Link to={"/login"} className={"go-back-link"}>
                    <button className={"go-back-button"}> ← </button>
                </Link>
            </div>

            <div className="forgot-card">
                {errorMessage && <div className="alert error">{errorMessage}</div>}
                {successMessage && <div className="alert success">{successMessage}</div>}

                {step === 1 && (
                    <form onSubmit={handleEmailSubmit}>
                        <h2>Forgot your Password?</h2>
                        <p className="forgot-subtitle">
                            Enter the email for your account. we will send a reset code
                        </p>
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
                        <button type="submit" className="forgot-btn" disabled={isSubmitting}>
                            {isSubmitting ? 'Sending...' : 'Send Reset Code'}
                        </button>
                    </form>
                )}

                {step === 2 && (
                    <VerifyForm
                        email={email}
                        verificationCode={code}
                        setVerificationCode={setCode}
                        onSubmit={handleCodeSubmit}
                        title="Enter Reset Code"
                        buttonText="Verify Code"
                    />
                )}

                {step === 3 && (
                    <form onSubmit={handleResetSubmit}>
                        <h2>New Password</h2>
                        <div className="input-group">
                            <label htmlFor="newPassword">New Password</label>
                            <input
                                type="password"
                                id="newPassword"
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                                required
                                placeholder="Enter new password"
                            />
                        </div>
                        <div className="input-group">
                            <label htmlFor="confirmPassword">Confirm Password</label>
                            <input
                                type="password"
                                id="confirmPassword"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                                placeholder="Confirm new password"
                            />
                        </div>
                        <button type="submit" className="forgot-btn" disabled={isSubmitting}>
                            {isSubmitting ? 'Resetting...' : 'Reset Password'}
                        </button>
                    </form>
                )}
            </div>
        </div>
    );
};

export default ForgotPassword;