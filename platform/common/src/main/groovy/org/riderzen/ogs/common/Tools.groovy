package org.riderzen.ogs.common

import hirondelle.date4j.DateTime
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.LocaleUtils
import org.vertx.java.core.impl.Context
import org.vertx.java.core.logging.impl.LoggerFactory

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicLong

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-19
 */
class Tools {
    private static ResourceBundle bundle
    private static AtomicLong idCounter = new AtomicLong(0)
    private static ConcurrentMap<String, AtomicLong> idMap = new ConcurrentHashMap<>()

    static final String MESSAGES = "messages"
    static logger = LoggerFactory.getLogger(Tools.class)
    static salt = "sJK" + "Vn3I" + "GFwj" + "dty37"

    static encrypt(message) {
        if (message) {
            DigestUtils.md5Hex(message + salt())
        }
    }

    static String salt() {
        DigestUtils.md2Hex(salt)
    }

    static currentTime() {
        DateTime.now(TimeZone.getDefault()).getMilliseconds(TimeZone.getDefault())
    }

    static ok(data) {
        [status: 0, data: data]
    }

    static error(String key, String... params) {
        [status: 1, message: t(key, params)]
    }

    static t(String key, String... params) {
        String message = bundle().getString(key)
        if (params) {
            params.each {
                message = message.replaceFirst('\\{\\}', it)
            }
        }
        message
    }

    synchronized static bundle() {
        if (!bundle) {
            String lang = System.getProperty("lang")
            Locale locale;
            if (lang) {
                try {
                    locale = LocaleUtils.toLocale(lang)
                } catch (e) {
                    logger.error("Property lang is invalid: $lang", e)
                }
            } else {
                locale = Locale.default
            }
            try {
                bundle = ResourceBundle.getBundle(MESSAGES, locale)
            } catch (e) {
                logger.error("Can't get bundle 'messages' with locale ${locale}", e)
                bundle = ResourceBundle.getBundle(MESSAGES)
            }
        }
    }

    static def uuid() {
        UUID.randomUUID().toString();
    }

    static long nextId(String name = null) {
        if (!name) {
            return idCounter.getAndIncrement()
        } else {
            if (!idMap.containsKey(name)) {
                idMap.put(name, new AtomicLong(0))
            }
            return idMap.get(name).getAndIncrement()
        }
    }

    static getLogger() {
        Context.context.deploymentHandle.logger
    }
}
