import Long from "long";

export interface AuthResponse {
    access_token: string;
    refresh_token: string;
    success: boolean;
    access_expires: Long;
    refresh_expires: Long;
    role: string;
    id: string;
}