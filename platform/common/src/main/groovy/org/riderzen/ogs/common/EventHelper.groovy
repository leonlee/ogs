package org.riderzen.ogs.common

import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.eventbus.EventBus
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.deploy.Container
import org.vertx.java.core.eventbus.Message
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.logging.Logger

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

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

    void sendOK(Message<JsonObject> message) {
        sendOK(message, null)
    }

    void sendStatus(String status, Message<JsonObject> message) {
        sendStatus(status, message, null)
    }

    void sendStatus(String status, Message<JsonObject> message, JsonObject json) {
        if (counter.decrementAndGet()) return
        
        if (json == null) {
            json = new JsonObject()
        }
        json.putString("status", status)
        message.reply(json)
    }

    void sendOK(Message<JsonObject> message, JsonObject json) {
        sendStatus("ok", message, json)
    }

    void sendError(Message<JsonObject> message, String error) {
        sendError(message, error, null)
    }

    void sendError(Message<JsonObject> message, String error, Exception e) {
        logger.error(error, e)
        JsonObject json = new JsonObject().putString("status", "error").putString("message", error)
        message.reply(json)
    }
}
