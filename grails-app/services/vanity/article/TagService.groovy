package vanity.article

import groovy.util.logging.Slf4j
import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Transactional

@Slf4j
class TagService {

    @Transactional(readOnly = true)
    public Tag read(final Long id) {
        return Tag.read(id)
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
            tag = new Tag(name: tagName, normalizedName: normalizedName, status: TagStatus.TO_BE_REVIEWED, root: false)
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

    @Transactional
    public boolean updateTagStatus(final Long id, final TagStatus status) {
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

    @Transactional(readOnly = true)
    public List<Tag> findAllActiveByQuery(final String query, final Boolean root) {
        return Tag.executeQuery("""
                select
                    id
                from
                    Tag t
                where
                    lower(name) like :query
                    and status in (:openStatuses)
                    and root = :root
                """,
            [
                query: "%${query?.toLowerCase()}%",
                openStatuses: TagStatus.OPEN_STATUSES,
                root: root
            ]
        ).collect { Long it -> Tag.read(it) }
    }

    @Transactional(readOnly = true)
    public Tag findByTagName(final String tagName) {
        return Tag.findByNormalizedName(tagName)
    }

    @Transactional(readOnly = true)
    public List<Tag> findAllParents(final Long id) {
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
    public List<Tag> findAllValidRootTags() {
        return Tag.findAllByStatusInListAndRoot(TagStatus.OPEN_STATUSES, true, [sort: 'name'])
    }

    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return Tag.list(sort: 'name')
    }

    @Transactional(readOnly = true)
    public List<Tag> findAllByStatus(final TagStatus status) {
        if (!status) {
            return Collections.emptyList()
        }

        return Tag.findAllByStatus(status, [sort: 'name'])
    }

    @Transactional(readOnly = true)
    public List<Long> findAllValidTagsIds() {
        return Tag.executeQuery("""
                    select
                        id
                    from
                        Tag t
                    where
                        status in (:openStatuses)

                """,
            [
                openStatuses: TagStatus.OPEN_STATUSES
            ]
        ) as List<Long>
    }

    @Transactional(readOnly = true)
    public List<Long> findAllValidTagsIds(final Date point) {
        return Tag.executeQuery("""
                    select
                        id
                    from
                        Tag t
                    where
                        (dateCreated >= :point or lastUpdated >= :point)
                        and status in (:statuses)

                """,
            [
                point: point,
                statuses: TagStatus.OPEN_STATUSES
            ]
        ) as List<Long>
    }

}


