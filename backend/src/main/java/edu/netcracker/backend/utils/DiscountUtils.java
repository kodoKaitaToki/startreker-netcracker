package edu.netcracker.backend.utils;

import edu.netcracker.backend.model.Discount;

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
}
