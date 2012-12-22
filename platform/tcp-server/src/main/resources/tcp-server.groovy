import org.riderzen.ogs.common.Address
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.core.net.NetServer

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-21
 */

def config = container.config
def eb = vertx.eventBus
def logger = container.logger

logger.info "starting tcp-server"

def host = config?.host ?: '0.0.0.0'
int port = config?.port ?: 6543
boolean tcpNoDelay = config?.tcpNoDelay ?: false
int sendBufferSize = config?.sendBufferSize ?: 8192
int receiveBufferSize = config?.receiveBufferSize ?: 8192
boolean tcpKeepAlive = config?.tcpKeepAlive ?: true
boolean reuseAddress = config?.reuseAddress ?: true
//boolean soLinger = config?.soLinger ?: false
int trafficClass = config?.trafficClass ?: -1
int backlog = config?.backlog ?: -1

NetServer server = vertx.createNetServer()

server.TCPNoDelay = tcpNoDelay
server.sendBufferSize = sendBufferSize
server.receiveBufferSize = receiveBufferSize
server.TCPKeepAlive = tcpKeepAlive
server.reuseAddress = reuseAddress
//server.soLinger = soLinger

if (trafficClass >= 0 && trafficClass <= 255)
    server.trafficClass = trafficClass
if (backlog > 0)
    server.acceptBacklog = backlog

server.connectHandler { sock ->
    sock.dataHandler { buffer ->
        logger.debug("received ${buffer.getLength()} bytes of data")
        println("received ${buffer.getLength()} bytes of data")
        eb.send(Address.appProtocol.val, buffer) { Message message ->
            logger.debug "received message ${message.body}"
            println "received message ${message.body}"
            sock.write(buffer as Buffer)
        }
    }
    sock.exceptionHandler { e ->
        logger.error("caught error", e)
        println("caught error", e)
    }

}.listen(port, host)