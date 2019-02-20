export interface LoginResponse {
  "type": string;
  "username": string;
  "authorities": object[];
  "access_token": string;
  "refresh_token": string;
} 