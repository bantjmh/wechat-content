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

public class SendMediaTest3 {

	public static void main(String[] args) {

		String access_token = "10_2_F50IrvCfD9SUsMWKbzKR5OA9HTyS4An9pIDEfFBYd1JTtXxxX9BVpGHxVGB1ULkPG5CZRVFHDJPY2N7ENOLgkJWjSGNs-sKSVPdpsIqqRqVXA_H3FkOPh5AYE5vBBRs8GVu6QtTww3lQYCAUEhAIAKEO";

		MessageSendResult messageMassGet = MessageAPI.messageMassGet(access_token, "1000000006");
		System.out.println(JSON.toJSONString(messageMassGet));
		

	}
}
