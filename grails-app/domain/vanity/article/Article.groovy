package vanity.article

import groovy.transform.ToString
import org.apache.commons.lang.StringUtils
import vanity.utils.DomainUtils

@ToString(includes = ['hash', 'title'])
class Article implements ReviewNecessityAware {

    private static final PREVIEW_MAX_LENGTH = 500

    String externalId

    String hash

    ArticleStatus status

    ContentSource source

    String title

    String body

    String url

    Date publicationDate

    Set<Tag> tags

    Date dateCreated

    Date lastUpdated

    static hasMany = [
        tags: Tag
    ]

    static transients = [
        'shouldBeReviewed',
        'shortBody',
        'flatTagSet',
        'publicTags'
    ]

    static constraints = {
        externalId(nullable: false, unique: 'source')
        hash(nullable: false, blank: false, unique: true, maxSize: 32)
        source(nullable: false)
        body(nullable: false, blank: false)
        title(nullable: false, blank: false)
        url(nullable: false, blank: false, url: true)
        publicationDate(nullable: false)
        tags(nullable: false, minSize: 1)
        status(nullable: false)
    }

    static mapping = {
        version false
        tags(lazy: false)
        body(type: 'text')
    }

    public Set<Tag> getPublicTags() {
        tags.findAll { it.open }
    }

    String getShortBody() {
        return StringUtils.abbreviate(body, PREVIEW_MAX_LENGTH)
    }

    @Override
    boolean shouldBeReviewed() {
        // this operation can be performed only on instance with tags
        if (!tags) {
            throw new IllegalStateException('Seems that this instance has no tags. Cant determine review necessity.')
        }
        // if there is no tas with state to be reviewed then all is ok
        return (tags.find({ it.shouldBeReviewed() }) != null)
    }

    def beforeValidate() {
        setUpHash()
    }

    def beforeInsert() {
        setUpHash()
    }

    private void setUpHash() {
        if (url && !hash) {
            hash = DomainUtils.generateHash(this.class, url)
        }
    }

    Set<String> flatTagSet() {
        return (Set<String>) tags.sum { it.flatChildrenSet() }
    }
}

