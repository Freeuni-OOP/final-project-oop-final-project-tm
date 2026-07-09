import { useState, useEffect } from 'react';
import { statisticsApi } from '../api/statisticsApi';

export function useFetchStatistics(serviceId) {
    const [statistics, setStatistics] = useState({
        star: 0
    });

    useEffect(() => {
        let cancelled = false;
        statisticsApi.getStatistics(serviceId).then(data => {
            if (!cancelled) setStatistics(data);
        });
        return () => { cancelled = true; };
    }, [serviceId]);

    return statistics;
}