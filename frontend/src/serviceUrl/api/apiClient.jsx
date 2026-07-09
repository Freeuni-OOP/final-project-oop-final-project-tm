import { API_BASE_URL } from './config';
const BASE_URL = API_BASE_URL;

export class ApiError extends Error {
    constructor(message, status) {
        super(message);
        this.status = status;
    }
}

async function request(path, { method = 'GET', body, headers = {} } = {}) {
    const response = await fetch(`${BASE_URL}${path}`, {
        method,
        headers: { 'Content-Type': 'application/json', ...headers },
        credentials: 'include', // sends the JWT cookie
        body: body ? JSON.stringify(body) : undefined,
    });

    if (response.status === 401) {
        throw new ApiError(response.error(), 401);
    }

    if (!response.ok) {
        const message = await response.text().catch(() => response.statusText);
        throw new ApiError(message || 'Request failed', response.status);
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

export const apiClient = {
    get: (path) => request(path),
    post: (path, body) => request(path, { method: 'POST', body }),
    put: (path, body) => request(path, { method: 'PUT', body }),
    delete: (path) => request(path, { method: 'DELETE' }),
};