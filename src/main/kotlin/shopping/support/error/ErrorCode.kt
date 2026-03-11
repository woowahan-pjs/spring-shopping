package shopping.support.error

enum class ErrorCode {
    // 공통
    E500,
    E400,
    E401,
    E403,
    E404,

    // 회원 및 인증 (1000번대)
    E1000,
    E1001,
    E1002,

    // 상품 (2000번대)
    E2000,
    E2001,
    E2002,
    E2003,

    // 위시 리스트 (3000번대)
    E3000,
    E3001,

    // 외부 API 연동 (10000번대)
    E10000,
}
