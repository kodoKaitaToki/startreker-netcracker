import { HttpHeaders } from '@angular/common/http';

let baseUrl;
const apiPort = ':80/'
//const apiPort = '/'

//baseUrl = `https://startreker-netcracker.herokuapp.com${apiPort}`
baseUrl = `http://127.0.0.1${apiPort}`

export const HttpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
}

export const HttpOptionsAuthorized = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
    'Authorization': `Bearer ${localStorage.getItem('at')}`,
    'Authorization-Refresh': `Bearer ${localStorage.getItem('rt')}`
  }),
  observe: 'response' as 'response'
}

export

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
  },
  confirmPassword(){
    return `${baseUrl}api/auth/confirm-password`;
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

const bundles = {
  bundles(){
    return `${baseUrl}api/v1/admin/carrier`;
  },
  getBundlesPagin(){
    return `${baseUrl}api/v1/admin/pagination`;
  }
/*  getCarrierByUsername(){
    return `${baseUrl}api/v1/admin/carrier-by-username?username=`;
  } */
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
  HttpOptions,
  HttpOptionsAuthorized,
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

export function checkToken(heads: HttpHeaders){
  if (heads.has('New-Access-Token')) {
    localStorage.removeItem('at');
    localStorage.setItem('at', heads.get('New-Access-Token'));
  }
}
