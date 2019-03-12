package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.DiscountDTO;

import java.util.List;

public interface DiscountService {

    List<DiscountDTO> getDiscountDTOs(List<Long> entityIds);

    DiscountDTO saveDiscount(DiscountDTO discountDTO);

    DiscountDTO deleteDiscount(Number discountId);

    DiscountDTO getRelatedDiscountDTO(Long discountId, List<DiscountDTO> discountDTOs);
}
