package edu.netcracker.backend.utils;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.request.DiscountDTO;
import edu.netcracker.backend.message.request.DiscountSuggestionDTO;
import edu.netcracker.backend.message.request.DiscountTicketClassDTO;
import edu.netcracker.backend.model.Discount;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class DiscountUtils {

    public static boolean isOverdueDiscount(Discount relatedDiscount) {
        return relatedDiscount.getFinishDate().isBefore(LocalDateTime.now());
    }

    public static  Discount findDiscount(Long discountId, List<Discount> discounts) {
        for (Discount discount: discounts) {
            if (discount.getDiscountId().equals(discountId)) {
                return discount;
            }
        }
        return null;
    }

    public static Discount getDiscount(DiscountDTO discountDTO) {
        Discount discount = Discount.toDiscount(discountDTO);

        if (discount.getFinishDate().isBefore(discount.getStartDate())) {
            throw new RequestException("Finish date greater than start date", HttpStatus.BAD_REQUEST);
        }

        if (discount.getDiscountRate() <= 0) {
            throw new RequestException("Discount rate must be greater than 0", HttpStatus.BAD_REQUEST);
        }

        if (discount.getIsPercent() && discount.getDiscountRate() > 100) {
            throw new RequestException("Discount rate in percents must be less than 100", HttpStatus.BAD_REQUEST);
        }

        return discount;
    }
}
