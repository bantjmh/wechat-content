package com.bshuai.content.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

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

import com.bshuai.content.api.domain.Article;
import com.bshuai.content.config.WechatProperties;
import com.bshuai.content.dao.ArticleGroupRelRepository;
import com.bshuai.content.dao.ArticleRepository;
import com.bshuai.content.dao.ArticleSendRecordRepository;
import com.bshuai.content.dao.AttachRepository;
import com.bshuai.content.dao.DraftRepository;
import com.bshuai.content.entity.ArticleEntity;
import com.bshuai.content.entity.ArticleGroupRelEntity;
import com.bshuai.content.entity.DraftEntity;
import com.bshuai.content.filter.ArticleFilter;
import com.bshuai.content.filter.ArticleFilterSpec;
import com.bshuai.content.model.param.DraftParam;
import com.bshuai.content.model.vo.AuditVo;

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

	@Autowired
	DraftRepository draftRepository;

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
		String creater = e.getCreater();
		String createrId = e.getCreaterId();
		BeanUtils.copyProperties(ar, e, "created");
		e.setUpdated(new Date());
		e.setCheckStatus(1);
		e.setCreater(creater);
		e.setCreaterId(createrId);
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
			Article article = this.toModel(e);
			article.setContent("");
			article.setContentModel("");
			list.add(article);
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

	public DraftEntity selectDraftByUid(String uid){
		return draftRepository.findOne(uid);
	}

	public void deleteDraft(String uid){
		DraftEntity draftEntity = draftRepository.findOne(uid);
		if (draftEntity != null){
			draftRepository.delete(uid);
		}
	}

	public DraftEntity updateDraft(DraftParam param,String uid){
		DraftEntity draftEntity = draftRepository.findOne(uid);
		try {
			if (draftEntity != null){
				org.apache.commons.beanutils.BeanUtils.copyProperties(draftEntity,param);
				draftEntity.setUpdated(new Date());
				draftRepository.save(draftEntity);
			}else {
				saveDraft(param,uid);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return draftEntity;
	}

	public DraftEntity saveDraft(DraftParam param,String uid){

		DraftEntity draftEntity = new DraftEntity();
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(draftEntity,param);
			Date now = new Date();
			draftEntity.setCreated(now);
			draftEntity.setUpdated(now);
			draftEntity.setUid(uid);
			draftRepository.save(draftEntity);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return draftEntity;
	}



}
