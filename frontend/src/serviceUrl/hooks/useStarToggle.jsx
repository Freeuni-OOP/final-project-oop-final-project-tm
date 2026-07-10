import { useState, useEffect } from 'react';
import { servicesApi } from '../api/servicesApi';

export function useStarToggle(serviceId) {
    const [isStarred, setIsStarred] = useState(false);

    useEffect(() => {
        // Helper async function to safely handle the promise
        const fetchStarStatus = async () => {
            try {
                const response = await servicesApi.stared(serviceId); // Added await here
                if (response && response.stared === true) {
                    setIsStarred(true);
                }
            } catch (error) {
                  console.error('Failed to fetch star status', error);
            }
        };

        if (serviceId) {
            fetchStarStatus();
        }
    }, [serviceId]);

    const toggle = async () => {
        try {
            isStarred
                ? await servicesApi.unstar(serviceId)
                : await servicesApi.star(serviceId);
            setIsStarred((prev) => !prev);
        } catch (error) {
            console.error('Failed to update star status', error);
        }
    };

    return [isStarred, toggle];
}