package com.bshuai.content.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.bshuai.content.entity.ArticleEntity;

public class ArticleFilterSpec implements Specification<ArticleEntity> {

	private ArticleFilter filter;

	private List<String> articleIds;

	public ArticleFilterSpec(ArticleFilter filter) {
		super();
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<ArticleEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		Predicate predicate;
		predicate = cb.conjunction();
		if (filter == null) {
			return predicate;
		}
		if (StringUtils.isNotBlank(filter.getKeyword())) {
			predicate = cb.and(predicate,
					cb.or(cb.like(root.get("title").as(String.class), "%" + filter.getKeyword() + "%"),
							cb.like(root.get("author").as(String.class), "%" + filter.getKeyword() + "%"),
							cb.like(root.get("digest").as(String.class), "%" + filter.getKeyword() + "%")));
		}

		if (CollectionUtils.isNotEmpty(this.getArticleIds())) {
			predicate = cb.and(predicate, root.get("id").in(this.getArticleIds()));
		}
		if(filter.getCheckStatus() != 0){
//			predicate = cb.and(predicate,cb.equal(root.get("checkStatus").as(Integer.class),filter.getCheckStatus()));
			predicate = cb.and(predicate,cb.greaterThanOrEqualTo(root.get("checkStatus").as(Integer.class),filter.getCheckStatus()));
		}
		if(filter.isMine()){
			predicate = cb.and(predicate,cb.equal(root.get("createrId").as(String.class),filter.getUid()));
		}
		return predicate;

	}

	public List<String> getArticleIds() {
		return articleIds;
	}

	public void setArticleIds(List<String> articleIds) {
		this.articleIds = articleIds;
	}

	public void add(String articleId) {
		if (this.articleIds == null) {
			this.articleIds = new ArrayList<>();
		}
		this.articleIds.add(articleId);
	}

}
