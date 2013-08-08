package vanity.article

import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class TagService {

    private static final int MAX_NUMBER_OF_RETRIES = 10

    private static String cleanUpTagName(final String tagName){
        return tagName.trim().toLowerCase()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Tag getOrCreate(final String tagName) {
        // validate input
        Validate.notEmpty(tagName, 'Provide not null tag')
        // prepare tag name and preform creation/find action
        String cleanedUpTagName = cleanUpTagName(tagName)
        return executeGetOrCreate(Tag.findByName(cleanedUpTagName), cleanedUpTagName)
    }

    private Tag executeGetOrCreate(final Tag tag, final String cleanedUpTagName, final counter = 0){
        // ok tag exists, return value
        if (tag){
            return tag
        }
        // check if we haven't exceed number of retries
        if (counter > MAX_NUMBER_OF_RETRIES){
            throw new IllegalStateException('To much retries')
        }
        // tag not exist, prepare it
        Tag newTag = new Tag(name: cleanedUpTagName, status: Status.Tag.TO_BE_REVIEWED)
        // try to save it - if its going to fail, then we have uniqueness issue
        if (newTag.save(flush: true)){
            return newTag
        }
        // check if other thread created this tag
        return executeGetOrCreate(Tag.findByName(cleanedUpTagName), cleanedUpTagName, ++counter)
    }

    @Transactional(readOnly = true)
    public Tag readByHash(final String hash){
        return hash ? Tag.findByHash(hash) : null
    }

    @Transactional(readOnly = true)
    public List<Tag> getAllValidRootTags(){
        return Tag.findAllByStatusInListAndRoot(Status.Tag.OPEN_STATUSES, true, [sort:'name'])
    }

    @Transactional(readOnly = true)
    public List<Tag> getAllTags(){
        return Tag.list(sort:'name')
    }

    @Transactional(readOnly = true)
    public List<Tag> getAllTagsByStatus(final Status.Tag status){
        if (!status){
            return Collections.emptyList()
        }

        return Tag.findAllByStatus(status, [sort:'name'])
    }

    @Transactional
    public boolean changeTagStatus(final Long id, final Status.Tag status) {
        // validate input
        Validate.notEmpty(id, 'Provide not null tag id')
        Validate.notEmpty(status, 'Provide not null tag status')
        // prepare tag name and preform creation/find action
        Tag tag = Tag.get(id)
        // check if there is anything to update
        if (!tag){
            return false
        }
        // execute update
        tag.status = status
        return tag.save()
    }

}


