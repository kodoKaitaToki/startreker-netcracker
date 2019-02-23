import { HttpHeaders } from '@angular/common/http';

let baseUrl;
const apiPort = ':80/'

baseUrl = `http://127.0.0.1${apiPort}`

export const HttpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
    'Access-Control-Allow-Origin': '*'
  })
}

export const HttpOptionsAuthorized = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
    'Access-Control-Allow-Origin': '*',
    'Authorization': `Bearer ${localStorage.getItem('at')}`,
    'Authorization-Refresh': `Bearer ${localStorage.getItem('rt')}`
  })
}

const auth = {
  loginUser() {
    return `${baseUrl}api/auth/sign-in`;
  },
  registerUser() {
    return `${baseUrl}api/auth/sign-up`;
  },
  logoutUser() {
    return `${baseUrl}api/log-out`;
  },
  recoverPassword() {
    return `${baseUrl}api/auth/password-recovery`;
  }
}

const carrier = {
  carriers(){
    return `${baseUrl}api/v1/admin/carrier`;
  },
  getCarriersPagin(){
    return `${baseUrl}api/v1/admin/pagination`;
  }
}

export const Api = {
  auth, carrier
}

export const options = {
  root: baseUrl
}
