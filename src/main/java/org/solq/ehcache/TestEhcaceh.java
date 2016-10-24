package org.solq.ehcache;

import java.io.File;
import java.util.Iterator;

import org.ehcache.Cache;
import org.ehcache.Cache.Entry;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.UserManagedCache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.UserManagedCacheBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.junit.Assert;
import org.junit.Test;
import org.solq.ehcache.model.Player;

public class TestEhcaceh {

    @Test
    public void cache() {
	UserManagedCache<Long, String> userManagedCache = UserManagedCacheBuilder.newUserManagedCacheBuilder(Long.class, String.class).build(false);
	userManagedCache.init();
	userManagedCache.put(1L, "da one!");

	userManagedCache.close();
    }

    @Test
    public void test() {
	CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
		.withCache("preConfigured", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(100)).build()).build(true);

	Cache<Long, String> preConfigured = cacheManager.getCache("preConfigured", Long.class, String.class);
	Cache<Long, String> myCache = cacheManager.createCache("myCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(100)).build());

	myCache.put(1L, "da one!");
	String value = myCache.get(1L);
	Assert.assertEquals(value, "da one!");
	cacheManager.close();
    }

    @Test
    public void coder() {
	CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
	Cache<Long, Player> myCache = cacheManager.createCache("myCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, Player.class, ResourcePoolsBuilder.heap(100)).build());
	Player p = Player.of(1L, "hello");
	myCache.put(1L, p);
	Player np = myCache.get(1L);
	Assert.assertEquals(np, p);
	cacheManager.close();
    }

    String cacheName = "c";

    @Test
    public void disk() {
	PersistentCacheManager persistentCacheManager = getCacheManager();
	Cache<Long, Player> cache = persistentCacheManager.getCache(cacheName, Long.class, Player.class);

	long count = 1000000;
	long start = System.currentTimeMillis();
	long end = System.currentTimeMillis();

	while (count-- > 0) {
	    Player p = Player.of(count, "hello");
	    cache.put(p.getId(), p);
	    if (count % 10000 == 0) {
		end = System.currentTimeMillis();
		System.out.println("count : " + count + " time : " + (end - start));
	    }
	}
	end = System.currentTimeMillis();
	System.out.println(end - start);
	persistentCacheManager.close();
    }

    @Test
    public void disk_get() {
	PersistentCacheManager persistentCacheManager = getCacheManager();
	Cache<Long, Player> cache = persistentCacheManager.getCache(cacheName, Long.class, Player.class);

	int count = 0;
	long start = System.currentTimeMillis();
	long end = System.currentTimeMillis();

	Iterator<Entry<Long, Player>> it = cache.iterator();
	while (it.hasNext()) {
	    it.next();
	    count++;
	}
	System.out.println("count : " + count);
	end = System.currentTimeMillis();
	System.out.println(end - start);
	//persistentCacheManager.close();
    }

    private PersistentCacheManager getCacheManager() {
	PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder().with(CacheManagerBuilder.persistence("myData1")).withCache(cacheName, CacheConfigurationBuilder
		.newCacheConfigurationBuilder(Long.class, Player.class, ResourcePoolsBuilder.newResourcePoolsBuilder().heap(60000, EntryUnit.ENTRIES).disk(10, MemoryUnit.GB, true))).build(true);
	return persistentCacheManager;
    }
}
