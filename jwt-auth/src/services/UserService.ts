import $api from "../http";
import {AxiosResponse} from 'axios';
import {IUser} from "../models/IUser";
import { UserFull } from "../models/UserFull";
import Long from 'long';
import { UserGeneral } from "../models/UserGeneral";

export default class UserService {
    static fetchUsers(): Promise<AxiosResponse<UserGeneral[]>> {
        return $api.get<UserGeneral[]>('/users/getUsers');
    }

    static fetchMyUser(id: string): Promise<AxiosResponse<UserFull>> {
        return $api.get<UserFull>('/users/getProfile', {
            params: {
                id: id
        }});
    }

    static updateUser(id: string, updatedData: Partial<UserFull>): Promise<AxiosResponse<UserFull>> {
        return $api.post<UserFull>(`/users/updateProfile/${id}`, updatedData);
    }

    static uploadFile(id: string, formData: FormData): Promise<any> {
        return $api.post<any>(`/files/uploadFile/${id}`, formData);
    }

    static getPhoto(id: string): Promise<any> {
        return $api.get<FormData>(`/files/getPhoto/${id}`);
    }

    static updateRole(id: string, role: string): Promise<any> {
        role = "ROLE_" + role;
        console.log("SEND ROLE =    ", role );
        return $api.post<any>(`users/updateRole/${id}`, role);
    }



    
}