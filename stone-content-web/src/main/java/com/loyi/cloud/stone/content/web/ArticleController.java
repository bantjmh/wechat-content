package com.loyi.cloud.stone.content.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.loyi.cloud.stone.content.dao.AttachRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.loyi.cloud.stone.content.config.WechatProperties;
import com.loyi.cloud.stone.content.dao.ArticleSendRecordRepository;
import com.loyi.cloud.stone.content.entity.ArticleSendRecordEntity;
import com.loyi.cloud.stone.content.entity.AttachEntity;
import com.loyi.cloud.stone.content.filter.ArticleFilter;
import com.loyi.cloud.stone.content.model.ServerResponse;
import com.loyi.cloud.stone.content.model.vo.AuditVo;
import com.loyi.cloud.stone.content.service.ArticleService;
import com.loyi.cloud.stone.content.service.AuthorizerTokenManager;
import com.loyi.stone.content.api.domain.Article;
import com.loyi.stone.content.api.domain.ArticleSendRecord;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "文章管理接口")
@RestController
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

	@Autowired
	AttachRepository attachRepository;

	@RequiresUser
	@GetMapping(value = "search")
	public Page<Article> search(ArticleFilter filter, Pageable pageable) {
		logger.info("receive article search request filter: {}");
		if (filter.isMine()) {
			filter.setUid(getLoginUID());
		}
		return articleService.search(filter, pageable);
	}

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
			throws IOException, TemplateException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		Template template = configuration.getTemplate("preview.html");

		Article article = articleService.detail(id);
		Map<String, String> param = org.apache.commons.beanutils.BeanUtils.describe(article);
		param.put("created", DateFormatUtils.format(article.getCreated(), "yyyy-MM-dd"));

		response.setContentType("text/html; charset=utf-8");
		template.process(param, response.getWriter());
	}

	@GetMapping(value = "detail")
	public Article detail(String articleId) {
		Article article = articleService.detail(articleId);
		AttachEntity attachEntity = attachRepository.findOne(article.getMediaId());
		article.setThumbImageFileName(attachEntity.getFilename());
		return article;
	}

	@PostMapping(value = "save/record")
	public void saveRecord(@RequestBody ArticleSendRecord articleSendRecord) {
		ArticleSendRecordEntity entity = assemblyEntity(articleSendRecord);
		articleSendRecordRepository.save(entity);
	}

	@RequiresUser
	@PostMapping(value = "remove")
	public void remove(@RequestBody Map<String, String> map) {
		articleService.remove(map.get("id"));
	}

	@RequiresUser
	@PostMapping(value = "create")
	public ServerResponse create(@RequestBody @Validated Article article) {
		article.setCreaterId(getLoginUID());
		article.setCreater(getLoginUname());
		if (StringUtils.isBlank(article.getThumbUrl()) && StringUtils.isNotBlank(article.getMediaId())) {
			logger.info("generate thumbUrl by mediaId");
			AttachEntity attachEntity = attachRepository.findOne(article.getMediaId());
			String filename = attachEntity.getFilename();
			String thumbUrl = wechatProperties.getImageServerUrl() + "/" + filename;
			article.setThumbUrl(thumbUrl);
		}
		if (StringUtils.isNotBlank(article.getId())) {
			this.modify(article);
		} else {
			articleService.add(article);
		}

		article.setContent("");
		article.setCreaterId("");
		return ServerResponse.createBySuccess(article);
	}

	@RequiresUser
	@PostMapping(value = "modify")
	public void modify(@RequestBody Article article) {
		articleService.modify(article);
	}

	@ApiOperation(value = "审核")
	@RequiresUser
	@RequiresRoles(value = { "ADMIN" })
	@PostMapping(value = "audit")
	public void audit(@RequestBody AuditVo param) {
		articleService.update(param);
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

	private ArticleSendRecordEntity assemblyEntity(ArticleSendRecord record) {
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
