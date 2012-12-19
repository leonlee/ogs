package org.riderzen.ogs.common

import net.sf.oval.ConstraintViolation

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-19
 */
class InvalidModelException extends Exception {
    InvalidModelException(List<ConstraintViolation> errors) {
        StringBuilder messageBuilder = new StringBuilder("Invalid Model:\n")
        errors.each {
            messageBuilder << it.message
            messageBuilder << "\n"
        }

        this.message = messageBuilder.toString()
    }
}
