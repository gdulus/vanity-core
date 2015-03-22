package vanity.celebrity

import org.springframework.transaction.annotation.Transactional
import vanity.article.Tag
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean
import vanity.pagination.PaginationParams

class CelebrityService implements PaginationAware<Celebrity> {

    @Transactional(readOnly = true)
    public Celebrity read(final Long id) {
        return Celebrity.read(id)
    }

    @Transactional(readOnly = true)
    PaginationBean<Celebrity> listWithPagination(final PaginationParams params) {
        return new PaginationBean<Celebrity>(Celebrity.list(max: params.max, offset: params.offset, sort: params.sort), Celebrity.count())
    }

    @Transactional(readOnly = true)
    Celebrity findByTag(final Tag tag) {
        return Celebrity.findByTag(tag)
    }

}
