package shopping.slang.application;

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

    private SlangRepository slangRepository;

    public SlangService(SlangRepository slangRepository){
        this.slangRepository = slangRepository;
    }

    @Transactional
    public SlangResponse registerSlangs(SlangRequest request) {
        Slangs psersistSlangs = findAllSlangs();
        Slangs slangs = request.toSlangs(psersistSlangs);
        slangRepository.saveAll(slangs.getSlangs());

        return SlangResponse.toSlangResponse(slangs);
    }

    private Slangs findAllSlangs() {
        List<Slang> slangs = slangRepository.findAll();
        return new Slangs(slangs);
    }
}
