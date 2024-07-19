package shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shopping.constant.enums.YesNo;
import shopping.member.application.MemberService;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
