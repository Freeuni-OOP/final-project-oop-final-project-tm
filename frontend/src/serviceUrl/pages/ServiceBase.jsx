import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import ProfilePicture from './ProfilePicture'; // <-- Make sure to import this!
import './ServiceBase.css';

function ServiceBase() {
    const { serviceId } = useParams();

    // Data States
    const [service, setService] = useState(null);
    const [providerImage, setProviderImage] = useState(null);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');

    // User Action States
    const [isStarred, setIsStarred] = useState(false);
    // --- API CALL FOR SERVICE DATA ---
    useEffect(() => {
        const fetchService = async () => {
            setLoading(true);
            setErrorMessage('');
            try {
                const response = await fetch(`http://localhost:8080/api/services/${serviceId}`);
                if (!response.ok) {
                    throw new Error('Service not found');
                }
                const data = await response.json();
                setService(data);
            } catch (error) {
                setErrorMessage(error.message);
            } finally {
                setLoading(false);
            }
        };
        fetchService();
    }, [serviceId]);

    // --- API CALL FOR PROVIDER PROFILE PICTURE ---
    useEffect(() => {
        const fetchProfilePic = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/services/profile/${serviceId}`);
                if (response.ok) {
                    console.log("Profile picture response:", response);
                    const data = await response.json();
                    setProviderImage(data.imagePath);
                }
            } catch (error) {
                console.error("Failed to fetch profile picture", error);
            }
        };
        fetchProfilePic();
    }, []);

    // --- API CALL FOR STARRING ---
    const handleStarClick = async () => {
        try {
            const method = isStarred ? 'DELETE' : 'POST';
            const response = await fetch(`http://localhost:8080/api/services/${serviceId}/star`, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
            });

            if (response.ok) {
                setIsStarred(!isStarred);
            }
        } catch (error) {
            console.error("Failed to update star status", error);
        }
    };

    if (loading) return <div>Loading...</div>;
    if (errorMessage) return <div>{errorMessage}</div>;

    return (
        <div className="service-page-container">
            {/* TOP HALF: Image, Title, Stats, and Bio */}
            <div className="service-top-half">
                <div className="service-profile-container">
                    <ProfilePicture image={providerImage} />
                </div>

                <div className="service-info-container">

                    {/* UPDATED: Title first, then Location and Category grouped side-by-side */}
                    <div className="service-header-group">
                        <h1 className="service-title">{service?.serviceTitle}</h1>

                        <div className="service-location-meta">
                            <p className="service-address">
                                📍 {service?.serviceAddress || "Remote / No location specified"}
                            </p>
                            <span className="service-category">
                                🏷️ {service?.serviceCategory || "Uncategorized"}
                            </span>
                        </div>
                    </div>

                    <div className="service-stats-row">
                        <div className="stat-badge">⭐ <strong>4.9</strong></div>
                        <div className="stat-badge">✅ <strong>340</strong> Completed</div>
                        <div className="stat-badge">⚡ <strong>1 hr</strong> Response Time</div>
                    </div>

                    {/* NEW: Price integrated with Action Buttons */}
                    <div className="service-price-and-actions">
                        <div className="service-price">
                            <h2>${service?.servicePrice || "0.00"}</h2>
                            <span className="price-subtitle">per service</span>
                        </div>

                        <div className="service-actions-row">
                            <button
                                className={`action-btn ${isStarred ? 'btn-starred' : 'btn-outline'}`}
                                onClick={handleStarClick}
                            >
                                {isStarred ? '⭐ Starred' : '☆ Star'}
                            </button>

                            <div className="service-action-btn">
                                <Link to={`/profile/${service?.serviceProfileId || 1}`} className="action-btn">
                                    Creator Profile
                                </Link>
                            </div>
                        </div>
                    </div>

                    <div className="service-description">
                        <h3 className="service-description-label">About this service</h3>
                        <p className="service-description-text">
                            {service?.serviceBio || "No description provided for this service."}
                        </p>
                    </div>
                </div>
            </div>

            {/* BOTTOM HALF: Two Calendars */}
            <div className="service-bottom-half">
                <div className="calendar-container">
                    <h3 className="calendar-title">Availability</h3>
                    <div className="calendar-placeholder">
                        <p>Calendar 1 will go here</p>
                    </div>
                </div>

                <div className="calendar-container">
                    <h3 className="calendar-title">Your Calendar</h3>
                    <div className="calendar-placeholder">
                        <p>Calendar 2 will go here</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ServiceBase;