package com.mx.ashe.ashecontrol.app;

/**
 * Created by OmarLenin on 03/03/2016.
 */
public class EndPoints {
    // localhost url -
    public static final String BASE_URL2 = "http://sistema.tecnocom.com.mx/cJson";
    public static final String BASE_URL = "http://192.168.101.109/gcm_chat/v1";
    public static final String LOGIN = BASE_URL2 + "/JsonLogin";
    public static final String Registro = BASE_URL2 + "/login";
    public static final String USER = BASE_URL2 + "/updateGCM";
    public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms";
    public static final String CHAT_THREAD = BASE_URL + "/chat_rooms/_ID_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_rooms/_ID_/message";
}