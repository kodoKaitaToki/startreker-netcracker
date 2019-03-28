import {TicketClass} from "../../../carrier/discounts/shared/model/ticket-class.model";

export function calcPriceWithDiscount(ticketClass: TicketClass): number {
  if (ticketClass.discount !== undefined && !ticketClass.discount.is_expired) {
    let price: number = ticketClass.ticket_price;
    let rate: number = ticketClass.discount.discount_rate;

    if (ticketClass.discount.is_percent)
      return price * (1 - rate / 100);
    else
      return price - rate;
  } else {
    return ticketClass.ticket_price;
  }
}
