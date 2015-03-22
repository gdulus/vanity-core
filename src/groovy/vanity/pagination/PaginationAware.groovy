package vanity.pagination

public interface PaginationAware<T> {

    PaginationBean<T> listWithPagination(PaginationParams paginationBean)

}