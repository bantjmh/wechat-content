package com.test.wechat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import weixin.popular.api.MaterialAPI;
import weixin.popular.api.MediaAPI;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.material.Description;
import weixin.popular.bean.media.Media;
import weixin.popular.bean.media.MediaType;
import weixin.popular.bean.media.UploadimgResult;
import weixin.popular.bean.message.MessageSendResult;
import weixin.popular.bean.message.massmessage.Filter;
import weixin.popular.bean.message.massmessage.MassMPnewsMessage;
import weixin.popular.bean.message.massmessage.MassMessage;

public class SendMediaTest {

	public static void main(String[] args) {

		// TODO 发送图文消息
		// Authorization_info authorization_info = authorizerTokenManager.get(appId);
		// String access_token = authorization_info.getAuthorizer_access_token();

		String access_token = "10_2_F50IrvCfD9SUsMWKbzKR5OA9HTyS4An9pIDEfFBYd1JTtXxxX9BVpGHxVGB1ULkPG5CZRVFHDJPY2N7ENOLgkJWjSGNs-sKSVPdpsIqqRqVXA_H3FkOPh5AYE5vBBRs8GVu6QtTww3lQYCAUEhAIAKEO";

		Media media = MediaAPI.mediaUpload(access_token, MediaType.image, new File("D:\\new.png"));
		// Media add_material = MaterialAPI.add_material(access_token, MediaType, new
		// File("D:\\new.png"),
		// new Description("title", "description"));
		//
		// String media_id = add_material.getMedia_id();
		System.out.println("media_id: " + media.getMedia_id());

		List<weixin.popular.bean.message.Article> articles = new ArrayList<>();
		weixin.popular.bean.message.Article ar = new weixin.popular.bean.message.Article();
		ar.setTitle("this is title");
		ar.setThumb_media_id(media.getMedia_id());
		ar.setShow_cover_pic("1");
		ar.setThumb_url("");
		ar.setAuthor("bshuai");
		ar.setDigest("this is digest");
		ar.setContent("this is content");
		ar.setUrl("url");
		ar.setContent("content_source_url");
		ar.setNeed_open_comment(1);
		ar.setOnly_fans_can_comment(0);
		articles.add(ar);

		System.out.println(JSON.toJSONString(articles));

		Media mediaUploadnews = MessageAPI.mediaUploadnews(access_token, articles);
		System.out.println("response media_id: " + mediaUploadnews.getMedia_id());

//		MassMessage massMessage = new MassMPnewsMessage(mediaUploadnews.getMedia_id());
//		MessageSendResult messageMassSendall = MessageAPI.messageMassSendall(access_token, massMessage);
//		System.out.println("mesg id: " + messageMassSendall.getMsg_id());

		Set<String> touser = new HashSet<>();
		touser.add("oti6RwozFrZOENiEj4_sAcNUZ9D0");
		MassMessage mpnewsMessage = new MassMPnewsMessage(mediaUploadnews.getMedia_id());
		//mpnewsMessage.setTouser(touser);
		mpnewsMessage.setFilter(new Filter(true, ""));
		System.out.println(JSON.toJSON(mpnewsMessage));
		MessageSendResult messageMassSendall = MessageAPI.messageMassSendall(access_token, mpnewsMessage);
		System.out.println("mesg id: " + messageMassSendall.getMsg_id());

	}
}
