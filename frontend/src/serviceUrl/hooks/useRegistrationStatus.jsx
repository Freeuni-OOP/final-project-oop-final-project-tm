import { useState, useEffect } from 'react';
import { authApi } from '../api/authApi';

export function useRegistrationStatus() {
    const [isRegistered, setIsRegistered] = useState(true);

    useEffect(() => {
        let cancelled = false;
        authApi.verifySession()
            .catch(() => { if (!cancelled) setIsRegistered(false); });
        return () => { cancelled = true; };
    }, []);

    return isRegistered;
}