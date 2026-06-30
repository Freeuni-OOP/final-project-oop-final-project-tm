import { useState, useEffect } from 'react';
import { Outlet } from 'react-router-dom';

function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkUserSession = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/auth/verify-session', {
          credentials: 'include'
        });
        if (response.ok) {
          const userData = await response.json();
          setCurrentUser(userData);
        }
      } catch (error) {
        console.error("Session network evaluation failed:", error);
      } finally {
        setLoading(false);
      }
    };
    checkUserSession();
  }, []);

  const handleLogout = () => {
    setCurrentUser(null);
  };

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '3rem' }}>Loading...</div>;
  }

  return <Outlet context={{ currentUser, setCurrentUser, handleLogout }} />;
}

export default App;