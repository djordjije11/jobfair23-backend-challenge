package com.nordeus.jobfair.auctionservice.auctionservice.domain.jpa;

import org.hibernate.Hibernate;

public final class JpaLoader {
    public static <T> T load(T obj) {
        Hibernate.initialize(obj);
        return obj;
    }

    public static void load(Object... objs) {
        for (Object obj:
             objs) {
            Hibernate.initialize(obj);
        }
    }
}
