package com.normal.core.mybatis;

public class Pages {

    private static final ThreadLocal<Page<?>> PAGEABLE_THREAD_LOCAL = new ThreadLocal();


    public static Page<?> getPageLocal() {
        return PAGEABLE_THREAD_LOCAL.get();
    }

    public static void removePageLocal() {
        PAGEABLE_THREAD_LOCAL.remove();
    }

    public static void setPageLocal(Page<?> page) {
        PAGEABLE_THREAD_LOCAL.set(page);
    }

    public static Page<?> query(Invoker invoker) {
        try {
            invoker.invoke();
            return getPageLocal();
        } finally {
            removePageLocal();
        }
    }

}