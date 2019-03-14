import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Api } from '../../../modules/api/index';
import { HttpOptionsAuthorized } from '../../../modules/api/index';

@Injectable(
    {providedIn: 'root'}
)
export class DashCostService {

    constructor(private http: HttpClient) {
    }

    public getCosts<T>(from: string, to: string): Observable<T>{
        let url = Api.costDash.getCosts() + '?' + 'from=' + from + '&to=' + to;
        return this.http.get<any>(url, HttpOptionsAuthorized);
    }

    public getCarCosts<T>(id: number, from: string, to: string): Observable<T>{
        let url = Api.costDash.getCosts() + '/' + id + '?' + 'from=' + from + '&to=' + to;
        console.log(url);
        return this.http.get<T>(url, HttpOptionsAuthorized);
    }

    public parseResponse(response){
        let answer = [];

        for(let key in response){

			answer.push({
				y: parseInt(key)*response[key],
				label: key + '$ tickets'
			});

        }

        return answer;
    }
}
