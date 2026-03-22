package shopping.component;

public class MemberContext {

	private static final ThreadLocal<Long> memberIdHolder = new ThreadLocal<>();

	public static void setMemberId(Long memberId) {
		memberIdHolder.set(memberId);
	}

	public static Long getMemberId() {
		return memberIdHolder.get();
	}

	public static void clear() {
		memberIdHolder.remove();
	}
}