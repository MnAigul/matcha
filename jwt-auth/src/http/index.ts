import axios from 'axios';
import { AuthResponse } from '../models/response/AuthResponse';
import CookieService from '../services/CookieService';
import Long from 'long';
import AuthService from '../services/AuthService';
import Store from '../store/store';

export const API_URL = 'http://localhost:8075';



const $api = axios.create({
    withCredentials: true,
    baseURL: API_URL
})

$api.interceptors.request.use((config) => {
    config.headers.Authorization = `${CookieService.getAccessToken()}`;
    return config;
})

$api.interceptors.response.use((config) => {
    return config;
},async (error) => {
    const originalRequest = error.config;
    if (error.response.status == 401 && error.config && !error.config._isRetry) {
        originalRequest._isRetry = true;
        try {
            if (!CookieService.getAccessToken() && CookieService.getRefreshToken()) {
                console.log("have not access");
                fetch(`${API_URL}/api/auth/token/refresh`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': CookieService.getRefreshToken(),
                    },
                    body: JSON.stringify({
                        refresh_token: CookieService.getRefreshToken(),
                    }),
                })
                    .then((response) => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then((data) => {
                        CookieService.setAccessToken(data.access_token, data.access_expires);
                        CookieService.setRefreshToken(data.refresh_token, data.refresh_expires);
                        return $api.request(originalRequest);
                    })
                    .catch((error) => {
                        console.error('Error refreshing token:', error);
                        throw error;
                    }); 
            } else {
                const store = new Store();
                store.logout();
                // throw new Error("No refresh token");
            }
            
        } catch (e) {
            AuthService.logout();
            // console.log('НЕ АВТОРИЗОВАН');
            window.location.href = `/`;
        }
    }
    throw error;
})

export default $api;