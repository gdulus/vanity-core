package vanity.article

import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Transactional

class ArticleService {

    TagService tagService

    @Transactional
    void updateRank(final Long articleId, final Integer rank){
        Article article = Article.get(articleId)

        if (article){
            article.rank += rank
            article.save()
        }
    }

    @Transactional
    Article create(final Set<String> stringTags, final ContentSource.Target contentSourceTarget, final Closure baseFieldsInitializer) {
        // validate input
        Validate.notNull(baseFieldsInitializer, 'Provide initializer object to setup base fields')
        Validate.isTrue(stringTags && !stringTags.isEmpty(), 'Provide not null and not empty tags')
        // create article
        Article article = new Article()
        baseFieldsInitializer.call(article)
        // initialize tags
        stringTags.each({ article.addToTags(tagService.getOrCreate(it)) })
        // find content source
        article.source = ContentSource.findByTarget(contentSourceTarget)
        // try to save it
        if (!article.save()) {
            log.error("Error during saving article ${article}: ${article.errors}")
            return null
        }
        // all ok - article created
        return article
    }

    @Transactional(readOnly = true)
    public List<Article> getByTag(final Tag tag) {
        List<Long> result = (List<Long>) Article.executeQuery('''
                select
                    distinct a.id
                from
                    Article a
                inner join
                    a.tags t
                where
                    t = :tag
            ''',
            [
                tag: tag
            ]
        )
        return result.collect { Article.get(it) }
    }

    @Transactional(readOnly = true)
    public List<Article> findByHashCodes(final List<String> hashCodes) {
        List<Long> result = (List<Long>) Article.executeQuery('''
            select
                id
            from
                Article
            where
                hash in :hashCodes
            order by
                source.priority desc,
                publicationDate desc
            ''',
            [
                hashCodes: hashCodes
            ]
        )

        return result.collect { Article.read(it) }
    }

    @Transactional(readOnly = true)
    public Article findByHashCode(final String hashCode) {
        Article.executeQuery('''
            from
                Article
            where
                hash = :hashCode
            ''',
            [
                hashCode: hashCode,
            ]
        ).first()
    }
}
