package org.riderzen.ogs.protocol

import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.msgpack.MessagePack
import org.msgpack.packer.Packer
import org.riderzen.ogs.common.Address
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.java.core.Handler
import org.vertx.java.core.eventbus.Message
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
class ProtocolParserTest extends VertxTestBase {
    static Logger logger = LoggerFactory.getLogger(ProtocolParserTest.class)
    long timeout = 10L

    @Before
    def void setup() {
        timeout = Long.parseLong(System.getProperty("vertx.test.timeout", "15"))
        logger.debug("rock on vertx!")
    }

    @Test
    def void testModulePP() {
        sleep(2000)
        MessagePack messagePack = new MessagePack()
        Packer packer = messagePack.createBufferPacker()

        packer.write("{endpoint: 'test.mpp', params: {id:1, name: 'leon'}}")

        getVertx().eventBus().registerHandler("test.mpp", new Handler<Message<Map>>() {
            @Override
            void handle(Message<Map> event) {
                logger.debug event.body
                event.reply(new Gson().toJson([status: 'ok', data: 'test']))
            }
        })

        getVertx().eventBus().send('ogs.protocol', new Buffer(packer.toByteArray()), new Handler<Message<Buffer>>() {
            @Override
            void handle(Message<Buffer> event) {
                logger.debug("result: ${event.body.toString()}")
            }
        })

        logger.debug("the message was sent")
    }
}
