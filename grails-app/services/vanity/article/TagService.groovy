package vanity.article

import groovy.util.logging.Slf4j
import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Transactional
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean

import javax.sql.DataSource

@Slf4j
class TagService implements PaginationAware<Tag> {

    DataSource dataSource

    @Transactional(readOnly = true)
    public Tag read(final Long id) {
        return Tag.read(id)
    }

    @Transactional(readOnly = true)
    public List<Tag> getParentTags(final Long id) {
        return Tag.executeQuery("""
            from
                Tag t
            where
                :tag in elements(t.childTags)
        """,
            [
                tag: Tag.load(id)
            ]
        )
    }

    @Transactional(readOnly = true)
    public PaginationBean<Tag> listWithPagination(final Long max, final Long offset, final String sort) {
        return new PaginationBean<Tag>(Tag.list(max: max, offset: offset, sort: sort), Tag.count())
    }

    @Transactional
    public Tag getOrCreate(final String tagName) {
        // validate input
        Validate.notEmpty(tagName, 'Provide not null tag')
        String normalizedName = tagName.encodeAsPrettyUrl()
        // try to find
        Tag tag = Tag.findByNormalizedName(normalizedName)
        // try to create
        if (!tag) {
            tag = new Tag(name: tagName, normalizedName: normalizedName, status: Status.Tag.TO_BE_REVIEWED, root: false)
            // something wrong
            if (!tag.save()) {
                log.warn('Cant save tag {} due to {}', tagName, tag.errors)
            }

            tag = Tag.findByNormalizedName(normalizedName)
        }

        if (!tag) {
            throw new IllegalArgumentException("Cant save tag with name ${tag}")
        }

        return tag
    }

    @Transactional(readOnly = true)
    public Tag readByTagName(final String tagName) {
        return Tag.findByNormalizedName(tagName)
    }

    @Transactional(readOnly = true)
    public List<Tag> getAllValidRootTags() {
        return Tag.findAllByStatusInListAndRoot(Status.Tag.OPEN_STATUSES, true, [sort: 'name'])
    }

    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return Tag.list(sort: 'name')
    }

    @Transactional(readOnly = true)
    public List<Tag> getAllTagsByStatus(final Status.Tag status) {
        if (!status) {
            return Collections.emptyList()
        }

        return Tag.findAllByStatus(status, [sort: 'name'])
    }

    @Transactional
    public boolean changeTagStatus(final Long id, final Status.Tag status) {
        // validate input
        Validate.notEmpty(id, 'Provide not null tag id')
        Validate.notEmpty(status, 'Provide not null tag status')
        // prepare tag name and preform creation/find action
        Tag tag = Tag.get(id)
        // check if there is anything to update
        if (!tag) {
            return false
        }
        // execute update
        tag.status = status
        return tag.save()
    }

}


