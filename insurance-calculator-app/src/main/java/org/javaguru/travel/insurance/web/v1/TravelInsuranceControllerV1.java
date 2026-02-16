package org.javaguru.travel.insurance.web.v1;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreResult;
import org.javaguru.travel.insurance.core.services.TravelCalculatePremiumService;
import org.javaguru.travel.insurance.dto.v1.DtoV1Converter;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumResponseV1;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.Collections;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TravelInsuranceControllerV1 {

    private final TravelCalculatePremiumService service;
    private final DtoV1Converter dtoV1Converter;

    @GetMapping("/insurance/travel/web/v1")
    public String showForm(ModelMap modelMap) {
        TravelCalculatePremiumRequestV1 request = new TravelCalculatePremiumRequestV1();

            // Даты
            request.setAgreementDateFrom(LocalDate.now().plusDays(1));
            request.setAgreementDateTo(LocalDate.now().plusDays(2));
            request.setPersonBirthDate(LocalDate.of(1990, 1, 1));

            // Страна
            request.setCountry("SPAIN");

            // Данные путешественника
            request.setPersonCode("010882-11034");
            request.setPersonFirstName("John");
            request.setPersonLastName("Doe");
            request.setMedicalRiskLimitLevel("LEVEL_10000");

            // Выбранные риски
            request.setSelectedRisks(Collections.singletonList("TRAVEL_MEDICAL"));

        modelMap.addAttribute("request", request);
        modelMap.addAttribute("response", new TravelCalculatePremiumResponseV1());
        return "travel-calculate-premium-v1";
    }

    @PostMapping("/insurance/travel/web/v1")
    public String processForm(@ModelAttribute(value = "request") TravelCalculatePremiumRequestV1 request,
                              ModelMap modelMap) {
        TravelCalculatePremiumCoreCommand command = dtoV1Converter.buildCoreCommand(request);
        TravelCalculatePremiumCoreResult result = service.calculatePremium(command);
        TravelCalculatePremiumResponseV1 response = dtoV1Converter.buildResponse(result);
        modelMap.addAttribute("response", response);
        return "travel-calculate-premium-v1";
    }

    @PostMapping("/insurance/travel/web/v1/clear")
    public String clearForm() {
        return "redirect:/insurance/travel/web/v1";
    }


}