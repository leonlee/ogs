package org.riderzen.ogs.common

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-16
 */
enum Address {
    appProtocol("ogs.protocol"),
    sysAuthRegister("ogs.mod.sys-auth.register"),
    dataStore("ogs.plat.data-store")

    public String val;

    Address(String val) {
        this.val = val
    }

    @Override
    public String toString() {
        return val;
    }
}
