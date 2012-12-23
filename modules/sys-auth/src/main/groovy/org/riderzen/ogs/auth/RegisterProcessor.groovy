package org.riderzen.ogs.auth

import org.riderzen.ogs.common.Address
import DBHelper
import org.riderzen.ogs.common.DataStoreOperation
import org.riderzen.ogs.common.EventHelper

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
            eh.eb.send(Address.dataStore.val, [operation: DataStoreOperation.create, params: user.toJson()]) { message ->
                eh.sendOK(user.toJson())
            }
        }
    }
}
