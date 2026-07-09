import { Link } from 'react-router-dom';
import ProfilePicture from '../common/ProfilePicture';

function ServiceHeader({ service, providerImage, isStarred, onStarClick }) {
    return (
        <div className="service-top-half">
            <div className="service-profile-container">
                <ProfilePicture image={providerImage} />
            </div>

            <div className="service-info-container">
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

                <div className="service-price-and-actions">
                    <div className="service-price">
                        <h2>${service?.servicePrice || "0.00"}</h2>
                        <span className="price-subtitle">per service</span>
                    </div>

                    <div className="service-actions-row">
                        <button
                            className={`action-btn ${isStarred ? 'btn-starred' : 'btn-outline'}`}
                            onClick={onStarClick}
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
    );
}

export default ServiceHeader;