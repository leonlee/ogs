package org.riderzen.ogs.protocol

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.msgpack.MessagePack
import org.msgpack.unpacker.Unpacker
import org.msgpack.util.json.JSON
import org.vertx.java.busmods.BusModBase
import org.vertx.java.core.Handler
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.eventbus.Message
import org.vertx.java.core.parsetools.RecordParser
import com.google.gson.Gson
import org.riderzen.ogs.common.AE

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
        address = getOptionalStringConfig("address", AE.appProtocol.val)

        def msgHandler = { Buffer message ->

        }

        eb.registerHandler(address, new Handler<Message<Buffer>>() {
            @Override
            void handle(Message<Buffer> event) {
                Buffer message = event.body
                logger.debug("received message length ${message.length()}")
                println("received message length ${message.length()}")

                MessagePack messagePack = new MessagePack()
                Unpacker unpacker = messagePack.createUnpacker(new ByteArrayInputStream(message.getBytes()))
                String messagString = unpacker.read(String.class)
                Gson gson = new Gson()
                Object[] tokens = gson.fromJson(messagString, Object[].class)

                logger.debug("message: $gson")
                println("message: $tokens")

                Buffer result = new Buffer("OK".getBytes())
                event.reply(result)

                logger.debug("result was sent")
            }
        })
    }
}
