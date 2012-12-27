package org.riderzen.ogs.auth

import org.riderzen.ogs.common.Address
import org.riderzen.ogs.common.BaseProcessor
import org.riderzen.ogs.common.DataStoreOperation
import org.riderzen.ogs.common.EntityEvent
import org.riderzen.ogs.common.EventType
import org.riderzen.ogs.common.ProcessHelper
import org.riderzen.ogs.common.Tools
import org.vertx.groovy.core.eventbus.Message

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-21
 */
class RegisterProcessor extends BaseProcessor{

    RegisterProcessor(Long pid, ProcessHelper ph) {
        super(pid, ph)
    }

    def process() {
        def user = User.populate(ph.param)
        EntityEvent event = new EntityEvent(type: EventType.create, rid: ph.rid, pid: pid, id: Tools.nextId(EntityEvent.name))
        event.entity = user.entityName
        event.body = user.toJson()
        if (user) {
            ph.newProcess(2)
            ph.eb.send(Address.dataStore.val, [action: DataStoreOperation.beginTx, rid: ph.rid])

            ph.eb.send(Address.dataStore.val, [action: DataStoreOperation.create, event: event.toJson()]) { Message reply ->
                ph.sendOK('user', user.toJson())
            }
            ph.eb.send(Address.messageBoard.val, [rid: ph.rid, params: messageEvent]) { reply ->
                ph.sendOK()
            }


            ph.eb.send(Address.dataStore.val, [action: DataStoreOperation.commit, rid: ph.rid])

        }
    }
}
