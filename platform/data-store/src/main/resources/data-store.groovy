import com.google.gson.Gson
import org.msgpack.MessagePack
import org.msgpack.packer.Packer
import org.msgpack.unpacker.Unpacker
import org.riderzen.ogs.common.Address
import org.riderzen.ogs.common.DataStoreOperation
import org.riderzen.ogs.ds.CreateOperation
import org.riderzen.ogs.ds.DataSourceHolder
import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.Message

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-22
 */
def config = container.config
def logger = container.logger
def eb = vertx.eventBus

logger.info "starting data-store..."
config = config?: new Gson().fromJson(new File('config.json').text, Map.class)
logger.info "config: $config"

DataSourceHolder.init(config)

eb.registerHandler(Address.dataStore.val) { Message message ->
    DataStoreOperation operation = message.body.operation
    String expression = message.body.expression
    Map params = message.body.params

    switch (operation) {
        case DataStoreOperation.create:
            def id = new CreateOperation(params: params).execute()
            def result = id ? [error: false, id: id] : [error: "can't save"]
            message.reply result
            break;
        case DataStoreOperation.read:
            break;
        case DataStoreOperation.update:
            break;
        case DataStoreOperation.delete:
            break;
    }

}