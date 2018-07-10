package com.loyi.cloud.stone.content.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.loyi.cloud.stone.content.config.WechatProperties;
import com.loyi.cloud.stone.content.dao.ArticleSendRecordRepository;
import com.loyi.cloud.stone.content.entity.ArticleSendRecordEntity;
import com.loyi.cloud.stone.content.filter.ArticleFilter;
import com.loyi.cloud.stone.content.model.ServerResponse;
import com.loyi.cloud.stone.content.service.ArticleService;
import com.loyi.cloud.stone.content.service.AuthorizerTokenManager;
import com.loyi.stone.content.api.domain.Article;
import com.loyi.stone.content.api.domain.ArticleSendRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;

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

	@Autowired
	ArticleSendRecordRepository articleSendRecordRepository;

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
		String url = wechatProperties.getApiServerUrl();
		url = url + "/api/stone-content/article/view/" + id;
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

	@GetMapping(value = "detail")
	public Article detail(String articleId){
		Article article = articleService.detail(articleId);
		return article;
	}

	@PostMapping(value = "save/record")
	public void saveRecord(@RequestBody ArticleSendRecord articleSendRecord){
		ArticleSendRecordEntity entity = assemblyEntity(articleSendRecord);
		articleSendRecordRepository.save(entity);
	}

	@ResponseBody
	@RequiresUser
	@PostMapping(value = "remove")
	public void remove(@RequestBody Map<String, String> map) {
		articleService.remove(map.get("id"));
	}

	@RequiresUser
	@PostMapping(value = "create")
	public ServerResponse create(@RequestBody Article article) {
		article.setCreaterId(getLoginUID());
		article.setCreater(getLoginUname());

		if (StringUtils.isNotBlank(article.getId())) {
			this.modify(article);
		} else {
			articleService.add(article);
		}

		article.setContent("");
		article.setCreaterId("");
		return ServerResponse.createBySuccess(article);
	}

	@ResponseBody
	@RequiresUser
	@PostMapping(value = "modify")
	public void modify(@RequestBody Article article) {
		articleService.modify(article);
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

	private ArticleSendRecordEntity assemblyEntity(ArticleSendRecord record){
		ArticleSendRecordEntity entity = new ArticleSendRecordEntity();
		entity.setId(record.getId());
		entity.setAppId(record.getAppId());
		entity.setArticleId(record.getArticleId());
		entity.setCreated(record.getCreated());
		entity.setErrcode(record.getErrcode());
		entity.setErrmsg(record.getErrmsg());
		return entity;
	}
}
