package vanity.pagination

class PaginationBean<T> {

    final List<T> elements

    final Integer totalCount

    PaginationBean(List<T> elements, Integer totalCount) {
        this.elements = elements
        this.totalCount = totalCount
    }

    boolean isEmpty() {
        return elements.isEmpty()
    }
}
