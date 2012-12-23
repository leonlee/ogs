package org.riderzen.ogs.common

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-23
 */
class DataStoreHelper {
    def static String generateInsertSql(model) {
        StringBuilder buffer = new StringBuilder("insert into ")
        buffer.append(model.tableName)
        buffer.append(" (")
        StringBuilder paramBuffer = new StringBuilder()
        boolean first = true
        for (String column : model.pAttributes().keySet()) {
            if (first) {
                first = false
                paramBuffer.append("?")
            } else {
                buffer.append(", ")
                paramBuffer.append(", ?")
            }
            buffer.append(column)
        }
        buffer.append(") values (")
        buffer.append(paramBuffer.toString())
        buffer.append(")")

        buffer.toString()
    }
}
