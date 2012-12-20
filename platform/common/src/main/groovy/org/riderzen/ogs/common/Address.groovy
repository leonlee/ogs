package org.riderzen.ogs.common

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-16
 */
enum Address {
    appProtocol("org.riderzen.ogs.protocol"),
    sysAuthRegister("org.riderzen.ogs.mod.sys-auth.register"),
    platJdbc("org.riderzen.ogs.plat.jdbc")

    public String val;

    Address(String val) {
        this.val = val
    }

    @Override
    public String toString() {
        return val;
    }
}
