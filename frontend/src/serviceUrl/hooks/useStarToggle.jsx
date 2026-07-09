import { useState } from 'react';
import { servicesApi } from '../api/servicesApi';

export function useStarToggle(serviceId, initial = false) {
    const [isStarred, setIsStarred] = useState(initial);

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