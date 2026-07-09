import { apiClient } from './apiClient';

export const statisticsApi = {
    getStatistics: () => apiClient.get('/services/statistics'),
};