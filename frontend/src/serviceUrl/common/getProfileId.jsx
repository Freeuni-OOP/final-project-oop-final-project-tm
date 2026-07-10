import { useState, useEffect } from 'react';
import { apiClient } from '../api/apiClient';

export function useCurrentUser() {
    const [userId, setUserId] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        let cancelled = false;
        apiClient.get('/profile/')
            .then((data) => {
                if (!cancelled && data && data.id) {
                    setUserId(data.id);
                }
            })
            .catch((error) => console.error('Failed to fetch current user', error))
            .finally(() => { if (!cancelled) setLoading(false); });
        return () => { cancelled = true; };
    }, []);

    return { userId, loading };
}