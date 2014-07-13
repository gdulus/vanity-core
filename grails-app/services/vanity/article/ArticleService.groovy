package vanity.article

import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Transactional

class ArticleService {

    TagService tagService

    @Transactional(readOnly = true)
    Article read(final Long id) {
        return Article.read(id)
    }

    @Transactional
    Article create(
        final Set<String> stringTags,
        final ContentSource.Target contentSourceTarget, final Closure baseFieldsInitializer) {
        // validate input
        Validate.notNull(baseFieldsInitializer, 'Provide initializer object to setup base fields')
        Validate.isTrue(stringTags && !stringTags.isEmpty(), 'Provide not null and not empty tags')
        // create article
        Article article = new Article()
        baseFieldsInitializer.call(article)
        article.status = ArticleStatus.ACTIVE
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

    @Transactional
    public void updateLastUpdatedForAllByTag(final Tag tag) {
        Article.executeUpdate('''
            update
                Article a
            set
                a.lastUpdated = :date
            where
                :tag in elements(a.tags)
            ''',
            [
                date: new Date(),
                tag: tag
            ]
        )
    }

    @Transactional(readOnly = true)
    public List<Article> findAllByTag(final Tag tag) {
        return Article.executeQuery('''
            select
                distinct a
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
    }

    @Transactional(readOnly = true)
    public List<Article> findAllNewsetByTagAndDate(final Tag tag, final Date publicationDate, final int max) {
        return Article.executeQuery('''
            select
                distinct a
            from
                Article a
            inner join
                a.tags t
            where
                t = :tag
                and a.publicationDate <= :publicationDate
            order by
                a.publicationDate desc
            ''',
            [
                tag: tag,
                publicationDate: publicationDate
            ],
            [
                max: max
            ]
        )
    }

    @Transactional(readOnly = true)
    public List<Article> findAllByIds(final List<Long> ids) {
        return ids.collect { read(it) }
    }

    @Transactional(readOnly = true)
    public List<Article> findAllWithPublicationDateOlderThan(final Date point) {
        return Article.findAll { publicationDate <= point && status == ArticleStatus.ACTIVE }
    }

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        return Article.findAllWhere(status: ArticleStatus.ACTIVE)
    }

    @Transactional(readOnly = true)
    public List<Long> findAllIds() {
        return Article.executeQuery('''
            select
                id
            from
                Article
            where
                status = :status
            ''',
            [
                status: ArticleStatus.ACTIVE
            ]
        ) as List<Long>
    }

    @Transactional(readOnly = true)
    public Article findByExternalId(final String externalId) {
        return Article.findByExternalId(externalId)
    }

    @Transactional(readOnly = true)
    public Integer count() {
        return Article.count()
    }

}
