import { useNavigate } from 'react-router-dom';
import './NavigationBar.css';
import NotificationButton from "../Notification/NotificationButton.jsx";
import MiniSearch from '../../miniSearch';

function NavigationBar({ user, onLogout }) {
    const navigate = useNavigate();

    return (
        <nav className="navigation-box">
            <div className="navigation-left">
                BookTo
                <div className="navigation-links">
                    <span className="navigation-link-item" onClick={() => navigate('/')}>Home</span>
                </div>
            </div>

            <div className="search-container">
                <MiniSearch />
            </div>

            {user ? (
                <div className="profile-area">
                    <NotificationButton />
                    <button
                        className="profile-icon-btn"
                        onClick={() => navigate(`/profile/`)}
                    >
                        {user.firstName[0].toUpperCase()}
                    </button>
                    <button className="logout-button" onClick={onLogout}>Log Out</button>
                </div>
            ):(
                <div className="Login-register">
                    <button className="login-button" onClick={() => navigate('/login')}>Log In</button>
                    <button className="SignUp-button" onClick={() => navigate('/register')}>Sign Up</button>
                </div>
            )}
        </nav>
    );
}

export default NavigationBar;