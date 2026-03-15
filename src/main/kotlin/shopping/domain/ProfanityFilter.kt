package shopping.domain

interface ProfanityFilter {
    /**
     * 텍스트에 비속어가 포함되어 있는지 검사합니다.
     * @param text 검사할 텍스트
     * @return 비속어 포함 여부 (true: 포함됨, false: 깨끗함)
     */
    fun containsProfanity(text: String): Boolean
}
