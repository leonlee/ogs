package org.riderzen.ogs.common

import net.sf.oval.Validator
import net.sf.oval.constraint.NotNull

import java.util.concurrent.atomic.AtomicInteger

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-7
 */
class BaseModel {
    @NotNull
    Long id
    @NotNull
    Long createdOn
    @NotNull
    Long updatedOn
    @NotNull
    Long vsn

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

    def validate() {
        if (!id){
            id = DBHelper.nextID()
            createdOn = Tools.currentTime()
            updatedOn = createdOn
            vsn = 0
        }

        Validator validator = new Validator()
        def errors = validator.validate(this)
        if (errors) {
            throw new InvalidModelException(errors)
        }
        true
    }

    def toJson() {

    }
}
