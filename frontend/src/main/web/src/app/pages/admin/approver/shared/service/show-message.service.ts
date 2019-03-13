import {Injectable} from "@angular/core";
import {MessageService} from "primeng/api";

@Injectable()
export class ShowMessageService {

  showMessage(msgSrvc: MessageService, severity: string, summary: string, detail: string) {
    msgSrvc.add({
      severity: severity,
      summary: summary,
      detail: detail
    });
  }
}
