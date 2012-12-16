package org.riderzen.ogs.common

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-16
 */
enum AE {
    appProtocol("org.riderzen.ogs.protocol")
    public String val;

    AE(String val) {
        this.val = val
    }

    @Override
    public String toString() {
        return val;
    }
}