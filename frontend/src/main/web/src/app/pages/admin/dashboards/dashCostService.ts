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

    public getCosts(from: string, to: string){
        let url = Api.costDash.getCosts() + '?' + 'from=' + from + '&to=' + to;
        return this.http.get(url, HttpOptionsAuthorized);
    }

    public getCarCosts(id: number, from: string, to: string){
        let url = Api.costDash.getCosts() + '/' + id + '?' + 'from=' + from + '&to=' + to;
        return this.http.get(url, HttpOptionsAuthorized);
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
