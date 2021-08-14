package com.gvendas.gestaovendas.exception;

public class Error {

    private String msgUsuario;
    private String msgDev;

    public Error(String msgUsuario, String msgDev) {
        this.msgUsuario = msgUsuario;
        this.msgDev = msgDev;
    }

    public String getMsgUsuario() {
        return msgUsuario;
    }

    public void setMsgUsuario(String msgUsuario) {
        this.msgUsuario = msgUsuario;
    }

    public String getMsgDev() {
        return msgDev;
    }

    public void setMsgDev(String msgDev) {
        this.msgDev = msgDev;
    }
}
