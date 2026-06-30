import 'react';
import NavigationBar from '../components/NavigationBar/NavigationBar';
import SuggestedServices from '../components/SuggestedServices/SuggestedServices';
import './LandingPage.css';

function LandingPage() {
    return (
        <div className="landing-container">
            <NavigationBar />

            <main className="main-content">
                <SuggestedServices />
            </main>
        </div>
    );
}

export default LandingPage;