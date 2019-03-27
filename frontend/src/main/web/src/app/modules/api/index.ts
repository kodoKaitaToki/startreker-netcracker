import { HttpHeaders } from '@angular/common/http';

let baseUrl;
const apiPort = ':80/'
//const apiPort = '/'

// baseUrl = `http://startreker-netcracker.herokuapp.com${apiPort}`
baseUrl = `http://localhost${apiPort}`

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
  },
  getCarrierNumber(){
    return `${baseUrl}api/v1/admin/carrier/amount`;
  },
  getCarrierTrips(){
    return `${baseUrl}api/v1/carrier/trip`;
  }
}

const bundles = {
  bundles(){
    return `${baseUrl}api/v1/admin/carrier`;
  },
  getBundlesPagin(){
    return `${baseUrl}api/v1/admin/pagination`;
  }
}
const trips = {
  getExistingPlanets() {
    return `${baseUrl}api/v1/planets-spaceports`;
  },
  addTrip() {
    return `${baseUrl}api/v1/trips`;
  },
  updateTrip(id) {
    return `${baseUrl}api/v1/trips/${id}`
  },
  getAllTrips(limit: number = 10, offset: number = 0) {
    // return `${baseUrl}api/v1/trips`;
    return `${baseUrl}api/v1/trips/paging?limit=${limit}&offset=${offset}`
  },
  getCarrierTrips() {
    return `${baseUrl}api/v1/carrier/trips`;
  },
  addTicketClass() {
    return `${baseUrl}api/v1/ticket-class`;
  },
  deleteTicketClass(id) {
    return `${baseUrl}api/v1/ticket-class/${id}`;
  },
  deleteTrip(id) {
    return `${baseUrl}api/v1/trip/${id}`;
  }
 }

const costDash = {
  getCosts(){
    return `${baseUrl}api/v1/admin/costs`;
  }
}

const possibleServices = {
  possibleServices(){
    return `${baseUrl}api/v1/possible-services`;
  }
}

const service = {
  services(){
    return `${baseUrl}api/v1/service`;
  },
  paginServices(){
    return `${baseUrl}api/v1/service/pagin`;
  },
  servicesByStatus(){
    return `${baseUrl}api/v1/carrier/service/by-status`;
  },
  servicesAmount(){
    return `${baseUrl}api/v1/carrier/carrier/service/amount`;
  },
  reviewService(id: number) {
    return `${baseUrl}api/v1/service/${id}/review`;
  },
  preloadServices() {
    return `${baseUrl}api/v1/service/preload`;
  }
}

const trip = {
  trips(){
    return `${baseUrl}api/v1/approver/trip`;
  },
  update(){
    return `${baseUrl}api/v1/trip`;
  }
}

const landing = {
  planets(){
    return `${baseUrl}api/v1/planets`;
  },
  spaceports(){
    return `${baseUrl}api/v1/spaceports`;
  },
  trips(){
    return `${baseUrl}api/v1/user/trips`;
  },
  bundles(){
    return `${baseUrl}api/v1/bundles/fresh`;
  }
}

const history = {
  getBoughtTickets(){
    return `${baseUrl}api/v1/history/user/ticket`;
  },
  getTicketService(id: number){
    return `${baseUrl}api/v1/history/ticket/${id}/service`;
  },
}

const user = {
  buyTicket(){
    return `${baseUrl}api/v1/user/bought-tickets`;
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
  service,
  possibleServices,
  trip,
  trips,
  landing,
  user,
  history
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
