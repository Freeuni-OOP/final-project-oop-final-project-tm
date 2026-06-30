import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

function ServicePage() {
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
            {/*service details */}
        </div>
    );
}

export default ServicePage;