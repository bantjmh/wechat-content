package com.loyi.cloud.stone.content.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.servlet.http.HttpServletResponse;


import com.loyi.cloud.stone.content.entity.AttachEntity;
import com.loyi.cloud.stone.content.model.ServerResponse;
import com.loyi.cloud.stone.content.model.param.UploadParam;
import com.loyi.cloud.stone.content.model.param.UploadThumbParam;
import com.loyi.cloud.stone.content.model.vo.ImageVo;

import com.loyi.cloud.wecaht.platform.domain.WechatImageMessage;
import com.loyi.cloud.wechat.platform.sdk.client.IBatchFeignService;
import com.loyi.stone.content.api.domain.Attach;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.loyi.cloud.stone.content.dao.AttachRepository;
import com.loyi.cloud.stone.content.service.AttachService;
import sun.misc.BASE64Decoder;

@Controller
@RequestMapping(value = "image")
public class ImageController extends BaseController {

	@Autowired
	AttachService attachService;

	@Autowired
	IBatchFeignService iBatchFeignService;

	@ResponseBody
	// @RequiresUser
	@PostMapping(value = "upload")
	public Map<String, String> upload(@RequestParam(value = "file", required = false) MultipartFile file,String appId)
			throws IOException {
		String uid = getLoginUID();
		String id = attachService.upload(file,uid);
		AttachEntity attachEntity = attachRepository.findOne(id);
		String fileName = attachEntity.getFilename();
		Map<String, String> result = new HashMap<String, String>();
		result.put("mediaId", id);
		result.put("filename",fileName);

//		WechatImageMessage wechatImageMessage = iBatchFeignService.upImageUpload(fileName,appId);
//		logger.info("从微信模块获取图片在微信服务器的url: {}",wechatImageMessage.getWechatImageUrl());
//
//		result.put("wechat_url",wechatImageMessage.getWechatImageUrl());
		return result;
	}

	@ResponseBody
	@RequiresUser
	@PostMapping(value = "remove")
	public void remove(@RequestBody Map<String, String> param) throws IOException {
		attachService.remove(param.get("mediaId"));
	}

	@GetMapping("view")
	public void imageView(String mediaId, HttpServletResponse response) throws IOException {
		attachService.get(mediaId, response);
	}

	@GetMapping("list")
	public ServerResponse imageList(){
		logger.info("receive image list request");
		String uid = getLoginUID();
		logger.info("uid:{}",uid);
		List<AttachEntity> imageEntityList = attachRepository.selectByUid(uid);
		String serverUrl = wechatProperties.getImageServerUrl();
		List<ImageVo> imageVoList = new LinkedList<>();
		imageEntityList.forEach(imageEntity -> {
			ImageVo imageVo = new ImageVo();
			imageVo.setMediaId(imageEntity.getId());
			String imageUrl = serverUrl+"/"+imageEntity.getFilename();
			imageVo.setImageUrl(imageUrl);
			imageVo.setFilename(imageEntity.getFilename());
			imageVoList.add(imageVo);
		});
		logger.info("image list response success  data : {}",imageVoList);
		return ServerResponse.createBySuccess(imageVoList);
	}

	@GetMapping("detail")
	public Attach detail(String mediaId){
		AttachEntity entity = attachRepository.findOne(mediaId);
		return asseamblyAttach(entity);
	}

	/**
	 * 不经过前端裁剪工具裁剪上传内容图片
	 * @param param
	 * @return
	 */
	@PostMapping(value = "upload/base64")
	public Map<String, String> upLoadImage(@RequestBody UploadParam param){
//		System.out.print(param.getBaseString());
		String uid = getLoginUID();
        String baseStr = getUsefulBase64(param);
        String imageType = "png";
        if (StringUtils.isNotBlank(param.getImageType())){
			imageType = param.getImageType();
		}
        AttachEntity attachEntity = attachService.uploadBase64(baseStr,uid,imageType);
        Map<String, String> result = getResultMap(attachEntity);
		return result;
	}

	/**
	 * 不经过前端裁剪工具裁剪上传封面图片，服务器会进行一次裁剪
	 * @param param
	 * @return
	 */
    @PostMapping(value = "upload/thumbase64")
    public Map<String,String> uploadThumb(@RequestBody UploadThumbParam param){
        String uid = getLoginUID();
        String baseStr = getUsefulBase64(param);
        AttachEntity attachEntity = attachService.uploadThumbBase64(baseStr,uid,param.getScale());
        Map<String, String> result = getResultMap(attachEntity);
        return result;
    }

	/**
	 * 经过裁剪工具裁剪上传图片的接口，区别主要在于上传的base64字符串的类型不同
	 * @param param
	 * @return
	 */
	@RequiresUser
	@PostMapping(value = "upload/tailored/thumb")
	public Map<String,String> uploadTailoredThumb(@RequestBody UploadParam param){
		String uid = getLoginUID();
		String baseStr = param.getBaseString();
		String imageType = "png";
		if (StringUtils.isNotBlank(param.getImageType())){
			imageType = param.getImageType();
		}
		AttachEntity attachEntity = attachService.uploadBase64(baseStr,uid,imageType);
		Map<String, String> result = getResultMap(attachEntity);
		return result;
	}

    @GetMapping(value = "base64")
	public Map<String,String> getBase64StringBy(String mediaId){
	    if (StringUtils.isBlank(mediaId)){
	        throw new RuntimeException("mediaId 不能为空");
        }
        String base64Str = attachService.getBase64StringBy(mediaId);
        Map<String,String> result = new HashMap<>();
        result.put("base64",base64Str);
        result.put("mediaId",mediaId);
        return result;
    }

    @GetMapping(value = "tailored")
    public Map<String,String> tailoredImage(@RequestParam String attachId,@RequestParam double scale) throws IOException {
        String uid = getLoginUID();
        AttachEntity attachEntity = attachService.tailoredImage(attachId,uid,scale);
        Map<String, String> result = getResultMap(attachEntity);
        return result;
    }



    private Map<String, String> getResultMap(AttachEntity attachEntity) {
        Map<String,String> result = new HashMap<>();
        result.put("attachId",attachEntity.getId());
        result.put("filename",attachEntity.getFilename());
        result.put("imageurl",wechatProperties.getImageServerUrl()+"/"+attachEntity.getFilename());
        return result;
    }

    private String getUsefulBase64(@RequestBody UploadParam param) {
        String baseStr = param.getBaseString();
        if (!param.getBaseString().startsWith("/9j")){
            baseStr = baseStr.substring(baseStr.indexOf("/9j"));
        }
        return baseStr;
    }

	private Attach asseamblyAttach(AttachEntity attachEntity){
		Attach attach = new Attach();
		attach.setId(attachEntity.getId());
		attach.setUid(attachEntity.getUid());
		attach.setUploadtime(attachEntity.getUploadtime());
		attach.setFilename(attachEntity.getFilename());
		attach.setType(attachEntity.getType());
		attach.setWidth(attachEntity.getWidth());
		attach.setHeight(attachEntity.getHeight());
		attach.setAperture(attachEntity.getAperture());
		attach.setFocusLength(attachEntity.getFocusLength());
		attach.setMimeType(attachEntity.getMimeType());
		attach.setIso(attachEntity.getIso());
		attach.setCreatedTime(attachEntity.getCreatedTime());
		attach.setFlash(attachEntity.getFlash());
		attach.setCompression(attachEntity.getCompression());
		attach.setModel(attachEntity.getModel());
		return attach;
	}
}
