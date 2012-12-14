package org.riderzen.ogs.protocol

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.msgpack.MessagePack
import org.msgpack.packer.Packer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.vertx.java.core.buffer.Buffer
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
        MessagePack messagePack = new MessagePack()
        Packer packer = messagePack.createBufferPacker()

        packer.write("['hello', [1, 2, 3, 'world']]")

        getVertx().eventBus().send("org.riderzen.ogs.protocol", new Buffer(packer.toByteArray()))


    }
}
