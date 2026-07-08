import { useRef, useState, useEffect } from 'react';
import ServiceCard from '../ServiceCard/ServiceCard';
import './SuggestedServices.css';

function SuggestedServices() {
    const sliderRef = useRef(null);
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchServices = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/miniServices/suggested');
                if (!response.ok) throw new Error('Failed to load services');
                const data = await response.json();
                setServices(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        fetchServices();
    }, []);

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

            {loading && <p>Loading services...</p>}
            {error && <p>Error: {error}</p>}
            {!loading && !error && (
                <div className="slider-wrapper">
                    <button className="slider-arrow-button left" onClick={slideLeft}>❮</button>
                    <div className="slider-container" ref={sliderRef}>
                        {services.map((service) => (
                            <ServiceCard key={service.id} service={service} />
                        ))}
                    </div>
                    <button className="slider-arrow-button right" onClick={slideRight}>❯</button>
                </div>
            )}
        </section>);
}

export default SuggestedServices;