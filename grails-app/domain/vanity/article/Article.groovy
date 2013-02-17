package vanity.article

import vanity.ContentSource

class Article implements ReviewNecessityAware {

    String id

    ContentSource source

    Status status

    String title

    String body

    String url

    Date publicationDate

    Date dateCreated

    Date lastUpdated

    static hasMany = [
        tags: Tag
    ]

    static embedded = [
        'tags'
    ]

    static constraints = {
        source(nullable: false)
        status(nullable: false)
        body(nullable: false, blank: false)
        title(nullable: false, blank: false)
        url(nullable: false, blank: false, url: true)
        publicationDate(nullable: false)
        tags(nullable: false, minSize: 1)
    }

    static mapping = {
        version false
        autoTimestamp true
    }

    @Override
    public String toString() {
        return "Article{" +
                "url='" + url + '\'' +
                '}';
    }

    @Override
    boolean shouldBeReviewed() {
        // if current article is already in "to be reviewed" state
        if (status && status == Status.TO_BE_REVIEWED){
            return true
        }
        // this operation can be performed only on instance with tags
        if (!tags){
            throw new IllegalStateException('Seems that this instance has no tags. Cant determine review necessity.')
        }
        // if there is no tas with state to be reviewed then all is ok
        return (tags.find({it.shouldBeReviewed()}) != null)
    }
}
