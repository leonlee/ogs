package org.riderzen.ogs.ds

import org.riderzen.ogs.common.BaseEntity
import org.riderzen.ogs.common.NoPropertyFoundException
import org.slf4j.LoggerFactory

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-18
 */
class DBHelper {
    def static logger = LoggerFactory.getLogger(DBHelper.class)

    static def save(BaseEntity entity) {
        if (!entity.metaClass.hasProperty('tableName')) {
            logger.error("No tableName property found in $entity")
            throw new NoPropertyFoundException('tableName')
        }

        def tableName = entity.entityName

        logger.debug("working on table $tableName")

        def sql = getShardSql(entity)
        def dataSet = sql.dateSet(tableName)
        def id = nextId()
        entity.id = id as String
        entity.vsn = 0
        dataSet.add(entity.pAttributes)

        id
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

    static def find(BaseEntity entity) {
        def sql = getShardSql(entity)
        def whereClause;
        def params = new HashMap();
        if (entity.primaryKeys?.size() > 1) {
            def whereBuilder = new StringBuilder(" where true ")
            entity.primaryKeys.each {
                whereBuilder.append("and ${it}=:${it} ")
                params.put(it, entity.getProperty(it))
            }
            whereClause = whereBuilder.toString()
        } else {
            whereClause = "${entity.primaryKeys[0]}=:id"
            params.put('id', entity.getProperty(entity.primaryKeys[0]))
        }
        def row = sql.firstRow("select * from ${entity.entityName} $whereClause", params)

        entity.pAttributes.each { key, value ->
            entity[key] = row[key]
        }
        entity
    }


    def static String generateInsertSql(BaseEntity entity) {
        StringBuilder buffer = new StringBuilder("insert into ")
        buffer.append(entity.entityName)
        buffer.append(" (")
        StringBuilder paramBuffer = new StringBuilder()
        boolean first = true
        for (String column : entity.pAttributes.keySet()) {
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

    static def getShardSql(BaseEntity entity) {
        def sql = defaultSql()
        if (entity.metaClass.hasProperty('shardBy')) {
            def shardBy = entity.shardBy

            logger.debug("shadBy: $shardBy")

            sql = getShardSql(shardBy)
        }
        sql
    }

    static def defaultSql() {
        return DataSourceHolder.defaultSql;
    }

    static long nextId() {

    }
}