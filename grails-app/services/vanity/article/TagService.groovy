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
    public boolean setStatus(final Tag tag, final TagStatus status) {
        tag.status = status
        tag.save() != null
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
        findAllParents(Tag.load(id))
    }

    @Transactional(readOnly = true)
    public List<Tag> findAllParents(final Tag tag) {
        return Tag.executeQuery("""
                from
                    Tag t
                where
                    t.status in :statuses
                    and :tag in elements(t.childTags)
            """,
            [
                statuses: TagStatus.OPEN_STATUSES,
                tag: tag
            ]
        )
    }

    @Transactional(readOnly = true)
    public List<Tag> findAllRootParents(final Long id) {
        Tag tag = Tag.get(id)

        if (tag.root) {
            return [tag]
        }

        List<Tag> roots = []
        List<Tag> parents = findAllParents(tag)

        while (parents.size() > 0) {
            Tag parent = parents.pop()

            if (parent.root) {
                roots << parent
                break
            }

            List<Tag> parentParents = findAllParents(parent)
            parents += parentParents
        }

        return roots.unique()
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

}


