export class Servicehistorymodel {
  bought_services_name: string;
  bought_services_count: number;

  constructor(bought_services_name: string, bought_services_count: number) {
    this.bought_services_name = bought_services_name;
    this.bought_services_count = bought_services_count;
  }
}
