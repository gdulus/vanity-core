package vanity.article

import groovy.sql.Sql
import groovy.util.logging.Slf4j
import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Transactional
import vanity.utils.DomainUtils

import javax.sql.DataSource
import java.sql.SQLException

@Slf4j
class TagService {

    DataSource dataSource

    @Transactional
    public Tag getOrCreate(final String tagName) {
        // validate input
        Validate.notEmpty(tagName, 'Provide not null tag')
        // prepare tag name and preform creation/find action
        String cleanedUpTagName = cleanUpTagName(tagName)
        return executeGetOrCreate(Tag.findByName(cleanedUpTagName), cleanedUpTagName)
    }

    private static String cleanUpTagName(final String tagName) {
        return tagName.trim().toLowerCase()
    }

    private Tag executeGetOrCreate(final Tag tag, final String cleanedUpTagName) {
        if (tag) {
            return tag
        }

        try {
            Sql sql = new Sql(dataSource)
            sql.executeInsert(
                [
                    name: cleanedUpTagName,
                    hash: DomainUtils.generateHash(Tag.class, cleanedUpTagName),
                    status: Status.Tag.TO_BE_REVIEWED.toString(),
                    root: false
                ],
                '''
                    INSERT INTO
                        tag(
                            id,
                            name,
                            hash,
                            status,
                            date_created,
                            last_updated,
                            root
                        )
                    SELECT
                        nextval('hibernate_sequence'),
                        :name,
                        :hash,
                        :status,
                        now(),
                        now(),
                        :root
                    WHERE
                        NOT EXISTS (
                            SELECT 1 FROM tag WHERE hash = :hash
                        );
                '''
            )
        } catch (SQLException exp) {
            log.warn('Exception during creating tag {}', cleanedUpTagName)
        }

        return Tag.findByName(cleanedUpTagName)
    }

    @Transactional(readOnly = true)
    public Tag readByHash(final String hash) {
        return hash ? Tag.findByHash(hash) : null
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


