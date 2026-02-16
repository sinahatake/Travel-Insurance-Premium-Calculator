package org.javaguru.travel.insurance.web.v2;

import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreResult;
import org.javaguru.travel.insurance.core.services.TravelCalculatePremiumService;
import org.javaguru.travel.insurance.dto.v2.DtoV2Converter;
import org.javaguru.travel.insurance.dto.v2.TravelCalculatePremiumRequestV2;
import org.javaguru.travel.insurance.dto.v2.TravelCalculatePremiumResponseV2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.Collections;

@Controller
public class TravelInsuranceControllerV2 {

    private final TravelCalculatePremiumService calculatePremiumService;
    private final DtoV2Converter dtoV2Converter;

    TravelInsuranceControllerV2(TravelCalculatePremiumService service,
                                DtoV2Converter dtoV2Converter) {
        this.calculatePremiumService = service;
        this.dtoV2Converter = dtoV2Converter;
    }

    @GetMapping("/insurance/travel/web/v2")
    public String showForm(ModelMap modelMap) {
        TravelCalculatePremiumRequestV2 request = new TravelCalculatePremiumRequestV2();

        request.setAgreementDateFrom(LocalDate.now().plusDays(1));
        request.setAgreementDateTo(LocalDate.now().plusDays(2));
        request.setCountry("SPAIN");
        request.setSelectedRisks(Collections.singletonList("TRAVEL_MEDICAL"));

        modelMap.addAttribute("request", request);
        modelMap.addAttribute("response", new TravelCalculatePremiumResponseV2());

        return "travel-calculate-premium-v2";
    }

    @PostMapping("/insurance/travel/web/v2")
    public String processForm(@ModelAttribute(value = "request") TravelCalculatePremiumRequestV2 request,
                              ModelMap modelMap) {
        TravelCalculatePremiumCoreCommand coreCommand = dtoV2Converter.buildCoreCommand(request);
        TravelCalculatePremiumCoreResult coreResult = calculatePremiumService.calculatePremium(coreCommand);
        TravelCalculatePremiumResponseV2 response = dtoV2Converter.buildResponse(coreResult);
        modelMap.addAttribute("response", response);
        return "travel-calculate-premium-v2";
    }

    @PostMapping("/insurance/travel/web/v2/clear")
    public String clearForm() {
        return "redirect:/insurance/travel/web/v2";
    }

}
