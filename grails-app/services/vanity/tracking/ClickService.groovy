package vanity.tracking

import org.apache.commons.lang.Validate
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import vanity.article.Article
import vanity.article.Tag

class ClickService {

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(final Article article) {
        Validate.notNull(article, 'Provide not null article object')
        ArticleClick.withNewSession { new ArticleClick(article: article).save(flush: true) }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(final Tag tag) {
        Validate.notNull(tag, 'Provide not null tag object')
        TagClick.withNewSession { new TagClick(tag: tag).save(flush: true) }
    }

    @Transactional
    public Integer deleteAllByArticleIds(final Set<Long> ids) {
        if (!ids) {
            return 0
        }

        return ArticleClick.executeUpdate('''
            delete from
                ArticleClick
            where
                article.id in (:ids)
        ''',
            [
                ids: ids
            ])
    }

    @Transactional
    public Integer deleteAllByTagIds(final Set<Long> ids) {
        if (!ids) {
            return 0
        }

        return ArticleClick.executeUpdate('''
            delete from
                TagClick
            where
                tag.id in (:ids)
        ''',
            [
                ids: ids
            ])
    }
}
