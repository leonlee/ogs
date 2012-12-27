package org.riderzen.ogs.common

 /**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-27
 */

class BaseProcessor {
    Long pid
    ProcessHelper ph

    BaseProcessor(Long pid, ProcessHelper ph) {
        this.pid = pid
        this.ph = ph
    }
}