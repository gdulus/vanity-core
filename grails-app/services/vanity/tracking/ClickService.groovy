package vanity.tracking

import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Transactional
import vanity.article.Article
import vanity.article.Tag

class ClickService {

    @Transactional
    public void createForArticle(final Long id) {
        Validate.notNull(id, 'Provide not null article id')
        Article article = Article.load(id)
        new ArticleClick(article: article).save(flush: true)
    }

    @Transactional
    public void createForTag(final Long id) {
        Validate.notNull(id, 'Provide not null tag id')
        Tag tag = Tag.load(id)
        new TagClick(tag: tag).save(flush: true)
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
