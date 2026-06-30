import { useRef } from 'react';
import ServiceCard from '../ServiceCard/ServiceCard';
import { tmp_users } from '../../UsersData/tmp_users';
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

                <div className="slider-container" ref={sliderRef}>
                    {tmp_users.map((item) => (
                        <ServiceCard key={item.id} user={item} />
                    ))}
                </div>

                <button className="slider-arrow-button right" onClick={slideRight}>❯</button>
            </div>
        </section>
    );
}

export default SuggestedServices;