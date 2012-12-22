/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-22
 */

def logger = container.logger
def config = container.config
def appConfig = [
        tcpServerConfig: [
            id: 1
        ],
        protocolConfig: [
            id: 2
        ]
]

logger.info "starting ogs..."

container.deployModule("org.riderzen.ogs.platform.tcp-server-v1.0", appConfig.tcpServerConfig, 1) {
    container.deployModule("org.riderzen.ogs.platform.protocol-v1.0", appConfig.protocolConfig, 1) {
        logger.info "ogs was started"
        if (config['test']) {
            logger.info 'deploying test mod'
            container.deployModule('org.riderzen.ogs.platform.test-v1.0', null, 1) {
                logger.info "starting test... ${it}"
                vertx.sharedData.getMap('ogs')['test-mod-id'] = it
            }
        }
    }
}

