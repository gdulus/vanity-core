package vanity.article

import grails.transaction.Transactional

class ContentSourceService {

    public ContentSource read(final Long id) {
        ContentSource.read(id)
    }

    public ContentSource get(final Long id) {
        ContentSource.read(id)
    }

    public ContentSource get(final ContentSource.Target target) {
        ContentSource.findByTarget(target)
    }

    @Transactional
    public void disable(final Long id) {
        ContentSource contentSource = ContentSource.get(id)
        contentSource.disabled = true
        contentSource.save()
    }

    @Transactional
    public void enable(final Long id) {
        ContentSource contentSource = ContentSource.get(id)
        contentSource.disabled = false
        contentSource.save()
    }

}
