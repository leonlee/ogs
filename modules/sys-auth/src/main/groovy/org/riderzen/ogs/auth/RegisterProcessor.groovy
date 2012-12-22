package org.riderzen.ogs.auth

import org.riderzen.ogs.common.Address
import org.riderzen.ogs.common.EventHelper
import org.riderzen.ogs.common.Tools

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-21
 */
class RegisterProcessor{
    EventHelper eh

    def process() {
        def user = User.populate(eh.param)
        if (user) {
            eh.newProcess(1)
            eh.eb.send(Address.platJdbc.val, user.toJson()) { message ->
                eh.sendOK(user.toJson())
            }
        }
    }
}
