package shopping.interfaces.api

import org.springframework.data.domain.Page

class Paging {
    data class PagingRequest(
        val page: Int = 0,
        val size: Int = 10,
    )

    data class PageResponse<T>(
        val content: List<T>,
        val page: Int,
        val size: Int,
        val totalElements: Long,
        val totalPages: Int,
    ) {
        companion object {
            fun <T> from(page: Page<T>): PageResponse<T> =
                PageResponse(
                    content = page.content,
                    page = page.number,
                    size = page.size,
                    totalElements = page.totalElements,
                    totalPages = page.totalPages,
                )
        }
    }
}
