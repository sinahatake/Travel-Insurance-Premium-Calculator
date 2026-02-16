package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.util.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class DayCountCalculator {
    private final DateTimeUtil dateTimeUtil;

    BigDecimal calculate(AgreementDTO agreement) {
        var daysBetween = dateTimeUtil.getDaysBetween(agreement.getAgreementDateFrom(), agreement.getAgreementDateTo());
        return new BigDecimal(daysBetween);
    }
}
