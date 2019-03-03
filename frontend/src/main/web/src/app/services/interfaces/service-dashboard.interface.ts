export interface Service {
    service_id: number;
    service_name: string;
    occurence_count: number;
    percentage: number;
}

export interface ServiceList {
    services: Service[];
}

