package com.winnie.common.auth.pojo;

public class UserHolder {
    private static final ThreadLocal<Long> TL = new ThreadLocal<>();

    public static final void setUserId(Long userId){
        TL.set(userId);
    }
    public static Long getUserId(){
        return TL.get();
    }

    public static void removeUserId(){
        TL.remove();
    }
}
