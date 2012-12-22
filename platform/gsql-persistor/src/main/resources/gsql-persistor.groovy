import com.google.gson.Gson
import org.msgpack.MessagePack
import org.msgpack.packer.Packer
import org.msgpack.unpacker.Unpacker
import org.vertx.groovy.core.buffer.Buffer

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-22
 */
def logger = container.logger
def eb = vertx.eventBus
def client = vertx.createNetClient()

//eb.registerHandler(Address)