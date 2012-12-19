package org.riderzen.ogs.common

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-18
 */
class NoPropertyFoundException extends Exception {
    NoPropertyFoundException(name) {
        super("No $name property found")
    }
}
