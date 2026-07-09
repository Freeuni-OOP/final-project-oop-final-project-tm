import { useState, useEffect } from 'react';
import { servicesApi } from '../api/servicesApi';

export function useServiceData(serviceId) {
    const [service, setService] = useState(null);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        let cancelled = false;
        (async () => {
            setLoading(true);
            setErrorMessage('');
            try {
                const data = await servicesApi.getById(serviceId);
                if (!cancelled) setService(data);
            } catch (error) {
                if (!cancelled) setErrorMessage(error.message);
            } finally {
                if (!cancelled) setLoading(false);
            }
        })();
        return () => { cancelled = true; };
    }, [serviceId]);

    return { service, loading, errorMessage };
}