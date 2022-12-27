package com.fastshop.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.fastshop.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.fastshop.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.fastshop.domain.User.class.getName());
            createCache(cm, com.fastshop.domain.Authority.class.getName());
            createCache(cm, com.fastshop.domain.User.class.getName() + ".authorities");
            createCache(cm, com.fastshop.domain.Seller.class.getName());
            createCache(cm, com.fastshop.domain.Seller.class.getName() + ".products");
            createCache(cm, com.fastshop.domain.ProductModel.class.getName());
            createCache(cm, com.fastshop.domain.ProductModel.class.getName() + ".products");
            createCache(cm, com.fastshop.domain.Product.class.getName());
            createCache(cm, com.fastshop.domain.Product.class.getName() + ".productDiscounts");
            createCache(cm, com.fastshop.domain.Product.class.getName() + ".propertyDetails");
            createCache(cm, com.fastshop.domain.Product.class.getName() + ".productInventories");
            createCache(cm, com.fastshop.domain.Product.class.getName() + ".comments");
            createCache(cm, com.fastshop.domain.Property.class.getName());
            createCache(cm, com.fastshop.domain.PropertyDes.class.getName());
            createCache(cm, com.fastshop.domain.ProductImage.class.getName());
            createCache(cm, com.fastshop.domain.Color.class.getName());
            createCache(cm, com.fastshop.domain.Color.class.getName() + ".products");
            createCache(cm, com.fastshop.domain.ProductInventory.class.getName());
            createCache(cm, com.fastshop.domain.Brand.class.getName());
            createCache(cm, com.fastshop.domain.Brand.class.getName() + ".products");
            createCache(cm, com.fastshop.domain.Category.class.getName());
            createCache(cm, com.fastshop.domain.Category.class.getName() + ".subCategories");
            createCache(cm, com.fastshop.domain.SubCategory.class.getName());
            createCache(cm, com.fastshop.domain.SubCategory.class.getName() + ".products");
            createCache(cm, com.fastshop.domain.ProductType.class.getName());
            createCache(cm, com.fastshop.domain.ProductType.class.getName() + ".categories");
            createCache(cm, com.fastshop.domain.Discount.class.getName());
            createCache(cm, com.fastshop.domain.ProductDiscount.class.getName());
            createCache(cm, com.fastshop.domain.Order.class.getName());
            createCache(cm, com.fastshop.domain.Order.class.getName() + ".orderItems");
            createCache(cm, com.fastshop.domain.OrderItem.class.getName());
            createCache(cm, com.fastshop.domain.Payment.class.getName());
            createCache(cm, com.fastshop.domain.UserAdress.class.getName());
            createCache(cm, com.fastshop.domain.UserCart.class.getName());
            createCache(cm, com.fastshop.domain.Favorite.class.getName());
            createCache(cm, com.fastshop.domain.Comment.class.getName());
            createCache(cm, com.fastshop.domain.ProductStatistics.class.getName());
            createCache(cm, com.fastshop.domain.SellerStatistics.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
