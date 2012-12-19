package org.riderzen.ogs.auth

import hirondelle.date4j.DateTime
import net.sf.oval.Validator
import net.sf.oval.constraint.Email
import net.sf.oval.constraint.MaxLength
import net.sf.oval.constraint.MinLength
import net.sf.oval.constraint.NotNull
import org.riderzen.ogs.common.BaseModel
import org.riderzen.ogs.common.Tools

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-19
 */
class User extends BaseModel {
    @MinLength(6)
    @MaxLength(64)
    String username
    @NotNull
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

    static User register(String username, String password, String email = null,
                         String mobile = null, String deviceToken = null, String clientVersion = null) {
        User user = new User()
        user.username = username
        user.password = Tools.encrypt(password)
        user.email = email
        user.mobile = mobile
        user.deviceToken = deviceToken
        user.clientVersion = clientVersion

        if (email) {
            user.emailBindOn = Tools.currentTime()
        }

        User.save()
    }
}

enum UserType {
    auto, register
}

enum UserStatus {
    normal, blocked
}