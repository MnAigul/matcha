import $api from "../http";
import { AxiosResponse } from "axios";
import { AuthResponse } from "../models/response/AuthResponse";
import CookieService from "./CookieService";
import { API_URL } from "../http";

export default class AuthService {
    static async login(email : string, password: string) : Promise<AxiosResponse<AuthResponse>> {
        return $api.post<AuthResponse>('/api/auth/signin', {email, password});
    }

    static async registration(email : string, password: string) : Promise<AxiosResponse<string>> {
        return $api.post<string>('/api/auth/signup', {email, password});
    }

    static async logout(): Promise<void> {
        if (CookieService.getRefreshToken()) {
          const response = await fetch(`${API_URL}/api/auth/logout`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': CookieService.getRefreshToken(),
            },
            body: JSON.stringify({
              refresh_token: CookieService.getRefreshToken(),
            }),
          });
            CookieService.removeRole();
            CookieService.removeAccessToken();
            CookieService.removeRefreshToken();
            CookieService.removeId();
    
        } else {
          console.error("no refresh token");
        }
      }

}