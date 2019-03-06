export class Service {
  id: number;

  approver_name: string;

  service_name: string;

  service_descr: string;

  service_status: number;

  creation_date: Date;

  reply_text: string;

  constructor(
    id,
    approver_name,
    service_name,
    service_descr,
    service_status,
    creation_date,
    reply_text
  ) {
    this.id = id;
    this.approver_name = approver_name;
    this.service_name = service_name;
    this.service_descr = service_descr;
    this.service_status = service_status;
    this.creation_date = creation_date;
    this.reply_text = reply_text;
  }
}
