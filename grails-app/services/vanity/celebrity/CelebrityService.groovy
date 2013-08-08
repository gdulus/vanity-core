package vanity.celebrity

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import vanity.article.Tag
import vanity.pagination.PaginationBean

class CelebrityService {

    GrailsApplication grailsApplication

    @Transactional(readOnly = true)
    PaginationBean<Celebrity> listWithPagination(final GrailsParameterMap params) {
        return new PaginationBean<Celebrity>(Celebrity.list(params), Celebrity.count())
    }

    @Transactional
    boolean save(final Celebrity celebrity, final MultipartFile image){
        if (!celebrity || !celebrity.save()){
            return Boolean.FALSE
        }

        //String fileName = "celebrity_${celebrity.ident()}"
        //String resultDir = grailsApplication.config.images.celebrity.resultDir

    }

    @Transactional(readOnly = true)
    Celebrity get(final Long id) {
        return Celebrity.get(id)
    }

    @Transactional(readOnly = true)
    Celebrity findByTag(final Tag tag) {
        return Celebrity.findByTag(tag)
    }

}
