import org.riderzen.ogs.auth.User
import org.riderzen.ogs.common.Address
import org.riderzen.ogs.common.EventHelper
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
    def eh = new EventHelper(container: container, vertx: vertx, message: message)
    User user = User.register(eh)
}

