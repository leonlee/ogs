package org.riderzen.ogs.auth

import hirondelle.date4j.DateTime
import net.sf.oval.Validator
import net.sf.oval.constraint.Email
import net.sf.oval.constraint.MaxLength
import net.sf.oval.constraint.MinLength
import net.sf.oval.constraint.NotNull
import org.riderzen.ogs.common.BaseModel
import org.riderzen.ogs.common.EventHelper
import org.riderzen.ogs.common.Tools

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-19
 */
class User extends BaseModel {
    @MinLength(6)
    @MaxLength(64)
    String username
    @MinLength(6)
    @MaxLength(32)
    String password
    @NotNull
    UserType type
    @NotNull
    UserStatus status
    @Email(when = "groovy:_value != null")
    String email
    @NotNull(when = "groovy:_this.email != null")
    Long emailBindOn
    String mobile
    String deviceToken
    String clientVersion
    Long lastNodeId
    Long lastLoginOn

    static User register(EventHelper eh) {
        def param = eh.param

        User user = new User()
        user.username = param.username
        user.password = param.password
        user.email = param.email
        user.mobile = param.mobile
        user.deviceToken = param.deviceToken
        user.clientVersion = param.clientVersion

        if (param.email) {
            user.emailBindOn = Tools.currentTime()
        }

        user.save(eh) {
            user.password = Tools.encrypt(user.password)
        }
    }
}

enum UserType {
    auto, register
}

enum UserStatus {
    normal, blocked
}