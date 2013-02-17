package vanity.article

import org.apache.commons.lang.Validate

class TagService {

    private static final int MAX_NUMBER_OF_RETRIES = 10

    Tag getOrCreate(final String tagName) {
        // validate input
        Validate.notEmpty(tagName, 'Provide not null tag')
        // prepare tag name and preform creation/find action
        String cleanedUpTagName = cleanUpTagName(tagName)
        return getOrCreate(Tag.findByName(cleanedUpTagName), cleanedUpTagName)
    }

    private String cleanUpTagName(final String tagName){
        return tagName.trim().capitalize()
    }

    private Tag getOrCreate(final Tag tag, final String cleanedUpTagName, final counter = 0){
        // check if we haven't exceed number of retries
        if (counter > MAX_NUMBER_OF_RETRIES){
            throw new IllegalStateException('To much retries')
        }
        // ok tag exists, return value
        if (tag){
            return tag
        }
        // tag not exist, prepare it
        Tag newTag = new Tag(name: cleanedUpTagName, status: Status.TO_BE_REVIEWED)
        // try to save it - if its going to fail, then we have uniqueness issue
        if (newTag.save()){
            return newTag
        }
        // check if other thread created this tag
        return getOrCreate(Tag.findByName(cleanedUpTagName), cleanedUpTagName, ++counter)
    }

}
