import { useState, useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

/**
 * Root application component.
 * Renders the landing page (list of profiles) as the main page content.
 */
function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

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

  const handleLogout = async () => {
    try {
      await fetch('http://localhost:8080/api/auth/logout', {
        method: 'POST',
        credentials: 'include'
      });
    } catch (error) {
      console.error("Logout failed:", error);
    } finally {
      setCurrentUser(null);
      navigate('/')
    }
  };

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '3rem' }}>Loading...</div>;
  }

  return <Outlet context={{ currentUser, setCurrentUser, handleLogout }} />;
}

export default App;
