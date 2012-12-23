package org.riderzen.ogs.common

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import net.sf.oval.Validator
import net.sf.oval.constraint.NotNull

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-7
 */
class BaseModel {
    Long id
    @NotNull
    Long createdOn
    @NotNull
    Long updatedOn
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
        if (!id) {
            createdOn = Tools.currentTime()
            updatedOn = createdOn
        }

        Validator validator = new Validator()
        def errors = validator.validate(this)
        if (errors) {
            throw new InvalidModelException(errors)
        }
        true
    }

    def String toJson() {
        createBuilder()
                .create()
                .toJson(this)
    }

    def fromJson(String json) {
        createBuilder()
                .create()
                .fromJson(json, this.class)
    }

    static GsonBuilder createBuilder() {
        new GsonBuilder()
//                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    }

}
