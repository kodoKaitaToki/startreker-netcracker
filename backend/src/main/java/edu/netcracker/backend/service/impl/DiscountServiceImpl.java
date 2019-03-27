package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.message.request.DiscountDTO;
import edu.netcracker.backend.model.Discount;
import edu.netcracker.backend.service.DiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DiscountServiceImpl implements DiscountService {

    private static final String DATE_PATTERN = "dd-MM-yyyy";

    private final DiscountDAO discountDAO;

    @Autowired
    public DiscountServiceImpl(DiscountDAO discountDAO) {
        this.discountDAO = discountDAO;
    }

    @Override
    public List<DiscountDTO> getDiscountDTOs(List<Long> entityIds) {
        log.debug("DiscountServiceImpl.getDiscountDTOs(List<Long> entityIds) was invoked");

        List<Discount> discounts = discountDAO.findIn(entityIds);
        checkingDiscountsForOverdue(discounts);
        return discounts.stream()
                        .map(discount -> DiscountDTO.toDiscountDTO(discount, DATE_PATTERN))
                        .collect(Collectors.toList());
    }

    @Override
    public DiscountDTO saveDiscount(DiscountDTO discountDTO) {
        log.debug("DiscountServiceImpl.saveDiscount(DiscountDTO discountDTO) was invoked");
        if (discountDTO == null) {
            log.error("discountDTO is null");
            throw new RequestException("Discount is null", HttpStatus.BAD_REQUEST);
        }

        Discount discount = getDiscount(discountDTO);
        checkingDiscountForOverdue(discount);
        discountDAO.save(discount);

        log.debug("discount with od {} is saved", discount.getDiscountId());

        return DiscountDTO.toDiscountDTO(discount, DATE_PATTERN);
    }

    @Override
    public DiscountDTO deleteDiscount(Number discountId) {
        log.debug("DiscountServiceImpl.deleteDiscount(Number discountId) was invoked");
        Optional<Discount> optionalDiscount = discountDAO.find(discountId);

        if (!optionalDiscount.isPresent()) {
            log.error("discount with id {} not found", discountId);
            throw new RequestException("No such discount", HttpStatus.NOT_FOUND);
        }

        discountDAO.delete(discountId);

        log.debug("discount with od {} is deleted", discountId);

        Discount discount = optionalDiscount.get();
        checkingDiscountForOverdue(discount);

        return DiscountDTO.toDiscountDTO(discount, DATE_PATTERN);
    }

    @Override
    public DiscountDTO getRelatedDiscountDTO(Long discountId, List<DiscountDTO> discountDTOs) {
        log.debug(
                "DiscountServiceImpl.getRelatedDiscountDTO(Long discountId, List<DiscountDTO> discountDTOs) was invoked");
        if (discountId == null) {
            return null;
        }

        for (DiscountDTO discountDTO : discountDTOs) {
            if (discountId.equals(discountDTO.getDiscountId())) {
                return discountDTO;
            }
        }
        return null;
    }

    private void checkingDiscountsForOverdue(List<Discount> discounts) {
        for (Discount discount : discounts) {
            checkingDiscountForOverdue(discount);
        }
    }

    private void checkingDiscountForOverdue(Discount discount) {
        discount.setExpired(isOverdueDiscount(discount.getFinishDate()));
    }

    private boolean isOverdueDiscount(LocalDateTime finishDate) {
        return finishDate.isBefore(LocalDateTime.now());
    }

    private Discount getDiscount(DiscountDTO discountDTO) {
        log.debug(
                "DiscountServiceImpl.getDiscount(DiscountDTO discountDTO) was invoked");
        Discount discount = Discount.toDiscount(discountDTO);

        if (discount.getStartDate()
                    .isBefore(LocalDateTime.now())) {
            log.error("Start date of discount is less than current date");
            throw new RequestException("Start date of discount is less than current date", HttpStatus.BAD_REQUEST);
        }

        if (discount.getFinishDate()
                    .isBefore(LocalDateTime.now())) {
            log.error("Finish date of discount is less than current date");
            throw new RequestException("Finish date of discount is less than current date", HttpStatus.BAD_REQUEST);
        }

        if (discount.getFinishDate()
                    .isBefore(discount.getStartDate())) {
            log.error("Finish date of discount is less than start date of discount");
            throw new RequestException("Finish date of discount is less than start date of discount",
                                       HttpStatus.BAD_REQUEST);
        }

        if (discount.getDiscountRate() <= 0) {
            log.error("Discount rate must be greater than 0. Found {}", discount.getDiscountRate());
            throw new RequestException("Discount rate must be greater than 0", HttpStatus.BAD_REQUEST);
        }

        if (discount.getIsPercent() && discount.getDiscountRate() > 100) {
            log.error("Discount rate in percents must be less than 100. Found {}", discount.getDiscountRate());
            throw new RequestException("Discount rate in percents must be less than 100", HttpStatus.BAD_REQUEST);
        }

        return discount;
    }
}
