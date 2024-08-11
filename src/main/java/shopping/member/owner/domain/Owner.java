package shopping.member.owner.domain;

import jakarta.persistence.Entity;
import shopping.member.common.domain.Member;
import shopping.member.common.domain.MemberRole;
import shopping.member.common.domain.Password;

@Entity
public class Owner extends Member {

    protected Owner() {
    }

    public Owner(final String email, final Password password, final String memberName) {
        super(email, password, memberName, MemberRole.OWNER);
    }
}
