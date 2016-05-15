package com.antuansoft.herospace;




 public class MyThreadLocalStuff{

    private static final ThreadLocal t = new ThreadLocal();

    private MyThreadLocalStuff(){
    }


    public static Object get() {
        return t.get();
    }


    public static void set(Object o) {
        t.set(o);
    }

}

