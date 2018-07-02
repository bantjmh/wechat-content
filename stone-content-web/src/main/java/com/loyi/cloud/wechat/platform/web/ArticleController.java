package com.loyi.cloud.wechat.platform.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.loyi.cloud.wechat.platform.config.WechatProperties;
import com.loyi.cloud.wechat.platform.filter.ArticleFilter;
import com.loyi.cloud.wechat.platform.model.Article;
import com.loyi.cloud.wechat.platform.service.ArticleService;
import com.loyi.cloud.wechat.platform.service.AuthorizerTokenManager;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

@Api(description = "文章管理接口")
@Controller
@RequestMapping(value = "article")
public class ArticleController extends BaseController {

	@Autowired
	ArticleService articleService;

	@Autowired
	WechatProperties wechatProperties;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired
	AuthorizerTokenManager authorizerTokenManager;

	@ResponseBody
	@RequiresUser
	@GetMapping(value = "search")
	public Page<Article> search(ArticleFilter filter, Pageable pageable) {
		return articleService.search(filter, pageable);
	}

	@ResponseBody
	@RequiresUser
	@GetMapping(value = "preview")
	public Preview preview(String id) {
		Preview preview = new Preview();
		String url = wechatProperties.getServerUrl();
		url = url + "/article/view/" + id;
		preview.setUrl(url);
		return preview;
	}

	@GetMapping(value = "view/{id}")
	public void view(@PathVariable(name = "id") String id, HttpServletRequest request, HttpServletResponse response)
			throws IOException, TemplateException {

		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		Template template = configuration.getTemplate("preview.html");

		Article article = articleService.detail(id);

		response.setContentType("text/html; charset=utf-8");
		template.process(article, response.getWriter());
	}

	@ResponseBody
	@RequiresUser
	@PostMapping(value = "remove")
	public void remove(@RequestBody Map<String, String> map) {
		articleService.remove(map.get("id"));
	}

	@RequiresUser
	@PostMapping(value = "create")
	public Article create(@RequestBody Article article) {
		article.setCreaterId(getLoginUID());
		article.setCreater(getLoginUname());

		if (StringUtils.isNotBlank(article.getId())) {
			this.modify(article);
		} else {
			articleService.add(article);
		}

		article.setContent("");
		article.setCreaterId("");
		return article;
	}

	@ResponseBody
	@RequiresUser
	@PostMapping(value = "modify")
	public void modify(@RequestBody Article article) {
		articleService.modify(article);
	}

	@Async
	@ResponseBody
	@RequiresUser
	@PostMapping(value = "send")
	public void send(@RequestBody BatchSend param) throws FileNotFoundException {

		articleService.batchSend(param);
	}

	public static class BatchSend {
		private String articleId;
		private List<String> appId;
		private List<String> groupId;
		@ApiModelProperty(value = "用户标签名称")
		private String tag;

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getArticleId() {
			return articleId;
		}

		public void setArticleId(String articleId) {
			this.articleId = articleId;
		}

		public List<String> getAppId() {
			return appId;
		}

		public void setAppId(List<String> appId) {
			this.appId = appId;
		}

		public List<String> getGroupId() {
			return groupId;
		}

		public void setGroupId(List<String> groupId) {
			this.groupId = groupId;
		}

	}

	public static class Preview {
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

}
