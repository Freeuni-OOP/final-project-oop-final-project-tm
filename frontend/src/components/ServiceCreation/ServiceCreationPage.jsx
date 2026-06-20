import 'react';
import ProfilePic from '../../assets/tempProfilePicture.jpeg';
import './ServiceCreationPage.css';

function ServiceCreationPage() {
    return (
        <div className="service-creation-page">
            <header className="service-creation-upper">
                <div className="profile-section">
                    <img
                        src={ProfilePic}
                        alt="Profile"
                        className="profile-picture"
                        onClick={() => window.location.href = '/user-profile'}
                    />
                </div>

                <div className="fields-section">
                    <div className="field-group">
                        <label htmlFor="service-title" className="field-label">=
                            Service Title
                        </label>
                        <input
                            id="service-title"
                            type="text"
                            className="service-title-input"
                            placeholder="Enter a name for your service"
                        />
                    </div>

                    <div className="field-group">
                        <label htmlFor="readme" className="field-label">
                            ReadMe
                        </label>
                        <textarea
                            id="readme"
                            className="readme-textarea"
                            placeholder="Describe what this service does, how to use it, and any other relevant details..."
                        />
                    </div>
                </div>
            </header>

            <section className="service-creation-lower">
                {/* Intentionally left empty for now */}
            </section>
        </div>
    );
}

export default ServiceCreationPage;