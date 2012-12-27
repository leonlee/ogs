package org.riderzen.ogs.common

import com.google.gson.Gson
import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.eventbus.EventBus
import org.vertx.groovy.deploy.Container
import org.vertx.java.core.eventbus.Message
import org.vertx.java.core.logging.Logger

import java.util.concurrent.atomic.AtomicInteger

/**
 * Event Helper
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-20
 */
class ProcessHelper {
    private AtomicInteger counter

    Long rid
    Long pid
    Container container
    Vertx vertx
    Message message
    Map results
    Map errors

    ProcessHelper(Container container, Vertx vertx, Message message) {
        this.container = container
        this.vertx = vertx
        this.message = message
        rid = message.body.rid
    }

    def newProcess(int max) {
        counter = new AtomicInteger(max)
    }

    EventBus getEb() {
        vertx?.eventBus
    }

    Logger getLogger() {
        container?.logger
    }

    def getConfig() {
        container?.config
    }

    def getParam() {
        message?.body?.params
    }

    void sendOK() {
        sendStatus(Status.OK)
    }

    void sendStatus(int status) {
        sendStatus(status, null)
    }

    void sendStatus(int status, String name, Object data) {
        results.put(name, data)
        if (counter.decrementAndGet()) return

        message.reply(new Gson().toJson([status: status, data: results]))
    }

    void sendOK(String name, Object data) {
        sendStatus(Status.OK, name, data)
    }

    void sendError(String error) {
        sendError(Status.SERVER_ERROR, error)
    }

    void sendError(int status, String error) {
        message.reply(new Gson([status: status, error: error]))
    }
}
