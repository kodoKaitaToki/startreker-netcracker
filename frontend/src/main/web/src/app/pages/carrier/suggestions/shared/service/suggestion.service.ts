import { Injectable } from '@angular/core';
import { Api } from 'src/app/modules/api';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { SuggestionDTO } from '../model/suggestionDTO';

@Injectable({
  providedIn: 'root'
})
export class SuggestionService {
  private defaultUrl: string = Api.baseUrl;
 
  private apiVersion: string = 'api/v1/';

  private page: string = 'suggestions';

  private url: string;

  constructor(private http: HttpClient) {
    this.url = this.defaultUrl + this.apiVersion + this.page;
  }

  public getAll(classId: number): Observable<any> {
    return this.http.get(this.url + '?class-id=' + classId);
  }

  public postSuggestion(suggestion: SuggestionDTO): Observable<any> {
    return this.http.post(this.url, suggestion);
  }

  public putSuggestion(suggestion: SuggestionDTO): Observable<any> {
    return this.http.put(this.url, suggestion)
  }

  public deleteSuggestion(suggestion: SuggestionDTO): Observable<any> {

    return this.http.request('DELETE',
                             this.url + `/${suggestion.id}`,
                             {
                               body: suggestion
                             });
  }
}
