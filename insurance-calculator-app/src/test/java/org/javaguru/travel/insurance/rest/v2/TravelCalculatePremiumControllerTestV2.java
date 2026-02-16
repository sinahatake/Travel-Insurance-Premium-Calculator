package org.javaguru.travel.insurance.rest.v2;

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
public class TravelCalculatePremiumControllerTestV2 {
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
        executeAndCompare("rest/v2/request/correct_request_v2.json",
                "rest/v2/response/correct_request_v2.json");
    }

    @Test
    public void correctRequestWith_one_person_single_risk() throws Exception {
        executeAndCompare("rest/v2/request/one_person_single_risk.json",
                "rest/v2/response/one_person_single_risk.json");
    }

    @Test
    public void error_9_selected_risk_not_supported_one_risk() throws Exception {
        executeAndCompare(
                "rest/v2/request/error_9_selected_risk_not_supported_one_risk.json",
                "rest/v2/response/error_9_selected_risk_not_supported_one_risk.json"
        );
    }

    @Test
    public void error_9_selected_risk_not_supported_two_risks() throws Exception {
        executeAndCompare(
                "rest/v2/request/error_9_selected_risk_not_supported_two_risks.json",
                "rest/v2/response/error_9_selected_risk_not_supported_two_risks.json"
        );
    }

    @Test
    public void error_10_country_empty_TRAVEL_MEDICAL() throws Exception {
        executeAndCompare(
                "rest/v2/request/error_10_country_empty_TRAVEL_MEDICAL.json",
                "rest/v2/response/error_10_country_empty_TRAVEL_MEDICAL.json"
        );
    }

    @Test
    public void error_10_country_null_TRAVEL_MEDICAL() throws Exception {
        executeAndCompare(
                "rest/v2/request/error_10_country_null_TRAVEL_MEDICAL.json",
                "rest/v2/response/error_10_country_null_TRAVEL_MEDICAL.json"
        );
    }

    @Test
    public void error_11_country_not_supported_TRAVEL_MEDICAL() throws Exception {
        executeAndCompare(
                "rest/v2/request/error_11_country_not_supported_TRAVEL_MEDICAL.json",
                "rest/v2/response/error_11_country_not_supported_TRAVEL_MEDICAL.json"
        );
    }

    @Test
    public void error_15_medical_risk_limit_level_not_supported() throws Exception {
        executeAndCompare(
                "rest/v2/request/error_15_medical_risk_limit_level_not_supported.json",
                "rest/v2/response/error_15_medical_risk_limit_level_not_supported.json"
        );
    }


    private void executeAndCompare(String jsonRequestFilePath,
                                   String jsonResponseFilePath) throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile(jsonRequestFilePath);
        String jsonResponseFromFile = jsonFileReader.readJsonFromFile(jsonResponseFilePath);

        MvcResult result = mockMvc.perform(post("/insurance/travel/api/v2/")
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