import 'react';
import { useNavigate } from 'react-router-dom';
import './ServiceCard.css';

function ServiceCard({ service }) {
    const navigate = useNavigate();
    const defaultImage = 'https://placehold.co/100x100/dae3ef/1b1850?text=No+Image';

    return (
        <div
            className="service-card"
            onClick={() => navigate(`/services/${service.id}`)}
        >
            <img
                src={service.imagePath || defaultImage}
                alt={service.title || "Service"}
                className="user-avatar"
                onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = defaultImage;
                }}
            />
            <div className="user-info">
                <h4 className="provider-title">{service.title}</h4>
                <p className="provider-desc">{service.bio}</p>

                <div className="service-details">
                    <span className="service-category">{service.category}</span>
                    <span className="service-price">${parseFloat(service.price).toFixed(2)}</span>
                </div>
            </div>
        </div>
    );
}

export default ServiceCard;