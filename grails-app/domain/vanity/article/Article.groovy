package vanity.article

import org.apache.commons.lang.StringUtils
import vanity.utils.DomainUtils

class Article implements ReviewNecessityAware {

    String externalId

    String hash

    ContentSource source

    String title

    String body

    String url

    Date publicationDate

    Integer rank = 0

    List<Tag> tags

    Date dateCreated

    Date lastUpdated

    static hasMany = [
        tags: Tag
    ]

    static transients = [
        'shouldBeReviewed',
        'shortBody',
        'flatTagSet'
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
    }

    static mapping = {
        version false
        body(type: 'text')
    }

    String getShortBody() {
        return StringUtils.abbreviate(body, 500)
    }

    @Override
    public String toString() {
        return "Article{" +
            "url='" + url + '\'' +
            '}';
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
        return collectFlatTagSet(tags as Set, [] as Set<String>)
    }

    private Set<String> collectFlatTagSet(final Set<Tag> tags, final Set<String> tagsNames) {
        tags.each { final Tag tag ->
            if (tag.hasChildren()) {
                collectFlatTagSet(tag.childTags as Set, tagsNames)
            }

            tagsNames << tag.name
        }

        return tagsNames
    }

}
