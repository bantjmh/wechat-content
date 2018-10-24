package com.bshuai.content.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bshuai.content.api.domain.Article;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class ArticleCache {
	private static Logger log = LoggerFactory.getLogger(ArticleCache.class);

	private LoadingCache<String, Article> articleCache = CacheBuilder.newBuilder().initialCapacity(1000)
			.maximumSize(10000).expireAfterAccess(30, TimeUnit.MINUTES).build(new CacheLoader<String, Article>() {
				@Override
				public Article load(String s) throws Exception {
					return null;
				}
			});

	public void saveArticle(String key, Article article) {
		articleCache.put(key, article);
	}

	public Article getArticle(String key) {
		Article article = null;
		try {
			article = articleCache.get(key);
			return article;
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
}
