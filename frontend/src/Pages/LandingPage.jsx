import 'react';
import SuggestedServices from '../components/SuggestedServices/SuggestedServices';
import './LandingPage.css';

function LandingPage() {
    return (
        <div className="landing-container">
            <main className="main-content">
                <SuggestedServices />
            </main>
        </div>
    );
}

export default LandingPage;