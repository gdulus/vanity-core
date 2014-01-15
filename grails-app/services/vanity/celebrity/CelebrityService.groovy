package vanity.celebrity

import org.springframework.transaction.annotation.Transactional
import vanity.article.Tag
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean

class CelebrityService implements PaginationAware<Celebrity> {

    @Transactional(readOnly = true)
    PaginationBean<Celebrity> listWithPagination(Long max, Long offset, String sort) {
        return new PaginationBean<Celebrity>(Celebrity.list(max: max, offset: offset, sort: sort), Celebrity.count())
    }

    @Transactional(readOnly = true)
    Celebrity findByTag(final Tag tag) {
        return Celebrity.findByTag(tag)
    }

}
