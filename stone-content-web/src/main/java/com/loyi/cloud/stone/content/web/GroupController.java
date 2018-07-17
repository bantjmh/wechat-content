package com.loyi.cloud.stone.content.web;

import java.util.List;
import java.util.Map;

import com.loyi.cloud.stone.content.filter.GroupFilter;
import com.loyi.cloud.stone.content.model.Group;
import com.loyi.cloud.stone.content.service.GroupService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(description = "分组管理")
@RestController
@RequestMapping(value = "group")
public class GroupController {

	@Autowired
    GroupService groupService;

	@RequiresUser
	@GetMapping(value = "search")
	public Page<Group> search(GroupFilter filter, Pageable pageable) {
		Page<Group> groups = groupService.search(filter, pageable);
		return groups;
	}

	/**
	 * 查询已选择的标签
	 */
	@RequiresUser
	@GetMapping(value = "selected")
	public List<Group> selected(GroupFilter filter) {
		return groupService.selected(filter);
	}

	/**
	 * 查询未选择的标签
	 */
	@RequiresUser
	@GetMapping(value = "unselected")
	public List<Group> unselected(GroupFilter filter) {
		return groupService.unselected(filter);
	}
	
	/**
	 * 给公众号或文章添加分组
	 */
	@RequiresUser
	@PostMapping(value = "add")
	public void add(@RequestBody RelAdd param) {
		groupService.addRel(param);
	}

	@RequiresUser
	@PostMapping(value = "remove")
	public void remove(@RequestBody Map<String, String> map) {
		groupService.remove(map.get("id"));
	}

	@RequiresUser
	@PostMapping(value = "create")
	public void create(@RequestBody Group group) {
		if (StringUtils.isNotBlank(group.getId())) {
			groupService.modify(group);
		} else {
			groupService.create(group);
		}
	}

	@RequiresUser
	@PostMapping(value = "modify")
	public void modify(@RequestBody Group group) {
		groupService.modify(group);
	}

	public static class RelAdd {
		private String articleId;

		private List<String> groupIds;

		public String getArticleId() {
			return articleId;
		}

		public void setArticleId(String articleId) {
			this.articleId = articleId;
		}

		public List<String> getGroupIds() {
			return groupIds;
		}

		public void setGroupIds(List<String> groupIds) {
			this.groupIds = groupIds;
		}

	}

}
