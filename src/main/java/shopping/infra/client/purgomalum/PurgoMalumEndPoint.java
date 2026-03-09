package shopping.infra.client.purgomalum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
enum PurgoMalumEndPoint {
    CONTAINS_PROFANITY("/service/containsprofanity");

    private final String endpoint;
}
