import { HttpHeaders } from '@angular/common/http';

let baseUrl;
const apiPort = ':80/'

//baseUrl = `https://startreker-netcracker.herokuapp.com${apiPort}`
baseUrl = `http://127.0.0.1${apiPort}`

export const HttpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
    // 'Access-Control-Allow-Origin': '*'
  })
}

export const HttpOptionsAuthorized = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
    // 'Access-Control-Allow-Origin': '*',
    // 'Authorization': `Bearer ${localStorage.getItem('at')}`,
    // 'Authorization-Refresh': `Bearer ${localStorage.getItem('rt')}`
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
    return `${baseUrl}api/v1/trip/distribution`;
  },
  serviceDistribution() {
    return `${baseUrl}api/v1/service/distribution`;
  }
}

const carrier = {
  carriers(){
    return `${baseUrl}api/v1/admin/carrier`;
  },
  getCarriersPagin(){
    return `${baseUrl}api/v1/admin/pagination`;
  },
  getCarrierByUsername(){
    return `${baseUrl}api/v1/admin/carrier-by-username?username=`;
  }
}

const costDash = {
  getCosts(){
    return `${baseUrl}api/v1/admin/costs`;
  }
}

const service = {
  services(){
    return `${baseUrl}api/v1/carrier/service`;
  },
  paginServices(){
    return `${baseUrl}api/v1/carrier/service/pagin`;
  },
  servicesByStatus(){
    return `${baseUrl}api/v1/carrier/service/by-status`;
  }
}

export const Api = {
  auth,
  dashboard,
  carrier,
  costDash,
  baseUrl,
  service
}

export const options = {
  root: baseUrl
}
