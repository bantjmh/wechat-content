package com.loyi.cloud.stone.content.service;

import java.util.*;

import javax.transaction.Transactional;

import com.loyi.cloud.stone.content.dao.ArticleRepository;
import com.loyi.cloud.stone.content.filter.ArticleFilter;
import com.loyi.stone.content.api.domain.Article;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.loyi.cloud.stone.content.config.WechatProperties;
import com.loyi.cloud.stone.content.dao.ArticleGroupRelRepository;
import com.loyi.cloud.stone.content.dao.ArticleSendRecordRepository;
import com.loyi.cloud.stone.content.dao.AttachRepository;
import com.loyi.cloud.stone.content.entity.ArticleEntity;
import com.loyi.cloud.stone.content.entity.ArticleGroupRelEntity;
import com.loyi.cloud.stone.content.filter.ArticleFilterSpec;
import com.loyi.cloud.stone.content.model.vo.AuditVo;

@Service
public class ArticleService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	AttachRepository attachRepository;

	@Autowired
	WechatProperties properties;

	@Autowired
	ArticleGroupRelRepository articleGroupRelRepository;

	@Autowired
	AuthorizerTokenManager authorizerTokenManager;

	@Autowired
	ArticleSendRecordRepository articleSendRecordRepository;

	public void add(Article ar) {
		ar.setCreated(new Date());
		ar.setUpdated(new Date());
		ar.setId(UUID.randomUUID().toString());
		articleRepository.save(this.toEntity(ar));
	}

	public void modify(Article ar) {
		ArticleEntity e = articleRepository.findOne(ar.getId());
		if (e == null) {
			return;
		}
		BeanUtils.copyProperties(ar, e, "created");
		e.setUpdated(new Date());
		articleRepository.save(e);
	}

	public void remove(String id) {
		articleRepository.delete(id);
	}

	public Page<Article> search(ArticleFilter filter, Pageable pageable) {

		ArticleFilterSpec spec = new ArticleFilterSpec(filter);

		if (StringUtils.isNotBlank(filter.getGroupId())) {
			List<ArticleGroupRelEntity> list = articleGroupRelRepository.selectByGroupId(filter.getGroupId());
			if (CollectionUtils.isEmpty(list)) {
				spec.add("xxxxxxx");
			} else {
				for (ArticleGroupRelEntity e : list) {
					spec.add(e.getArticleId());
				}
			}
		}

		Sort sort = pageable.getSort();
		if (sort == null) {
			pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Direction.DESC, "created");
		}
		Page<ArticleEntity> page = articleRepository.findAll(spec, pageable);
		List<ArticleEntity> content = page.getContent();
		List<Article> list = new ArrayList<>();
		for (ArticleEntity e : content) {
			e.setCreaterId("");
			list.add(this.toModel(e));
		}
		return new PageImpl<>(list, pageable, page.getTotalElements());
	}

	private Article toModel(ArticleEntity e) {
		Article m = new Article();
		BeanUtils.copyProperties(e, m);
		return m;
	}

	private ArticleEntity toEntity(Article ar) {
		ArticleEntity e = new ArticleEntity();
		BeanUtils.copyProperties(ar, e);
		return e;
	}

	public Article detail(String id) {
		ArticleEntity detail = articleRepository.findOne(id);
		if (detail != null) {
			return this.toModel(detail);
		}
		return null;
	}

	@Transactional
	public void update(AuditVo param) {
		articleRepository.updateStatus(param.getId(), param.getCheckStatus());
	}


}
