import { HttpHeaders } from '@angular/common/http';

let baseUrl;
const apiPort = ':3000/'

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

const dashboard = {
  tripDistribution() {
    return `${baseUrl}api/trip/distribution`;
  },
  serviceDistribution() {
    return `${baseUrl}api/service/distribution`;
  }
}

export const Api = {
  auth,
  dashboard
}

export const options = {
  root: baseUrl
}
