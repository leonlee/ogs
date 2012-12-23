package org.riderzen.ogs.ds

import groovy.sql.Sql

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-23
 */
class CreateOperation {
    def params

    def execute() {
        if (params) {
            DBHelper.save(params)
        }
    }
}
