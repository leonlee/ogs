package org.riderzen.ogs.protocol

import org.vertx.groovy.core.parsetools.RecordParser
import org.vertx.java.busmods.BusModBase

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Created On: 12-12-11
 */
class ProtocolParser extends BusModBase {
    String address;
    RecordParser parser;

    @Override
    void start() {
        super.start()

        init()
    }

    def init() {
        address = getOptionalStringConfig("address", "org.riderzen.ogs.protocol")
        eb.registerHandler(address) { message ->
            logger.debug("received message $message")
            
        }
    }
}
