import { useState, useEffect } from 'react';
import { servicesApi } from '../api/servicesApi';

export function useStarCounter(serviceId) {
    const [starNum, setStarNum] = useState(0);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        let cancelled = false;
        (async () => {
            setLoading(true);
            setErrorMessage('');
            try {
                const data = await servicesApi.starNum(serviceId);
                // Change data.stared to data.starNum
                if (!cancelled && data && data.starNum !== undefined) {
                    setStarNum(data.starNum);
                }
            } catch (error) {
                if (!cancelled) setErrorMessage(error.message);
            } finally {
                if (!cancelled) setLoading(false);
            }
        })();
        return () => { cancelled = true; };
    }, [serviceId]);

    return {starNum};
}