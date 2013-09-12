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

    @Transactional
    public Integer deleteAllByArticleIds(final Set<Long> articleIds){
        if (!articleIds){
            return 0
        }

        return ArticleClick.executeUpdate('''
            delete from
                ArticleClick
            where
                article.id in (:articleIds)
        ''',
        [
            articleIds:articleIds
        ])
    }
}
