package org.javaguru.travel.insurance.core.blacklist;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.blacklist.dto.BlackListRequest;
import org.javaguru.travel.insurance.core.blacklist.dto.BlackListResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Profile({"mysql-container", "mysql-local"})
class BlackListedPersonServiceImpl implements BlackListedPersonService {
    private final WebClient webClient;

    @Override
    public Boolean isBlacklisted(PersonDTO person) {
        BlackListRequest request = new BlackListRequest(person.getPersonCode(), person.getPersonFirstName(), person.getPersonLastName());
        BlackListResponse response = webClient.post()
                .uri("/check/")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BlackListResponse.class)
                .block();
        assert response != null;
        return response.getBlacklisted();
    }

}
