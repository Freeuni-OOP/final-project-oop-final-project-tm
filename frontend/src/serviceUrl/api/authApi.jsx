import { apiClient } from './apiClient';

export const authApi = {
    verifySession: () => apiClient.get('/auth/verify-session'),
};