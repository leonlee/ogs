package org.riderzen.ogs.common

import hirondelle.date4j.DateTime
import org.apache.commons.codec.digest.DigestUtils

import java.security.MessageDigest

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-19
 */
class Tools {
    static salt = "sJKVn3IGFwjdty37"

    static encrypt(message) {
        DigestUtils.md5Hex(message + salt)
    }

    static currentTime() {
        DateTime.now(TimeZone.getDefault()).getMilliseconds(TimeZone.getDefault())
    }
}
