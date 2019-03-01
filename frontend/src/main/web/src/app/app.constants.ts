import { Injectable } from '@angular/core';

@Injectable()
export class Configuration {
    public Server = 'http://startreker-netcracker.herokuapp.com/';
    public ApiUrl = 'api/';
    public ServerWithApiUrl = this.Server + this.ApiUrl;
}
