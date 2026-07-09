import 'react';
import { useNavigate, useOutletContext } from 'react-router-dom';
import SuggestedServices from '../components/SuggestedServices/SuggestedServices';
import AboutUs from "../components/AboutUs/AboutUs.jsx";
import './LandingPage.css';

function LandingPage() {
    const navigate = useNavigate();
    const { currentUser } = useOutletContext();

    const handleJoinClick = () => {
        if (currentUser) {
            navigate('/service-creation');
        } else {
            navigate('/register');
        }
    };

    return (
        <div className="landing-container">
            <main className="main-content">
                <AboutUs />
                <SuggestedServices />
            </main>
            <section className="join-section">
                <div>
                    <h2 className="join-text" onClick={handleJoinClick}>
                        Join Us!
                    </h2>
                    <p className="join-section-subtitle">
                        start your journey with us, register and upload your first service!
                    </p>
                </div>
            </section>
        </div>
    );
}

export default LandingPage;