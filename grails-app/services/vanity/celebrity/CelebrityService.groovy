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
        if (!params.queryParams?.query) {
            return new PaginationBean<Celebrity>(Celebrity.list(max: params.max, offset: params.offset, sort: params.sort), Celebrity.count())
        }

        String query = params.queryParams.query
        String likeStatement = "%${query.toLowerCase()}%"

        List<Celebrity> celebrities = Celebrity.executeQuery("""
                from
                    Celebrity
                where
                    lower(firstName) like :query
                    or lower(lastName) like :query
                order by
                    :sort
            """,
                [
                        query : likeStatement,
                        max   : params.max,
                        offset: params.offset ?: 0,
                        sort  : params.sort
                ]
        )

        int count = Celebrity.executeQuery("""
                select
                    count(*)
                from
                    Celebrity
                where
                   lower(firstName) like :query
                   or lower(lastName) like :query
            """,
                [
                        query: likeStatement,
                ]
        )[0]

        return new PaginationBean<Celebrity>(celebrities, count)
    }

    @Transactional(readOnly = true)
    Celebrity findByTag(final Tag tag) {
        return Celebrity.findByTag(tag)
    }

    @Transactional(readOnly = true)
    List<Celebrity> findByLastNameLike(final String query, final int max) {
        return Celebrity.findAllByLastNameIlike("${query}%", [max: max, sort: 'firstName'])
    }

    @Transactional(readOnly = true)
    List<Celebrity> findLastUpdated(final int max) {
        return Celebrity.list(sort: 'lastUpdated', order: 'desc', max: max)
    }

}
