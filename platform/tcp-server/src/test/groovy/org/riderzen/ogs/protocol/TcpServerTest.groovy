package org.riderzen.ogs.protocol

import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.msgpack.MessagePack
import org.msgpack.packer.Packer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.vertx.java.core.Handler
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.eventbus.Message
import org.vertx.java.core.net.NetClient
import org.vertx.java.core.net.NetSocket
import org.vertx.java.test.TestVerticle
import org.vertx.java.test.VertxConfiguration
import org.vertx.java.test.VertxTestBase
import org.vertx.java.test.junit.VertxJUnit4ClassRunner

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Created On: 12-12-14
 */
@RunWith(VertxJUnit4ClassRunner.class)
@VertxConfiguration
//@TestModule(name = 'org.riderzen.ogs.protocol-v1.0')
@TestVerticle(main = 'deployer.js')
class TcpServerTest extends VertxTestBase {
    static Logger logger = LoggerFactory.getLogger(TcpServerTest.class)
    long timeout = 10L
    NetClient client

    @Before
    def void setup() {
        timeout = Long.parseLong(System.getProperty("vertx.test.timeout", "15"))
        logger.debug("rock on vertx!")
        client = getVertx().createNetClient()
    }

    @Test
    def void testTcpServer() {
        getVertx().eventBus().registerHandler('ogs.protocol', new Handler<Message>() {
            @Override
            void handle(Message event) {
                logger.debug("get event" + event.body)
            }
        })

//        sleep(1000000)

        client.connect(6543, "127.0.0.1", new Handler<NetSocket>() {
            @Override
            void handle(NetSocket socket) {
                socket.dataHandler(new Handler<Buffer>() {
                    @Override
                    void handle(Buffer event) {
                        logger.debug("get event: ${event}")
                    }
                })

                def message = new Gson().toJson([endpoint:'test.tcp', params:[id: 1, name: 'client']])
                MessagePack msgpack = new MessagePack()
                ByteArrayOutputStream out = new ByteArrayOutputStream()
                Packer packer = msgpack.createPacker(out)
                packer.write(message)

                socket << out.toByteArray()

            }
        })

    }
}
