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

def testAddress = 'test.tcp.protocol'
eb.registerHandler(testAddress) { message ->
    println "received message in $testAddress ${message.body}"

    message.reply new Gson().toJson([status: 200, data: 'ok'])
}

client.connect(6543, "127.0.0.1") { socket ->
    println "started test client"

    socket.exceptionHandler { ex -> println "Failed to connect $ex" }

    MessagePack msgpack = new MessagePack()
    socket.dataHandler { buffer ->
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer.getBytes())
        Unpacker upacker = msgpack.createUnpacker(inputStream)
        def message = upacker.read(String.class)
        println "Received message ${message}"

        logger.debug "Received message ${message}"

        def mod_id = vertx.sharedData.getMap('ogs')['test-mod-id']
        vertx.sharedData.getMap('ogs').remove('test-mod-id')
        container.undeployModule(mod_id) {
            logger.info "testing was done!"
        }
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream()
    Packer packer = msgpack.createPacker(out)
    packer.write(new Gson().toJson([endpoint: testAddress, params: [id: 1, name: 'test']]))

    Buffer buffer = new Buffer(out.toByteArray())
    socket << buffer
}