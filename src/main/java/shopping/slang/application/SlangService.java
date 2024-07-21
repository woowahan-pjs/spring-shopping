package shopping.slang.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.slang.domain.Slang;
import shopping.slang.domain.SlangRepository;
import shopping.slang.domain.Slangs;
import shopping.slang.dto.SlangRequest;
import shopping.slang.dto.SlangResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SlangService {

    @Value("${purgo.malum.url}")
    private String purgoMalumUrl;

    private SlangRepository slangRepository;
    private PurgoMalumFeign purgoMalumFeign;

    public SlangService(SlangRepository slangRepository, PurgoMalumFeign purgoMalumFeign){
        this.slangRepository = slangRepository;
        this.purgoMalumFeign = purgoMalumFeign;
    }

    @Transactional
    public SlangResponse registerSlangs(SlangRequest request) {
        Slangs psersistSlangs = findAllSlangs();
        Slangs slangs = request.toSlangs(psersistSlangs);
        slangRepository.saveAll(slangs.getSlangs());

        return SlangResponse.toSlangResponse(slangs);
    }

    public boolean hasSlang(String value) {
        if(checkForPurgoMalum(value)) {
            return true;
        }

        if(checkForCustomSlang(value)) {
            return true;
        }

        return false;
    }

    private boolean checkForCustomSlang(String value) {
        Slangs psersistSlangs = findAllSlangs();
        boolean hasCustomSlang = psersistSlangs.hasAnySlang(value);
        return hasCustomSlang;
    }

    private boolean checkForPurgoMalum(String value) {
        String result = purgoMalumFeign.containsSlang(value);
        boolean containSlang = Boolean.parseBoolean(result);
        return containSlang;
    }


    private Slangs findAllSlangs() {
        List<Slang> slangs = slangRepository.findAll();
        return new Slangs(slangs);
    }
}
