import { makeAutoObservable } from "mobx";
import AuthService from "../services/AuthService";
import { AuthResponse } from "../models/response/AuthResponse";
import axios from "axios";
import { API_URL } from "../http/index";
import CookieService from "../services/CookieService";



export default class Store {
    isAuth = false;
    isLoading = false;

    
    

    constructor() {
        makeAutoObservable(this);
    }

    setAuth(bool: boolean) {
        this.isAuth = bool;
    }

    setLoading(bool: boolean) {
        this.isLoading = bool;
    }



    

    async login(email: string, password: string) {
        try {
            const response = await AuthService.login(email, password);
            CookieService.setAccessToken(response.data.access_token, response.data.access_expires);
            CookieService.setRefreshToken(response.data.refresh_token, response.data.refresh_expires);
            CookieService.setRole(response.data.role); 
            CookieService.setId(response.data.id);
            this.setAuth(true);
        } catch(e) {
            const hasErrResponse = (e as { response: { [key: string]: string } }).response;
            if (!hasErrResponse) {
              throw e;
            }

        }
    }

    
    async checkAuth() {
        this.setLoading(true);
        
        try {
        
            if (CookieService.getAccessToken() && CookieService.getRefreshToken()) {
                this.setAuth(true);
                
            } else if (!CookieService.getAccessToken() && CookieService.getRefreshToken()) {
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
                        this.setAuth(true);

                    
                    })
                    .catch((error) => {
                        console.error('Error checking authentication:', error);
                        throw error;
                    }); 
                
                    
            } else {
                this.logout();
            }
            
        } catch (e) {
            const hasErrResponse = (e as { response: { [key: string]: string } }).response;
            if (!hasErrResponse) {
              throw e;
            }
        } finally {
            this.setLoading(false);
        }
    }

    
    

    async registration(email: string, password: string) {
        try {
            const response = await AuthService.registration(email, password);
        } catch(e) {
            const hasErrResponse = (e as { response: { [key: string]: string } }).response;
            if (!hasErrResponse) {
              throw e;
            }

        }
    }

    async logout() {
        try {
            const response = await AuthService.logout();
            this.setAuth(false);
        } catch(e) {
            const hasErrResponse = (e as { response: { [key: string]: string } }).response;
            if (!hasErrResponse) {
              throw e;
            }

        }
    }
}