package com.kasv.gunda.bootcamp.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TimeoutService {

    private final Map<Long, Integer> badPasswordsCount;
    private final Map<Long, Long> timeoutMap;
    private static TimeoutService instance;

    private TimeoutService() {
        badPasswordsCount = new ConcurrentHashMap<>();
        timeoutMap = new ConcurrentHashMap<>();
    }

    public static synchronized TimeoutService getInstance() {
        if (instance == null) {
            instance = new TimeoutService();
        }
        return instance;
    }

    public void incrementCount(Long userId) {
        badPasswordsCount.put(userId, badPasswordsCount.getOrDefault(userId, 0) + 1);
        System.out.println(badPasswordsCount);
    }

    public int getBadPasswordsCount(long id) {
        return Integer.parseInt(badPasswordsCount.getOrDefault(id, 0).toString());
    }

    public void resetBadPasswordsCount(long id) {
        badPasswordsCount.put(id, 0);
        System.out.println(badPasswordsCount);
    }

    public void setUserTimeout(long id) {
        long start = System.currentTimeMillis();
        long end = start + 5 * 1000;
        timeoutMap.put(id, end);
        resetBadPasswordsCount(id);
    }

    public boolean isUserOnTimeout(long id) {
        if (timeoutMap.containsKey(id)) {
            long currentTime = System.currentTimeMillis();
            if (currentTime < timeoutMap.get(id)) {
                return true;
            } else {
                timeoutMap.remove(id);
            }
        }
        return false;
    }
}
