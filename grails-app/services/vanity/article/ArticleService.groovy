package vanity.article

import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Transactional
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean

class ArticleService implements PaginationAware<Article> {

    TagService tagService

    @Transactional(readOnly = true)
    PaginationBean<Article> listWithPagination(final Long max, final Long offset, final String sort) {
        return new PaginationBean<Article>(Article.list(max: max, offset: offset, sort: sort), Article.count())
    }

    @Transactional(readOnly = true)
    Article read(final Long id) {
        return Article.read(id)
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
    public List<Article> findByHashCodes(final List<String> hashCodes) {
        return Article.executeQuery('''
            from
                Article
            where
                hash in :hashCodes
            ''',
            [
                hashCodes: hashCodes
            ]
        )
    }

    @Transactional(readOnly = true)
    public List<Article> findAllFromThePointOfTime(final Date point) {
        return Article.findAll { dateCreated >= point }
    }

    @Transactional(readOnly = true)
    public List<Article> list() {
        return Article.list()
    }

    @Transactional(readOnly = true)
    public Integer count() {
        return Article.count()
    }

    @Transactional(readOnly = true)
    public Article findByExternalId(final String externalId) {
        return Article.findByExternalId(externalId)
    }
}
