import 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ user, loading, children }) => {
    if (loading) {
        return <div style={{ textAlign: 'center', padding: '3rem' }}>Verifying Credentials...</div>;
    }
    if (!user) {
        return <Navigate to="/login" replace />;
    }
    return children;
};

export default ProtectedRoute;