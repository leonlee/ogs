package org.riderzen.ogs.common

import groovy.sql.Sql
import org.slf4j.LoggerFactory

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-18
 */
class DBHelper {
    def static logger = LoggerFactory.getLogger(DBHelper.class)

    static def save(model) {
        if (!model.metaClass.hasProperty('tableName')) {
            logger.error("No tableName property found in $model")
            throw new NoPropertyFoundException('tableName')
        }

        def tableName = model.tableName

        logger.debug("working on table $tableName")

        def sql = getShardSql(model)
        def dataSet = sql.dateSet(tableName)
        dataSet.add(model.pAttributes())
    }

    static def batchSave(models) {
        if (!models) return
        def sql = getShardSql(models[0])

        sql.withBatch { stmt ->
            models.each { it ->
                stmt.addBatch(generateInsertSql(it))
            }
        }
    }

    static def find(model) {
        def sql = getShardSql(model)
        def whereClause;
        def params = new HashMap();
        if (model.primaryKeys?.size() > 1) {
            def whereBuilder = new StringBuilder(" where true ")
            model.primaryKeys.each {
                whereBuilder.append("and ${it}=:${it} ")
                params.put(it, model.getProperty(it))
            }
            whereClause = whereBuilder.toString()
        } else {
            whereClause = "${model.primaryKeys[0]}=:id"
            params.put('id', model.getProperty(model.primaryKeys[0]))
        }
        def row = sql.firstRow("select * from ${model.tableName} $whereClause", params)

        model.pAttributes().each { key, value ->
            model[key] = row[key]
        }
        model
    }


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

    static def getShardSql(model) {
        def sql = defaultSql()
        if (model.metaClass.hasProperty('shardBy')) {
            def shardBy = model.shardBy

            logger.debug("shadBy: $shardBy")

            sql = getShardSql(shardBy)
        }
        sql
    }

    static def defaultSql() {
        return null;
    }
}