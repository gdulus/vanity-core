package vanity.stats

import vanity.user.User
import vanity.user.UserActivityType

class UserActivity {

    User user

    UserActivityType actionType

    String ip

    Date dateCreated

    static constraints = {
        user blank: false, nullable: false
        actionType blank: false, nullable: false
    }

    static mapping = {
        version false
        user index: 'user_activity_idx'
        actionType index: 'user_activity_idx'
    }
}
