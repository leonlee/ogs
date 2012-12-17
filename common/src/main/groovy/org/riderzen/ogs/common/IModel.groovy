package org.riderzen.ogs.common

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-17
 */
public interface IModel {
    void save()

    IModel get(id)

    void update()

    void delete()
}