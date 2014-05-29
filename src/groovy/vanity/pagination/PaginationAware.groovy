package vanity.pagination

public interface PaginationAware<T> {

    PaginationBean<T> listWithPagination(final Long max, final Long offset, final String sort, final String query)

}