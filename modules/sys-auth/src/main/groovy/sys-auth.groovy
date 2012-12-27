import org.riderzen.ogs.auth.RegisterProcessor
import org.riderzen.ogs.common.Address
import org.riderzen.ogs.common.ProcessHelper
import org.riderzen.ogs.common.Tools
import org.vertx.groovy.core.eventbus.Message

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-20
 */

def config = container.config
def eb = vertx.eventBus
def logger = container.logger

logger.info "starting sys-auth"

eb.registerHandler(config?.address ?: Address.sysAuthRegister.val) { Message message ->
    logger.debug("received message ${message.body}")
    def eh = new ProcessHelper(container: container, vertx: vertx, message: message)
    new RegisterProcessor(pid: Tools.nextId(RegisterProcessor.name), eh: eh).process()
}

