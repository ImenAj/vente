package com.projet.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.projet.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.projet.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(com.projet.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Article.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Sous_famille.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Famille.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Document_article.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Client.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Fournisseur.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Fournisseur.class.getName() + ".ns", jcacheConfiguration);
            cm.createCache(com.projet.domain.Mode_de_paiment.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Condition_de_reglement.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Commande.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Depense.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Operation.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Compte.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.SousFamilleDepense.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.FamilleDepense.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Bons_de_Reception.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Reglement.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Avoirs.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Facture.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Devis.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.CommandeClient.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Bons_de_livrison.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Bons_de_commande.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Bons_de_livraison.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Facture_achat.class.getName(), jcacheConfiguration);
            cm.createCache(com.projet.domain.Facture.class.getName() + ".ns", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
