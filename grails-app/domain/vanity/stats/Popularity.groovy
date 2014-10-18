package vanity.stats

abstract class Popularity {

    Integer rank = 0

    Date day

    static mapping = {
        version false
        day index: 'popularity_day_idx'
    }

    static constraints = {
        rank(nullable: false, min: 0)
    }

}
