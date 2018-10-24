package com.bshuai.content.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.bshuai.content.api.domain.Article;
import com.bshuai.content.api.domain.ArticleSendRecord;
import com.bshuai.content.config.WechatProperties;
import com.bshuai.content.dao.ArticleSendRecordRepository;
import com.bshuai.content.dao.AttachRepository;
import com.bshuai.content.entity.ArticleSendRecordEntity;
import com.bshuai.content.entity.AttachEntity;
import com.bshuai.content.entity.DraftEntity;
import com.bshuai.content.filter.ArticleFilter;
import com.bshuai.content.model.ServerResponse;
import com.bshuai.content.model.param.DraftParam;
import com.bshuai.content.model.vo.AuditVo;
import com.bshuai.content.service.ArticleCache;
import com.bshuai.content.service.ArticleService;
import com.bshuai.content.service.AuthorizerTokenManager;
import com.bshuai.content.web.ArticleController.Preview;

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

	@Autowired
	ArticleCache articleCache;

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

	@PostMapping(value = "create/preview")
	public Preview preCreatePreview(@RequestBody @Validated Article article) {
		Preview preview = new Preview();
		String url = wechatProperties.getApiServerUrl();
		String articleKey = UUID.randomUUID().toString();
		articleCache.saveArticle(articleKey, article);
		url = url + "/api/stone-content/article/create/view/" + articleKey;
		preview.setUrl(url);
		preview.setKey(articleKey);
		return preview;
	}

	@GetMapping(value = "create/view/{key}")
	public void preCreateView(@PathVariable(name = "key") String key, HttpServletResponse response) throws IOException,
			TemplateException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		Template template = configuration.getTemplate("preview.html");
		Article article = articleCache.getArticle(key);
		if (article == null) {
			return;
		}
		Map<String, String> param = org.apache.commons.beanutils.BeanUtils.describe(article);
		param.put("created", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		response.setContentType("text/html; charset=utf-8");
		template.process(param, response.getWriter());
//		Map<String,String> param = new HashMap<>();
//		param.put("created",DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
//		param.put("title",article.getTitle());
//		param.put("author",article.getAuthor());
//		param.put("content",article.getContent());
//		Configuration configuration = freeMarkerConfigurer.getConfiguration();
//		Template template = configuration.getTemplate("preview.html");
//		response.setContentType("text/html; charset=utf-8");
//		template.process(param, response.getWriter());
	}

	@GetMapping(value = "detail")
	public Article detail(String articleId) {
		Article article = articleService.detail(articleId);
		AttachEntity attachEntity = attachRepository.findOne(article.getMediaId());
		article.setThumbImageFileName(attachEntity.getFilename());
		return article;
	}

	@Deprecated
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
		saveArticle(article);
		article.setContent("");
		article.setCreaterId("");
		return ServerResponse.createBySuccess(article);
	}

	@GetMapping(value = "create/key")
	public void createByKey(String articleKey) {
		Article article = articleCache.getArticle(articleKey);
		if (article == null) {
			throw new RuntimeException("预览时间超过30分钟");
		}
		saveArticle(article);
	}

	public void saveArticle(Article article) {
		article.setCreaterId(getLoginUID());
		article.setCreater(getLoginUname());
		article.setCheckStatus(1);
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
	}

	@RequiresUser
	@PostMapping(value = "modify")
	public void modify(@RequestBody Article article) {
		if (StringUtils.isBlank(article.getId())) {
			throw new RuntimeException("Id不能为空");
		}
		articleService.modify(article);
	}

	@ApiOperation(value = "审核")
	@RequiresUser
	@RequiresRoles(value = { "ADMIN" })
	@PostMapping(value = "audit")
	public void audit(@RequestBody AuditVo param) {
		articleService.update(param);
	}

	@RequiresUser
	@PostMapping(value = "save/draft")
	public DraftEntity saveDraft(@RequestBody @Validated DraftParam param) {
		String uid = getLoginUID();
		return articleService.saveDraft(param, uid);
	}

	@RequiresUser
	@GetMapping(value = "delete/draft")
	public void deleteDraft() {
		String uid = getLoginUID();
		articleService.deleteDraft(uid);
	}

	@RequiresUser
	@PostMapping(value = "update/draft")
	public void updateDraft(@RequestBody @Validated DraftParam param) {
		String uid = getLoginUID();
		articleService.updateDraft(param, uid);
	}

	@RequiresUser
	@GetMapping(value = "draft")
	public DraftEntity getDraft() {
		String uid = getLoginUID();
		DraftEntity draftEntity = articleService.selectDraftByUid(uid);
		return draftEntity;
	}

	public static class Preview {
		private String url;
		private String key;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

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
