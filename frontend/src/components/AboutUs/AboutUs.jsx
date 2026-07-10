import 'react';
import './AboutUs.css';

const AboutUs = () => {
    const steps = [
        {
            id: 1,
            icon: "🔍",
            title: "Search & Compare",
            description: "Browse profiles, read verified reviews, and compare prices for local professionals."
        },
        {
            id: 2,
            icon: "📅",
            title: "Pick a Time",
            description: "Use our live calendar to find a slot that fits your schedule perfectly."
        },
        {
            id: 3,
            icon: "⭐",
            title: "Book & Relax",
            description: "Confirm your appointment instantly and enjoy."
        }
    ];

    return (
        <section className="how-it-works-section">
            <div className="container">
                <h2 className="section-title">Book To</h2>
                <p className="section-subtitle">Book your next service in three easy steps.</p>

                <div className="steps-grid">
                    {steps.map((step) => (
                        <div key={step.id} className="step-card">
                            <div className="step-icon-wrapper">
                                <span className="step-icon">{step.icon}</span>
                                <div className="step-number">{step.id}</div>
                            </div>
                            <h3 className="step-title">{step.title}</h3>
                            <p className="step-description">{step.description}</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
};

export default AboutUs;