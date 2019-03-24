import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { Api } from '../../../../modules/api/index';
import { Observable } from 'rxjs';

@Injectable ({
    providedIn: 'root'
})
export class LandingService {

    constructor(private http: HttpClient){}

    public getPlanets(){
        return this.http.get(Api.landing.planets(), Api.HttpOptions);
    }

    public getSpaceports(planetId){
        let params = new HttpParams().set("planetId", planetId);
        return this.http.get(Api.landing.spaceports(), {params: params});
    }

    public getBundles(): Observable<any>{
        return this.http.get(Api.landing.bundles());
    }
}