package vanity.celebrity

import org.springframework.transaction.annotation.Transactional
import vanity.article.Tag
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean

class CelebrityService implements PaginationAware<Celebrity> {

    @Transactional(readOnly = true)
    public Celebrity read(final Long id) {
        return Celebrity.read(id)
    }

    @Transactional(readOnly = true)
    PaginationBean<Celebrity> listWithPagination(Long max, Long offset, String sort, final String query) {
        return new PaginationBean<Celebrity>(Celebrity.list(max: max, offset: offset, sort: sort), Celebrity.count())
    }

    @Transactional(readOnly = true)
    Celebrity findByTag(final Tag tag) {
        return Celebrity.findByTag(tag)
    }

    @Transactional(readOnly = true)
    List<Celebrity> findLastUpdated(final int max) {
        return Celebrity.list(sort: 'lastUpdated', order: 'desc', max: max)
    }

    @Transactional(readOnly = true)
    List<Celebrity> findByLastNameLike(final String query, final int max) {
        return Celebrity.findAllByLastNameIlike("${query}%", [max: max, sort: 'firstName'])
    }

    @Transactional(readOnly = true)
    Integer countByLastNameLike(final String query) {
        return Celebrity.countByLastNameIlike("${query}%")
    }
}
