import 'react';
import SuggestedServices from '../components/SuggestedServices/SuggestedServices';
import AboutUs from "../components/HowItWorks/AboutUs.jsx";
import './LandingPage.css';

function LandingPage() {
    return (
        <div className="landing-container">
            <main className="main-content">
                <AboutUs />
                <SuggestedServices />
            </main>
        </div>
    );
}

export default LandingPage;