export class ViewsModel {
  views: number;
  from: Date;
  to: Date;

  constructor(views: number, from: Date, to: Date) {
    this.views = views;
    this.from = from;
    this.to = to;
  }
}
