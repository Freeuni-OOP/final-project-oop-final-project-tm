import { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';

function ProtectedRoute({ children }) {
    const [isAuthenticated, setIsAuthenticated] = useState(null);

    useEffect(() => {
        // This function asks the backend if the cookie is valid
        const verifyUser = async () => {
            try {
                const response = await fetch('/api/auth/verify' , { credentials: 'include' });
                if (response.ok) {
                    setIsAuthenticated(true);
                } else {
                    setIsAuthenticated(false);
                }
            } catch (error) {
                setIsAuthenticated(false);
            }
        };

        verifyUser();
    }, []);

    // Show nothing (or a spinner) while waiting for the backend to reply
    if (isAuthenticated === null) {
        return <div>Loading...</div>;
    }

    // If logged in, render the child component (Your ServiceCreationPage)
    // If not, kick them to the login route
    return isAuthenticated ? children : <Navigate to="/login" />;
}

export default ProtectedRoute;