import { apiClient } from './apiClient';

export const servicesApi = {
    getById: (serviceId) => apiClient.get(`/services/${serviceId}`),
    getProviderProfile: (serviceId) => apiClient.get(`/services/profile/${serviceId}`),
    star: (serviceId) => apiClient.post(`/services/${serviceId}/star`),
    unstar: (serviceId) => apiClient.delete(`/services/${serviceId}/star`),
    stared: (serviceId) => apiClient.get(`/services/${serviceId}/star`),
    starNum: (serviceId) => apiClient.get(`/services/${serviceId}/star-num`),
};