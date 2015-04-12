package vanity.celebrity

import org.springframework.transaction.annotation.Transactional

class CelebrityQuotationsService {

    @Transactional(readOnly = true)
    public Quotation read(final Long id) {
        return Quotation.read(id)
    }

    @Transactional
    public Quotation save(final Long celebirtyId, final String content, final String source) {
        Celebrity celebrity = Celebrity.get(celebirtyId)

        if (!celebrity) {
            return null
        }

        Quotation quotation = new Quotation(content: content, source: source)
        celebrity.addToQuotations(quotation)
        return quotation.save(failOnError: true)
    }

    @Transactional
    public Quotation update(final Long id, final String content, final String source) {
        Quotation quotation = Quotation.get(id)

        if (!quotation) {
            return null
        }

        quotation.content = content
        quotation.source = source
        return quotation.save(failOnError: true)
    }

    @Transactional
    public void delete(final Long id) {
        Quotation quotation = Quotation.get(id)

        if (quotation) {
            quotation.delete()
        }
    }
}
