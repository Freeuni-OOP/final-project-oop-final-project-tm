import 'react';
import './ServiceCard.css';

function ServiceCard({user}) {
    return (
        <div className="service-card">
            <img
                src={user.avatarUrl}
                alt={user.username}
                className="user-avatar"
            />
            <div className="user-info">
                <h4 className="provider-title">{user.title}</h4>
                <p className="provider-desc">{user.description}</p>
            </div>
        </div>
    );
}

export default ServiceCard;