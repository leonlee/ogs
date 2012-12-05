
/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Created On: 12-12-5
 */
import static org.vertx.groovy.core.streams.Pump.createPump

vertx.createNetServer().connectHandler { socket ->
    createPump(socket, socket).start()
}.listen(1234)
