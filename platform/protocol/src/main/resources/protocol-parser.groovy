import com.google.gson.Gson
import org.msgpack.MessagePack
import org.msgpack.packer.Packer
import org.msgpack.unpacker.Unpacker
import org.riderzen.ogs.common.Address
import org.riderzen.ogs.common.Tools
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.Message

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-21
 */

def config = container.config
def eb = vertx.eventBus
def logger = container.logger

logger.info "starting protocol-parser"

eb.registerHandler(config?.address ?: Address.appProtocol.val) { Message upstream ->
    def buffer = upstream.body

    MessagePack msgpack = new MessagePack()
    Unpacker unpacker = msgpack.createUnpacker(new ByteArrayInputStream(buffer.getBytes()))
    String messageJson = unpacker.read(String.class)

    Gson gson = new Gson()
    Map message = gson.fromJson(messageJson, Map.class)
    def rid = Tools.nextId('rid')

    logger.debug "received rid:${rid} endpoint:${message.endpoint} params:${message.params}"
    println "received rid:${rid} endpoint:${message.endpoint} params:${message.params}"

    eb.send(message.endpoint, gson.toJson([rid: rid, params: message.params])) { Message downstream ->
        logger.debug "received downstream ${downstream.body}"
        println "received downstream ${downstream.body}"

        ByteArrayOutputStream out = new ByteArrayOutputStream()
        Packer packer = msgpack.createPacker(out)
        packer.write((String) downstream.body)

        upstream.reply new Buffer(out.toByteArray())
    }
}