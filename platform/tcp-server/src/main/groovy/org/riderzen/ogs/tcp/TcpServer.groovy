package org.riderzen.ogs.tcp

import org.msgpack.MessagePack
import org.msgpack.packer.Packer
import org.vertx.java.busmods.BusModBase
import org.riderzen.ogs.common.Address
import org.vertx.java.core.buffer.Buffer

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-7
 */
class TcpServer extends BusModBase {
    def server = null
    def host
    def port
    def tcpNoDelay
    def sendBufferSize
    def receiveBufferSize
    def tcpKeepAlive
    def reuseAddress
    def soLinger
    def trafficClass
    def backlog

    def onConnected = { sock ->
        sock.dataHandler { buffer ->
            logger.debug("received ${buffer.lenght} bytes of data")
            eb.send(Address.appProtocol.val, buffer) { message ->
                MessagePack msgpack = new MessagePack()
                def stream = new ByteArrayOutputStream()
                Packer packer = msgpack.createPacker(stream)
                packer.write(message.body.toString().getBytes())
                sock << new Buffer(stream.toByteArray())
            }
        }
        sock.exceptionHandler { e ->
            logger.error("caught error", e)
        }
    }

    @Override
    void start() {
        super.start()

        initConfig()
        initServer()
        initEvent()
        server.listen(port, host)
    }

    def initConfig() {
        host = getOptionalStringConfig("host", "0.0.0.0")
        port = getOptionalIntConfig("port", 6543)
        tcpNoDelay = getOptionalBooleanConfig("tcpNoDelay", false)
        sendBufferSize = getOptionalIntConfig("sendBufferSize", 8192)
        receiveBufferSize = getOptionalIntConfig("receiveBufferSize", 8192)
        tcpKeepAlive = getOptionalBooleanConfig("tcpKeepAlive", true)
        reuseAddress = getOptionalBooleanConfig("reuseAddress", true)
        soLinger = getOptionalIntConfig("soLinger", -1)
        trafficClass = getOptionalIntConfig("trafficClass", -1)
        backlog = getOptionalIntConfig("backlog", -1)
    }

    def initServer() {
        logger.info("initializing server with config: $config")

        server = vertx.createNetServer()
        server.setTCPNoDelay(tcpNoDelay)
        server.setSendBufferSize(sendBufferSize)
        server.setReceiveBufferSize(receiveBufferSize)
        server.setTCPKeepAlive(tcpKeepAlive)
        server.setReuseAddress(reuseAddress)
        if (soLinger > 0)
            server.setSoLinger(soLinger)
        if (trafficClass >= 0 && trafficClass <= 255)
            server.setTrafficClass(trafficClass)
        if (backlog > 0)
            server.setAcceptBacklog(backlog)

        server.connectHandler(onConnected)
    }

    def initEvent() {
        eb.registerHandler("sys.tcp.stop") { message ->
            logger.info("received stop message, starting stop tcp server...")
            server.close { logger.info("tcp server was stopped") }
        }
    }
}
