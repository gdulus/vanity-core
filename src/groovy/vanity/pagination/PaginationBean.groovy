package vanity.pagination

class PaginationBean<T> {

    final List<T> elements

    final int totalCount

    PaginationBean(List<T> elements, int totalCount) {
        this.elements = elements
        this.totalCount = totalCount
    }

    boolean isEmpty(){
        !elements || elements.isEmpty()
    }
}
