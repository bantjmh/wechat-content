package com.loyi.cloud.stone.content.filter;

import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import com.loyi.cloud.stone.content.entity.GroupEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

public class GroupFilterSpec implements Specification<GroupEntity> {

	private GroupFilter filter;

	private List<String> notInIds;

	public GroupFilterSpec(GroupFilter filter) {
		super();
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<GroupEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		Predicate predicate;
		predicate = cb.conjunction();
		if (filter == null) {
			return predicate;
		}
		if (CollectionUtils.isNotEmpty(notInIds)) {
			In<String> in = cb.in(root.<String>get("id"));
			Iterator<String> iterator = this.getNotInIds().iterator();
			while (iterator.hasNext()) {
				in.value(iterator.next());
			}
			predicate = cb.and(predicate, cb.not(in));
		}
		return predicate;

	}

	public List<String> getNotInIds() {
		return notInIds;
	}

	public void setNotInIds(List<String> notInIds) {
		this.notInIds = notInIds;
	}

}
