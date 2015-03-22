package vanity.pagination

public class PaginationParams {

    private final Long max

    private final Long offset

    private final String sort

    private final Map<String, ?> queryParams

    PaginationParams(Long max, Long offset) {
        this(max, offset, null, null)
    }

    PaginationParams(Long max, Long offset, String sort) {
        this(max, offset, sort, null)
    }

    PaginationParams(Long max, Long offset, Map<String, ?> queryParams) {
        this(max, offset, null, queryParams)
    }

    PaginationParams(Long max, Long offset, String sort, Map<String, ?> queryParams) {
        this.max = max
        this.offset = offset
        this.sort = sort
        this.queryParams = queryParams
    }

    Long getMax() {
        return max ?: 20
    }

    Long getOffset() {
        return offset ?: 0
    }

    String getSort() {
        return sort
    }

    Map<String, ?> getQueryParams() {
        return queryParams
    }
}
