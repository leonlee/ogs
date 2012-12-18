package org.riderzen.ogs.common

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-7
 */
class BaseModel {
    static def transientAttributes = ['class', 'metaClass', 'transientAttributes', 'tableName']

    BaseModel() {
    }

    static def excludeAttributes(fields) {
        transientAttributes.addAll(fields)
    }

    def pAttributes() {
        def propMap = properties;

        transientAttributes?.each {
            propMap.remove(it)
        }

        propMap
    }

}
