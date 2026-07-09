import { useState, useEffect } from 'react';
import { servicesApi } from '../api/servicesApi';

export function useProviderImage(serviceId) {
    const [providerImage, setProviderImage] = useState(null);

    useEffect(() => {
        let cancelled = false;
        servicesApi.getProviderProfile(serviceId)
            .then((data) => { if (!cancelled) setProviderImage(data.imagePath); })
            .catch((error) => console.error('Failed to fetch profile picture', error));
        return () => { cancelled = true; };
    }, [serviceId]);

    return providerImage;
}