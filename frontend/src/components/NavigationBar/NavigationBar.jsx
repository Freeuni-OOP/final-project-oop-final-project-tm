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
            <div className="search-container">
                <span className="search-icon">⌕</span>
                <input
                    type="text"
                    placeholder="Search..."
                    className="search-input"
                />
            </div>
            <div className={"Login-register"}>
                <button className="login-button">
                    Log In
                </button>
                <button className="SignUp-button">
                    Sign Up
                </button>
            </div>
        </nav>
    );
}
export default NavigationBar;
