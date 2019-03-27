import { Injectable } from '@angular/core';
import { HttpParams, HttpClient } from '@angular/common/http';
import { Api, HttpOptionsAuthorized } from 'src/app/modules/api';
import { PossibleService } from '../../../suggestions/shared/model/possible-service';

@Injectable({
  providedIn: 'root'
})
export class PossibleServiceService {

  constructor(private http: HttpClient) { }

  public getPossibleServices(class_id: number){
    let params = new HttpParams().set('class-id', class_id.toString());
    return this.http.get<any>(Api.possibleServices.possibleServices(), {params: params,
                                                      headers: HttpOptionsAuthorized.headers,
                                                      observe: HttpOptionsAuthorized.observe});
  }

  public createPossibleService(possibleService: PossibleService){
    return this.http.post(Api.possibleServices.possibleServices(), possibleService, HttpOptionsAuthorized);
  }

  public updatePossibleService(possibleService: PossibleService){
    return this.http.put(Api.possibleServices.possibleServices(), possibleService, HttpOptionsAuthorized);
  }

  public deletePossibleService(id: number){
    return this.http.delete(Api.possibleServices.possibleServices() + "/" + id, HttpOptionsAuthorized);
  }
}
