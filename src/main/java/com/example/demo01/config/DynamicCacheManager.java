package com.example.demo01.config;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import java.util.Collection;

public class DynamicCacheManager extends AbstractTransactionSupportingCacheManager {
    @Override
    protected Collection<? extends Cache> loadCaches() {
        return null;
    }
}
