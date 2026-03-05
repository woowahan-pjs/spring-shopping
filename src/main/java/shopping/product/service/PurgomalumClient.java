package shopping.product.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.springframework.stereotype.Component;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@Component
public class PurgomalumClient {
    private static final String BASE_URL =
            "https://www.purgomalum.com/service/containsprofanity?text=";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public boolean containsProfanity(String text) {
        HttpResponse<String> response = execute(text);
        if (isSuccess(response.statusCode())) {
            return parseBody(response.body());
        }
        throw new ApiException(ErrorCode.PROFANITY_VERIFY_FAILED);
    }

    private HttpResponse<String> execute(String text) {
        HttpRequest request = createRequest(text);
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException exception) {
            throw new ApiException(ErrorCode.PROFANITY_API_CALL_FAILED);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ApiException(ErrorCode.PROFANITY_API_INTERRUPTED);
        }
    }

    private HttpRequest createRequest(String text) {
        URI uri = URI.create(BASE_URL + URLEncoder.encode(text, StandardCharsets.UTF_8));
        return HttpRequest.newBuilder(uri).GET().timeout(Duration.ofSeconds(3)).build();
    }

    private boolean parseBody(String body) {
        String normalized = body.trim();
        if ("true".equalsIgnoreCase(normalized)) {
            return true;
        }
        if ("false".equalsIgnoreCase(normalized)) {
            return false;
        }
        throw new ApiException(ErrorCode.PROFANITY_API_INVALID_RESPONSE);
    }

    private boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }
}
