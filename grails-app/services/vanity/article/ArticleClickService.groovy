package vanity.article

import org.apache.commons.lang.Validate
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class ArticleClickService {

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createAsync(Article article) {
        Validate.notNull(article, 'Provide not null article object')
        ArticleClick.withNewSession { new ArticleClick(article: article).save(flush: true) }
    }
}
