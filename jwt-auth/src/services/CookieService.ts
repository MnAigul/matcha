import { Cookies } from "react-cookie";
import Long from "long";

export default class CookieService {

  static cookies = new Cookies();



  static getRefreshToken() {
    return this.cookies.get('refresh_token');
  }

  static setRefreshToken(refreshToken: string, expirationInMilliseconds: Long) {
    const expirationDate = new Date(Long.fromValue(Date.now()).add(expirationInMilliseconds).toNumber());
    this.cookies.set('refresh_token', refreshToken,  { path: '/', expires: expirationDate });
  }

  static removeRefreshToken() {
    this.cookies.remove('refresh_token', { path: '/' });
  }

  static getAccessToken() {
    return this.cookies.get('access_token');
  }

  static setAccessToken(access_token: string, expirationInMilliseconds: Long) {
    const expirationDate = new Date(Long.fromValue(Date.now()).add(expirationInMilliseconds).toNumber());
    console.log("ACCESS = " +expirationDate );
    this.cookies.set('access_token', access_token,  { path: '/', expires: expirationDate });
  }

  static removeAccessToken() {
    this.cookies.remove('access_token', { path: '/' });
  }

  static setRole(role: string) {
    return this.cookies.set("role", role);
  }

  static getRole() {
    return this.cookies.get("role");
  }

  static removeRole() {
    this.cookies.remove("role");
  }

  static setId(id: string) {
    return this.cookies.set("id", id);
  }

  static getId() {
    return this.cookies.get("id");
  }

  static removeId() {
    this.cookies.remove("id");
  }
  
}