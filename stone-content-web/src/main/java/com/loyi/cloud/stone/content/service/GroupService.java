package com.loyi.cloud.stone.content.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.loyi.cloud.stone.content.dao.GroupRepository;
import com.loyi.cloud.stone.content.filter.GroupFilter;
import com.loyi.cloud.stone.content.web.GroupController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.loyi.cloud.stone.content.dao.ArticleGroupRelRepository;
import com.loyi.cloud.stone.content.entity.ArticleGroupRelEntity;
import com.loyi.cloud.stone.content.entity.GroupEntity;
import com.loyi.cloud.stone.content.filter.GroupFilterSpec;
import com.loyi.cloud.stone.content.model.Group;

import javax.transaction.Transactional;


@Service
public class GroupService {

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private ArticleGroupRelRepository articleGroupRelRepository;

	public void create(Group group) {
		group.setCreated(new Date());
		group.setId(UUID.randomUUID().toString());
		groupRepository.save(this.toEntity(group));
	}

	public void modify(Group group) {
		GroupEntity e = groupRepository.findOne(group.getId());
		if (e == null) {
			return;
		}
		BeanUtils.copyProperties(group, e, "created");
		groupRepository.save(e);
	}

	public void remove(String id) {
		groupRepository.delete(id);
	}

	public Page<Group> search(GroupFilter filter, Pageable pageable) {
		Page<GroupEntity> page = groupRepository.findAll(new GroupFilterSpec(filter), pageable);
		List<GroupEntity> content = page.getContent();
		List<Group> list = new ArrayList<>();
		for (GroupEntity e : content) {
			list.add(this.toModel(e));
		}
		return new PageImpl<>(list, pageable, page.getTotalElements());
	}

	private Group toModel(GroupEntity e) {
		Group m = new Group();
		BeanUtils.copyProperties(e, m);
		return m;
	}

	private List<Group> toModel(List<GroupEntity> entities) {
		List<Group> result = new ArrayList<>();
		for (GroupEntity e : entities) {
			result.add(toModel(e));
		}
		return result;
	}

	private GroupEntity toEntity(Group Group) {
		GroupEntity e = new GroupEntity();
		BeanUtils.copyProperties(Group, e);
		return e;
	}

	public List<Group> selected(GroupFilter filter) {
		List<String> groupIds = this.selectedGroupIds(filter);
		if (CollectionUtils.isEmpty(groupIds)) {
			return null;
		}
		List<GroupEntity> entities = groupRepository.findAll(groupIds);
		return this.toModel(entities);
	}

	private List<String> selectedGroupIds(GroupFilter filter) {
		List<String> groupIds = new ArrayList<>();
	 	if (StringUtils.isNotBlank(filter.getArticleId())) {
			List<ArticleGroupRelEntity> es = articleGroupRelRepository.select(filter.getArticleId());
			for (ArticleGroupRelEntity e : es) {
				groupIds.add(e.getGroupId());
			}
		}
		return groupIds;
	}

//	public List<Group> unselected(GroupFilter filter) {
//		List<String> selectedGroupIds = this.selectedGroupIds(filter);
//		GroupFilterSpec spec = new GroupFilterSpec(filter);
//		spec.setNotInIds(selectedGroupIds);
//		List<GroupEntity> entities = groupRepository.findAll(spec);
//		return this.toModel(entities);
//	}

	@Transactional
	public void addRel(GroupController.RelAdd param) {
 		if (StringUtils.isNotBlank(param.getArticleId())) {
			articleGroupRelRepository.deleteByArticleId(param.getArticleId());
			List<ArticleGroupRelEntity> entities = new ArrayList<>();
			for (String groupId : param.getGroupIds()) {
				ArticleGroupRelEntity e = new ArticleGroupRelEntity(groupId, param.getArticleId());
				entities.add(e);
			}
			articleGroupRelRepository.save(entities);
		}
	}

}
