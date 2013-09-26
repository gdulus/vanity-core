package vanity.tracking

abstract class Click {

    Long day = 0

    Long time = 0

    static mapping = {
        version: false
    }

    def beforeInsert() {
        Date dateWithTime = new Date()
        Date dateWithoutTime = new Date().clearTime()
        day = dateWithoutTime.time
        time = dateWithTime.time - dateWithoutTime.time
    }

}
