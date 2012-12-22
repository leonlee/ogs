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
class EventHelper {
    private AtomicInteger counter

    Container container
    Vertx vertx
    Message message

    EventHelper(Container container, Vertx vertx, Message message) {
        this.container = container
        this.vertx = vertx
        this.message = message
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
        message?.body
    }

    void sendOK() {
        sendOK(Status.OK)
    }

    void sendStatus(int status) {
        sendStatus(status, null)
    }

    void sendStatus(int status, Object data) {
        if (counter.decrementAndGet()) return

        message.reply(new Gson().toJson([status: status, data: data]))
    }

    void sendOK(Object data) {
        sendStatus(Status.OK, data)
    }

    void sendError(String error) {
        sendError(Status.SERVER_ERROR, error)
    }

    void sendError(int status, String error) {
        message.reply(new Gson([status: status, error: error]))
    }
}
