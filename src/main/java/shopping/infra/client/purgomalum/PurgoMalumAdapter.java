package shopping.infra.client.purgomalum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class PurgoMalumAdapter {

    private final PurgoMalumClient client;

    /**
     * 주어진 텍스트가 부적절한 언어(profanity)를 포함하고 있는지 확인합니다.
     *
     * @param text 확인할 텍스트로, 부적절한 언어 포함 여부를 판별합니다.
     * @return 텍스트에 부적절한 언어가 포함되어 있으면 true를 반환하며, 그렇지 않으면 false를 반환합니다.
     */
    public boolean isProfanity(final String text) {
        final String response = client.get(PurgoMalumEndPoint.CONTAINS_PROFANITY.getEndpoint(), new ContainsProfanityRequest(text));

        return response.equals("true");
    }
}
