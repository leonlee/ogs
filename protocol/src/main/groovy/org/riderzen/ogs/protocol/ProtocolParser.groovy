package org.riderzen.ogs.protocol

import org.msgpack.MessagePack
import org.msgpack.unpacker.Unpacker
import org.vertx.groovy.core.buffer.Buffer
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
        eb.registerHandler(address) {Buffer message ->
            logger.debug("received message length $message.length")

            MessagePack messagePack = new MessagePack()
            Unpacker unpacker = messagePack.createUnpacker(new ByteArrayInputStream(message.getBytes()))
            int size = unpacker.readArrayBegin()
            String operation;
            Object[] params;
            for (i in 0..size) {
                operation = unpacker.readString()
                int pSize = unpacker.readArrayBegin()
                params = new Object[pSize]
                for (j in 0..pSize) {
                    params[j] = unpacker.read(Object.class)
                }
                unpacker.readArrayEnd()
            }
            unpacker.readArrayEnd()

            logger.debug("operation: $operation, params: $params")
        }
    }
}
