package com.galaxydl.e_university.data.source.network.cookie

import okhttp3.Cookie
import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput

class ExternalizableCookie(@Transient private var cookie: Cookie) : Externalizable {

    companion object {
        private const val serialVersionUID: Long = 2114151658939974797L
    }

    fun getCookie() = cookie

    override fun writeExternal(output: ObjectOutput?) {
        output!!
        output.writeObject(cookie.name())
        output.writeObject(cookie.value())
        output.writeLong(cookie.expiresAt())
        output.writeObject(cookie.path())
        output.writeBoolean(cookie.secure())
        output.writeBoolean(cookie.httpOnly())
        output.writeBoolean(cookie.hostOnly())
        output.writeObject(cookie.domain())
    }

    override fun readExternal(input: ObjectInput?) {
        val builder = Cookie.Builder()
        input!!
        builder.name(input.readObject() as String)
                .value(input.readObject() as String)
                .expiresAt(input.readLong())
                .path(input.readObject() as String)
        if (input.readBoolean()) {
            builder.secure()
        }
        if (input.readBoolean()) {
            builder.httpOnly()
        }
        if (input.readBoolean()) {
            builder.hostOnlyDomain(input.readObject() as String)
        } else {
            builder.domain(input.readObject() as String)
        }
        cookie = builder.build()
    }

}