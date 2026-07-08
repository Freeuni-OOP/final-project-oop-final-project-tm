import 'react';
import { useNavigate } from 'react-router-dom';
import './ServiceCard.css';

function ServiceCard({ service }) {
    const navigate = useNavigate();

    return (
        <div
            className="service-card"
            onClick={() => navigate(`/services/${service.id}`)}
        >
            <img
                src={service.imagePath || 'https://via.placeholder.com/80'}
                alt={'👤'}
                className="user-avatar"
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