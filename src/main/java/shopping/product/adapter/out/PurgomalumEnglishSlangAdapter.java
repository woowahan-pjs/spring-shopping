package shopping.product.adapter.out;

import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.product.port.out.EnglishSlangVerificationPort;

@Component
@RequiredArgsConstructor
public class PurgomalumEnglishSlangAdapter implements EnglishSlangVerificationPort {
    private final PurgomalumFeignClient purgomalumFeignClient;

    @Override
    public boolean containsSlang(String text) {
        try {
            return parseBody(purgomalumFeignClient.containsSlang(text));
        } catch (RetryableException exception) {
            throw new ApiException(ErrorCode.SLANG_API_CALL_FAILED);
        } catch (FeignException exception) {
            throw new ApiException(ErrorCode.SLANG_VERIFY_FAILED);
        }
    }

    private boolean parseBody(String body) {
        String normalizedBody = body.trim();
        if (isSlangResponse(normalizedBody)) {
            return true;
        }
        if (isCleanResponse(normalizedBody)) {
            return false;
        }
        throw new ApiException(ErrorCode.SLANG_API_INVALID_RESPONSE);
    }

    private boolean isSlangResponse(String normalizedBody) {
        return "true".equalsIgnoreCase(normalizedBody);
    }

    private boolean isCleanResponse(String normalizedBody) {
        return "false".equalsIgnoreCase(normalizedBody);
    }
}
