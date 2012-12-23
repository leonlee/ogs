package org.riderzen.ogs.ds

import com.jolbox.bonecp.BoneCP
import com.jolbox.bonecp.BoneCPConfig
import groovy.sql.Sql

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

import static org.riderzen.ogs.common.Tools.logger

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-23
 */
class DataSourceHolder {
    static DataSourceHolder instance;
    static initialized = false
    BoneCPConfig[] poolConfigs
    BoneCP[] pools
    ConcurrentMap connections
    Range<Long>[] shardRanges

    private DataSourceHolder(config) {
        if (!config.shards) throw new DataPoolException("Invalid config ${config}")

        logger.debug "initializing data source holder with config: $config"

        poolConfigs = new BoneCPConfig[config.shards.length]
        pools = new BoneCP[poolConfigs.length]
        shardRanges = new Range<Long>[pools.length]

        def i = 0
        for (shard in config.shards) {
            poolConfigs[i] = new BoneCPConfig(
                    jdbcUrl: shard.url,
                    username: shard.user,
                    password: shard.password,
                    partitionCount: shard.partitionCount,
                    maxConnectionsPerPartition: shard.maxConnectionsPerPartition,
                    minConnectionsPerPartition: shard.minConnectionsPerPartition,
                    acquireIncrement: shard.acquireIncrement
            )

            shardRanges[i] = (shard.range[0]..shard.range[1])
            pools[i] = new BoneCP(poolConfigs[i])
            i++
        }
        connections = new ConcurrentHashMap()

        initialized = true
    }

    def static init(config) {
        instance = new DataSourceHolder(config)
    }

    def static getInstance() {
        if (!initialized) throw new DataPoolException("Non initialized.")
        instance
    }

    def getDefaultSql() {
        new Sql(pools[0].getConnection())
    }

    def getSql(id) {
        if (!id) return getDefaultSql()

        def sql = null
        for (i in 0..shardRanges.length) {
            if (shardRanges[i].contains(id)) {
                sql = new Sql(pools[i].getConnection())
            }
        }

        if (!sql) throw new DataPoolException("No shard found with id: ${id}")

        return sql
    }
}
