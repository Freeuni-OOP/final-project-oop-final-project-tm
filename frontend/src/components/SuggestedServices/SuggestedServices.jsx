import { useRef } from 'react';
import ServiceCard from '../ServiceCard/ServiceCard';
import { tmp_services } from '../../UsersData/tmp_services.js';
import './SuggestedServices.css';

function SuggestedServices() {
    const sliderRef = useRef(null);

    const slideLeft = () => {
        if (sliderRef.current) {
            sliderRef.current.scrollLeft -= 360;
        }
    };

    const slideRight = () => {
        if (sliderRef.current) {
            sliderRef.current.scrollLeft += 360;
        }
    };

    return (
        <section className="suggested-section">
            <h3 className="suggested-title">Suggested Services</h3>

            <div className="slider-wrapper">
                <button className="slider-arrow-button left" onClick={slideLeft}>❮</button>

                <div className="slider-container">
                    {tmp_services.map((service) => (
                        <ServiceCard key={service.service_id} service={service} />
                    ))}
                </div>

                <button className="slider-arrow-button right" onClick={slideRight}>❯</button>
            </div>
        </section>
    );
}

export default SuggestedServices;