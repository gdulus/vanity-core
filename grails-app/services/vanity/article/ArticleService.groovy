package vanity.article

import org.apache.commons.lang.Validate

class ArticleService {

    TagService tagService

    Article create(final Set<String> stringTags, final Closure baseFieldsInitializer) {
        // validate input
        Validate.notNull(baseFieldsInitializer, 'Provide initializer object to setup base fields')
        Validate.isTrue(stringTags && !stringTags.isEmpty(), 'Provide not null and not empty tags')
        // create article
        Article article = new Article()
        baseFieldsInitializer.call(article)
        // initialize tags
        stringTags.each({article.addToTags(tagService.getOrCreate(it))})
        // setup article state
        article.status = article.shouldBeReviewed() ? Status.TO_BE_REVIEWED : Status.PUBLISHED
        // try to save it
        if (!article.save()){
            log.error("Error during saving article ${article}: ${article.errors}")
            return null
        }
        // all ok - article created
        return article
    }
}
