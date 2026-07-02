import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom'; // 1. Swapped useNavigate for Link

function ServiceBase() {
    const { serviceId } = useParams();
    const [service, setService] = useState(null);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');

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

    if (loading) return <div>Loading...</div>;
    if (errorMessage) return <div>{errorMessage}</div>;

    return (
        <div className="service-page">
            <h1>{service.name}</h1>
            <div className="service-service">
                <header className="service-service-header">
                    <div className="service-profile-section">
                        {/* 3. Wrapped the profile picture in a Link component */}
                        <Link to="/user-profile">
                            <img
                                src="ProfilePic"
                                alt="profile"
                                className="service-profile-picture"
                                // Removed the onClick event from here
                            />
                        </Link>
                    </div>
                    <div className="service-fields-section">
                        <div className="service.description">
                            <label htmlFor="description" className="service-title-label">
                                Description
                            </label>
                            <textarea className="service-description-text-area" placeholder="Nothing for now :p"/>
                        </div>
                    </div>
                </header>
                <div className="home-return-class">
                    {/* 4. Wrapped the Home button in a Link component */}
                    <Link to="/">
                        <button className="service-return-home-page-button">
                            home
                        </button>
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default ServiceBase;