package edu.netcracker.backend.message.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
class GeneralPendingDto {

    private String carrierName;

    private String carrierEmail;

    private String carrierTel;

    private String approverName;

    private String approverEmail;

    private String approverTel;
}
