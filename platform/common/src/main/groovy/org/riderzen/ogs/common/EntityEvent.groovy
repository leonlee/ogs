package org.riderzen.ogs.common

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-27
 */
class EntityEvent {
    EventType type
    Long rid
    Long pid
    Long id
    String entity
    String body

    def String toJson() {
        createBuilder()
                .create()
                .toJson(this)
    }

    def fromJson(String json) {
        createBuilder()
                .create()
                .fromJson(json, this.class)
    }

    static GsonBuilder createBuilder() {
        new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
    }
}

enum EventType {
    create, update, delete
}