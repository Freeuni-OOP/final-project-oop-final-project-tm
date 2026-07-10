import 'react'
import {Link} from "react-router-dom";
import './PageNotFound.css'

function PageNotFound() {

    return (
        <div className="not-found-container">
            <div className="not-found-card">
                <div className="error-code">404</div>
                <h1 className="not-found-title">Page Not Found</h1>
                <Link to="/" className="home-link">
                    <button className="home-button">Go to Home Page</button>
                </Link>
            </div>
        </div>
    );
}
export default PageNotFound