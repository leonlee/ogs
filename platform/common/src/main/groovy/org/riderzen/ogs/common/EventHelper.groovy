package org.riderzen.ogs.common

import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.eventbus.EventBus
import org.vertx.groovy.deploy.Container
import org.vertx.java.core.eventbus.Message
import org.vertx.java.core.json.JsonObject
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
        sendOK(null)
    }

    void sendStatus(String status) {
        sendStatus(status, null)
    }

    void sendStatus(String status, JsonObject json) {
        if (counter.decrementAndGet()) return

        if (json == null) {
            json = new JsonObject()
        }
        json.putString("status", status)
        message.reply(json)
    }

    void sendOK(JsonObject json) {
        sendStatus("ok", json)
    }

    void sendError(String error) {
        sendError(error, null)
    }

    void sendError(String error, Exception e) {
        logger.error(error, e)
        JsonObject json = new JsonObject().putString("status", "error").putString("message", error)
        message.reply(json)
    }
}
