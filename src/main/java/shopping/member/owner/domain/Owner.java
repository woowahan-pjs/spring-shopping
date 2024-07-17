package shopping.member.owner.domain;

import jakarta.persistence.Entity;
import shopping.member.common.domain.Member;
import shopping.member.common.domain.MemberPriority;
import shopping.member.common.domain.Password;
import shopping.member.common.domain.PasswordEncoder;

@Entity
public class Owner extends Member {

    protected Owner() {
    }

    public Owner(String email, String rawPassword, PasswordEncoder encoder, String memberName){
        super(email, new Password(rawPassword, encoder), memberName, MemberPriority.OWNER);
    }
}
