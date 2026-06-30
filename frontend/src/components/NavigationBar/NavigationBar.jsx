import 'react';
import './NavigationBar.css';

function NavigationBar() {
    return (
        <nav className={"navigation-box"}>
            <div className={"navigation-left"}>
                BookTo
                <div className="navigation-links">
                    <span className="navigation-link-item">Home</span>
                </div>
            </div>
            <button className="login-button">
                Log In
            </button>
            <button className="SignUp-button">
                Sign Up
            </button>
            <div className="search-container">
                <span className="search-icon">⌕</span>
                <input
                    type="text"
                    placeholder="Search..."
                    className="search-input"
                />
            </div>
        </nav>
    );
}
export default NavigationBar;
