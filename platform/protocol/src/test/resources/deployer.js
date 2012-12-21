load('vertx.js');

var config = {

};

vertx.deployModule('org.riderzen.ogs.platform.protocol-v1.0', config, 1, function(id) {
    console.log('deployed module: org.riderzen.ogs.platform.protocol-v1.0 with id:' + id);
});