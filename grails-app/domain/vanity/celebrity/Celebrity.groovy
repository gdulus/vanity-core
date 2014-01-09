package vanity.celebrity

import org.codehaus.groovy.grails.commons.GrailsApplication
import vanity.article.Tag
import vanity.image.gorm.Image
import vanity.image.gorm.ImageContainer

class Celebrity implements ImageContainer {

    String firstName

    String lastName

    String description

    Image avatar

    static belongsTo = [
        tag: Tag
    ]

    static constraints = {
        firstName(nullable: true, blank: true)
        lastName(nullable: true, blank: true)
        description(nullable: true, blank: true)
        tag(nullable: false, unique: true, validator: {
            return it?.root
        })
        avatar(nullable: true)
    }

    static mapping = {
        description(type: 'text')
    }

    static embedded = [
        'avatar'
    ]

    static transients = [
        'getImagePath',
        'hasImage'
    ]

    @Override
    public String getImagePath(final GrailsApplication grailsApplication) {
        return grailsApplication.config.files.celebrity.host + avatar.name
    }

    @Override
    boolean hasImage() {
        return avatar != null
    }

    @Override
    public String toString() {
        return "Celebrity{" +
            "tag='" + tag + '\'' +
            '}';
    }

}
