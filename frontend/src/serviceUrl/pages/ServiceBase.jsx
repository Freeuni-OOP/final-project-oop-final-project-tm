import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import './ServiceBase.css';

function ServiceBase() {
    const { serviceId } = useParams();

    // Data States
    const [service, setService] = useState(null);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');

    // User Action States
    const [isStarred, setIsStarred] = useState(false);
    const [isFollowed, setIsFollowed] = useState(false);

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

                // If your backend returns whether the current user has already
                // starred/followed this service, you would set it here:
                // setIsStarred(data.hasUserStarred);
                // setIsFollowed(data.hasUserFollowed);

            } catch (error) {
                setErrorMessage(error.message);
            } finally {
                setLoading(false);
            }
        };
        fetchService();
    }, [serviceId]);

    // --- API CALL FOR STARRING ---
    const handleStarClick = async () => {
        try {
            // If already starred, we send a DELETE request. If not, a POST request.
            const method = isStarred ? 'DELETE' : 'POST';
            const response = await fetch(`http://localhost:8080/api/services/${serviceId}/star`, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                // headers: { 'Authorization': `Bearer ${userToken}` } // <-- Add this later for Auth
            });

            if (response.ok) {
                setIsStarred(!isStarred); // Toggle the UI state on success
            }
        } catch (error) {
            console.error("Failed to update star status", error);
        }
    };

    // --- API CALL FOR FOLLOWING ---
    const handleFollowClick = async () => {
        try {
            const method = isFollowed ? 'DELETE' : 'POST';
            const response = await fetch(`http://localhost:8080/api/services/${serviceId}/follow`, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
            });

            if (response.ok) {
                setIsFollowed(!isFollowed);
            }
        } catch (error) {
            console.error("Failed to update follow status", error);
        }
    };

    if (loading) return <div>Loading...</div>;
    if (errorMessage) return <div>{errorMessage}</div>;

    return (
        <div className="service-page-container">
            {/* TOP HALF: Image, Title, Stats, and Bio */}
            <div className="service-top-half">
                <div className="service-profile-container">
                    <Link to="/user-profile">
                        <img
                            src={service.profilePic || "/default-profile.png"}
                            alt={`${service.name} profile`}
                            className="service-profile-picture-large"
                        />
                    </Link>
                </div>

                <div className="service-info-container">
                    <h1 className="service-title">{service.name}</h1>

                    <div className="service-stats-row">
                        <div className="stat-badge">⭐ <strong>4.9</strong> (120 Reviews)</div>
                        <div className="stat-badge">👥 <strong>1.5k</strong> Followers</div>
                        <div className="stat-badge">✅ <strong>340</strong> Completed</div>
                        <div className="stat-badge">⚡ <strong>1 hr</strong> Response Time</div>
                    </div>

                    {/* NEW: Action Buttons */}
                    <div className="service-actions-row">
                        <button
                            className={`action-btn ${isStarred ? 'btn-starred' : 'btn-outline'}`}
                            onClick={handleStarClick}
                        >
                            {isStarred ? '⭐ Starred' : '☆ Star'}
                        </button>

                        <button
                            className={`action-btn ${isFollowed ? 'btn-followed' : 'btn-primary'}`}
                            onClick={handleFollowClick}
                        >
                            {isFollowed ? 'Following' : 'Follow'}
                        </button>
                    </div>

                    <div className="service-description">
                        <h3 className="service-description-label">About this service</h3>
                        <p className="service-description-text">
                            {service.serviceBio || "No description provided for this service."}
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
                    <h3 className="calendar-title">Bookings</h3>
                    <div className="calendar-placeholder">
                        <p>Calendar 2 will go here</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ServiceBase;