package vanity.article

import groovy.transform.ToString
import vanity.utils.DomainUtils

@ToString(includes = ['id', 'name'])
class Tag implements ReviewNecessityAware {

    String name

    String normalizedName

    String hash

    TagStatus status

    Boolean root = false

    Date dateCreated

    Date lastUpdated

    static hasMany = [
        childTags: Tag
    ]

    static transients = [
        'shouldBeReviewed',
        'isPromoted',
        'hasChildren',
        'flatChildrenSet'
    ]

    static constraints = {
        name(nullable: false, blank: false)
        normalizedName(nullable: false, blank: false, unique: true)
        hash(nullable: false, blank: false, unique: true, maxSize: 32)
        status(nullable: false, validator: { val, obj ->
            if (val == TagStatus.PROMOTED && !obj.root) {
                return 'tag.status.aliasTagAsPromoted'
            } else {
                return true
            }
        })
    }

    static mapping = {
        version false
    }

    @Override
    boolean shouldBeReviewed() {
        return status == TagStatus.TO_BE_REVIEWED
    }

    boolean isPromoted() {
        return status == TagStatus.PROMOTED
    }

    def beforeValidate() {
        setUpHash()
        setUpNormalizedName()
    }

    def beforeInsert() {
        setUpHash()
        setUpNormalizedName()
    }

    private void setUpNormalizedName() {
        if (name && !normalizedName) {
            normalizedName = name.encodeAsPrettyUrl()
        }
    }

    private void setUpHash() {
        if (name && !hash) {
            hash = DomainUtils.generateHash(this.class, name)
        }
    }

    public boolean hasChildren() {
        childTags && childTags.size() > 0
    }

    Set<String> flatChildrenSet() {
        return collectFlatChildrenSet(childTags as Set, [] as Set<String>)
    }

    private Set<String> collectFlatChildrenSet(final Set<Tag> tags, final Set<String> tagsNames) {
        tags.each { final Tag tag ->
            if (tag.hasChildren()) {
                collectFlatChildrenSet(tag.childTags as Set, tagsNames)
            }

            tagsNames << tag.name
        }

        return tagsNames
    }

}
