import 'react';
import { useNavigate } from 'react-router-dom';

function LoginPage() {
    const navigate = useNavigate();

    // 1. THIS is where your POST function lives
    const handleDummyLogin = async () => {
        try {
            // Send the POST request to get the cookie
            await fetch('/api/auth/dummy-login', { method: 'POST', credentials: 'include'});

            // Once the browser has the cookie, redirect the user to the protected page!
            navigate('/create-service');
        } catch (error) {
            console.error("Login failed", error);
        }
    };

    return (
        <div style={{ padding: '50px', textAlign: 'center', color: 'white' }}>
            <h2>Login Page</h2>
            <p>Click the button below to get your secure cookie.</p>

            {/* 2. The function is triggered when the user clicks the button */}
            <button
                onClick={handleDummyLogin}
                style={{ padding: '10px 20px', cursor: 'pointer' }}
            >
                Give me the Dummy Cookie!
            </button>
        </div>
    );
}

export default LoginPage;