package org.javaguru.travel.insurance.rest.v1;

import org.javaguru.travel.insurance.core.blacklist.BlackListedPersonService;
import org.javaguru.travel.insurance.core.messagebroker.proposal.ProposalGeneratorQueueSender;
import org.javaguru.travel.insurance.rest.common.JsonFileReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TravelCalculatePremiumControllerTestV1 {
    @MockitoBean
    private BlackListedPersonService blackListedPersonService;

    @MockitoBean
    private ProposalGeneratorQueueSender sender;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonFileReader jsonFileReader;

    @Test
    public void correctRequest() throws Exception {
        executeAndCompare("rest/v1/request/correct_request.json",
                "rest/v1/response/correct_request.json");
    }

    @Test
    public void shouldFail_whenStartDateInPast() throws Exception {
        executeAndCompare("rest/v1/request/error_1_start_date_in_past.json",
                "rest/v1/response/error_1_start_date_in_past.json");
    }

    @Test
    public void shouldFail_whenDateFromIsNull() throws Exception {
        executeAndCompare("rest/v1/request/error_2_date_from_null.json",
                "rest/v1/response/error_2_date_from_null.json");
    }

    @Test
    public void shouldFail_whenEndDateInPast() throws Exception {
        executeAndCompare("rest/v1/request/error_3_end_date_in_past.json",
                "rest/v1/response/error_3_end_date_in_past.json");
    }

    @Test
    public void shouldFail_whenDateToIsNull() throws Exception {
        executeAndCompare("rest/v1/request/error_4_date_to_null.json",
                "rest/v1/response/error_4_date_to_null.json");
    }

    @Test
    public void shouldFail_whenStartAfterEnd() throws Exception {
        executeAndCompare("rest/v1/request/error_5_start_after_end.json",
                "rest/v1/response/error_5_start_after_end.json");
    }

    @Test
    public void shouldFail_whenSelectedRisksIsEmpty() throws Exception {
        executeAndCompare("rest/v1/request/error_6_selected_risks_empty.json",
                "rest/v1/response/error_6_selected_risks_empty.json");
    }

    @Test
    public void shouldFail_whenFirstNameIsEmpty() throws Exception {
        executeAndCompare("rest/v1/request/error_7_first_name_empty.json",
                "rest/v1/response/error_7_first_name_empty.json");
    }

    @Test
    public void shouldFail_whenLastNameIsEmpty() throws Exception {
        executeAndCompare("rest/v1/request/error_8_last_name_empty.json",
                "rest/v1/response/error_8_last_name_empty.json");
    }

    @Test
    public void shouldFail_whenUnknownRiskTypeProvided() throws Exception {
        executeAndCompare("rest/v1/request/error_9_unknown_risk.json",
                "rest/v1/response/error_9_unknown_risk.json");
    }

    @Test
    public void shouldFail_whenTravelMedicalSelectedAndCountryEmpty() throws Exception {
        executeAndCompare("rest/v1/request/error_10_travel_medical_country_required.json",
                "rest/v1/response/error_10_travel_medical_country_required.json");
    }

    @Test
    public void shouldFail_whenCountryNotInClassifier() throws Exception {
        executeAndCompare("rest/v1/request/error_11_country_not_in_classifier.json",
                "rest/v1/response/error_11_country_not_in_classifier.json");
    }

    @Test
    public void shouldFail_when_medicalRiskLimitLevelMissing() throws Exception {
        executeAndCompare("rest/v1/request/error_14_medicalRiskLimitLevel_missing.json",
                "rest/v1/response/error_14_medicalRiskLimitLevel_missing.json");
    }

    @Test
    public void shouldFail_when_medicalRiskLimitLevelinvalid() throws Exception {
        executeAndCompare("rest/v1/request/error_15_ medicalRiskLimitLevel_invalid.json",
                "rest/v1/response/error_15_ medicalRiskLimitLevel_invalid.json");
    }


    private void executeAndCompare(String jsonRequestFilePath,
                                   String jsonResponseFilePath) throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile(jsonRequestFilePath);
        String jsonResponseFromFile = jsonFileReader.readJsonFromFile(jsonResponseFilePath);

        MvcResult result = mockMvc.perform(post("/insurance/travel/api/v1/")
                        .content(jsonRequest)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        jsonResponse = jsonResponse.replaceAll("\"uuid\"\\s*:\\s*\"[^\"]+\"", "\"uuid\":\"IGNORED\"");
        jsonResponseFromFile = jsonResponseFromFile.replaceAll("\"uuid\"\\s*:\\s*\"[^\"]+\"", "\"uuid\":\"IGNORED\"");


        assertJson(jsonResponseFromFile)
                .where()
                .keysInAnyOrder()
                .arrayInAnyOrder()
                .isEqualTo(jsonResponse);
    }
}