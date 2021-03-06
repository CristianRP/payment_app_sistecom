package com.sistecom.paymentapp.utils

/**
 * Created by: cristianramirez
 * On: 6/06/20 at: 09:59 AM
 *
 */

object AuthenticationError {
    fun UserNotFound(): String {
        return "El usuario no existe"
    }

    fun InvalidParameter(): String {
        return "Verifique los campos de nombre de usuario y contraseña"
    }

    fun InvalidPassword(): String {
        return "Usuario o contraseña incorrectos"
    }

    fun InvalidResponse(): String {
        return "Respuesta del servidor inválida"
    }

    fun LimitExceeded(): String {
        return "Tiempo limite excedido"
    }

    fun UserNameExists(): String {
        return "El usuario ya existe"
    }

    fun UserNotConfirmed(): String {
        return "Usuario no confirmado"
    }

    fun UnknownError(): String {
        return "Error 500"
    }
}