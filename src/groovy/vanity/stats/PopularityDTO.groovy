package vanity.stats

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = ['elementId'])
class PopularityDTO {

    final Long elementId

    final Integer rank

    private PopularityDTO(Long elementId, Integer rank) {
        this.elementId = elementId
        this.rank = rank
    }

    public static valueOf(final Object[] queryResult) {
        new PopularityDTO((Long) queryResult[0], (Integer) queryResult[1])
    }
}
