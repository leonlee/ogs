
/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Created On: 12-12-5
 */
import org.vertx.java.core.net.NetServer

import static org.vertx.groovy.core.streams.Pump.createPump

def config = container.config
def logger = container.logger

logger.info("initializing tcp server with config: ${config}")

def server = vertx.createNetServer()

server.tcpNoDelay = config.tcpNoDelay
server.sendBufferSize = config.

server.connectHandler { sock ->
    logger.debug("client was connected")
    sock.dataHandler { buffer ->

    }
    sock.exceptionHandler { e ->
        logger.error("caught errors on socket", e)
    }
}.listen(config.port, config.host)

