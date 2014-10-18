package vanity.image.gorm

import org.codehaus.groovy.grails.commons.GrailsApplication

public interface ImageContainer {

    public String getImagePath(GrailsApplication grailsApplication)

    public boolean hasImage()

}