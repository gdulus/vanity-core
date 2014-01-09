package vanity.celebrity

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.transaction.annotation.Transactional
import vanity.article.Tag
import vanity.pagination.PaginationBean

class CelebrityService {

    @Transactional(readOnly = true)
    PaginationBean<Celebrity> listWithPagination(final GrailsParameterMap params) {
        return new PaginationBean<Celebrity>(Celebrity.list(params), Celebrity.count())
    }

    @Transactional(readOnly = true)
    Celebrity findByTag(final Tag tag) {
        return Celebrity.findByTag(tag)
    }

}
