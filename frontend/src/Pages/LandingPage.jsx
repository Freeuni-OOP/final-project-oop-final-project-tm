import 'react';
import { useNavigate } from 'react-router-dom';
import SuggestedServices from '../components/SuggestedServices/SuggestedServices';
import AboutUs from "../components/AboutUs/AboutUs.jsx";
import './LandingPage.css';

function LandingPage() {
    const navigate = useNavigate();

    return (
        <div className="landing-container">
            <main className="main-content">
                <AboutUs />
                <SuggestedServices />
            </main>
            <section className="join-section">
                <div>
                    <h2 className="join-text" onClick={() => navigate('/register')}>
                        Join Us!
                    </h2>
                    <p className={"join-section-subtitle"}>
                        start your journey with us, register and upload your first service!
                    </p>
                </div>
            </section>
        </div>
    );
}

export default LandingPage;