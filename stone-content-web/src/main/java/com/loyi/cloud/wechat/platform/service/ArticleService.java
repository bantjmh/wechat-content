package com.loyi.cloud.wechat.platform.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.procedure.ParameterMisuseException;
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

import com.alibaba.fastjson.JSON;
import com.loyi.cloud.wechat.platform.config.WechatProperties;
import com.loyi.cloud.wechat.platform.dao.ArticleGroupRelRepository;
import com.loyi.cloud.wechat.platform.dao.ArticleRepository;
import com.loyi.cloud.wechat.platform.dao.ArticleSendRecordRepository;
import com.loyi.cloud.wechat.platform.dao.AttachRepository;
import com.loyi.cloud.wechat.platform.entity.ArticleEntity;
import com.loyi.cloud.wechat.platform.entity.ArticleGroupRelEntity;
import com.loyi.cloud.wechat.platform.entity.ArticleSendRecordEntity;
import com.loyi.cloud.wechat.platform.entity.AttachEntity;
import com.loyi.cloud.wechat.platform.filter.ArticleFilter;
import com.loyi.cloud.wechat.platform.filter.ArticleFilterSpec;
import com.loyi.cloud.wechat.platform.model.Article;
import com.loyi.cloud.wechat.platform.web.ArticleController.BatchSend;

import weixin.popular.api.MediaAPI;
import weixin.popular.api.MessageAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;
import weixin.popular.bean.media.Media;
import weixin.popular.bean.media.MediaType;
import weixin.popular.bean.message.MessageSendResult;
import weixin.popular.bean.message.massmessage.Filter;
import weixin.popular.bean.message.massmessage.MassMPnewsMessage;
import weixin.popular.bean.message.massmessage.MassMessage;
import weixin.popular.bean.user.Tag;
import weixin.popular.bean.user.TagsGetResult;

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

	public void batchSend(BatchSend param) throws FileNotFoundException {
		String articleId = param.getArticleId();
		Article article = this.detail(articleId);
		if (article == null) {
			throw new ParameterMisuseException("文章不存在");
		}

		AttachEntity e = attachRepository.findOne(article.getMediaId());
		if (e == null) {
			throw new ParameterMisuseException("该文章未上传封面图片");
		}

		FileInputStream is = new FileInputStream(new File(properties.getFilePath() + File.separator + e.getFilename()));

		List<String> appIds = param.getAppId();
		for (String appId : appIds) {
			try {
				logger.warn("图文消息推送至 appId: {}", appId);
				Authorization_info authorization_info = authorizerTokenManager.get(appId);
				String access_token = authorization_info.getAuthorizer_access_token();

				// 第一步， 上传墙面图
				Media media = MediaAPI.mediaUpload(access_token, MediaType.image, is);
				logger.warn("上传封面图响应: {}", JSON.toJSONString(media));

				List<weixin.popular.bean.message.Article> articles = new ArrayList<>();
				weixin.popular.bean.message.Article ar = new weixin.popular.bean.message.Article();
				ar.setTitle(article.getTitle());
				ar.setThumb_media_id(media.getMedia_id());
				ar.setShow_cover_pic("1");
				ar.setThumb_url("");
				ar.setAuthor(article.getAuthor());
				ar.setDigest(article.getDigest());
				ar.setContent(article.getContent());
				ar.setUrl(article.getUrl());
				ar.setNeed_open_comment(1);
				ar.setOnly_fans_can_comment(0);
				articles.add(ar);

				// 第二步，上传图文素材
				Media mediaUploadnews = MessageAPI.mediaUploadnews(access_token, articles);
				logger.warn("上传图文素材响应: {}", JSON.toJSONString(mediaUploadnews));

				MassMessage mpnewsMessage = new MassMPnewsMessage(mediaUploadnews.getMedia_id());

				// 第三步，获取用户标签
				if (StringUtils.isNotBlank(param.getTag())) {
					TagsGetResult tagsGet = UserAPI.tagsGet(access_token);
					List<Tag> tags = tagsGet.getTags();
					logger.warn("获取到用户标签: {}", JSON.toJSONString(tags));
					for (Tag tag : tags) {
						if (tag.getName().equals(param.getTag())) {
							// 发送给指定标签用户
							mpnewsMessage.setFilter(new Filter(false, tag.getId()));
							break;
						}
					}
				} else {
					// 发送给所有用户
					mpnewsMessage.setFilter(new Filter(true, ""));
				}

				// 第四步，推送图文消息
				MessageSendResult messageMassSendall = MessageAPI.messageMassSendall(access_token, mpnewsMessage);
				logger.warn("消息推送结果: {}", JSON.toJSONString(messageMassSendall));

				ArticleSendRecordEntity record = new ArticleSendRecordEntity();
				record.setAppId(appId);
				record.setArticleId(articleId);
				record.setErrcode(messageMassSendall.getErrcode());
				record.setErrmsg(messageMassSendall.getErrmsg());
				record.setId(UUID.randomUUID().toString());
				record.setCreated(new Date());
				articleSendRecordRepository.save(record);
			} catch (Exception e1) {
				e1.printStackTrace();

				logger.error("消息推送失败 appId: {}", appId);
				logger.error("消息推送失败: ", e1);
			}

		}

	}

}
