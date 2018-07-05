package com.loyi.cloud.wechat.platform.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.loyi.cloud.wechat.platform.dao.ImageRepository;
import com.loyi.cloud.wechat.platform.entity.ImageEntity;
import com.loyi.cloud.wechat.platform.model.ServerResponse;
import com.loyi.cloud.wechat.platform.model.vo.ImageVo;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.loyi.cloud.wechat.platform.dao.AttachRepository;
import com.loyi.cloud.wechat.platform.service.AttachService;

@Controller
@RequestMapping(value = "image")
public class ImageController extends BaseController {

	@Autowired
	AttachRepository attachRepository;

	@Autowired
	AttachService attachService;

	@Autowired
	ImageRepository imageRepository;

	@ResponseBody
	// @RequiresUser
	@PostMapping(value = "upload")
	public Map<String, String> upload(@RequestParam(value = "file", required = false) MultipartFile file)
			throws IOException {
		String id = attachService.upload(file);
		String uid = getLoginUID();
		ImageEntity imageEntity = new ImageEntity();
		imageEntity.setMediaId(id);
		imageEntity.setUid(uid);
		imageRepository.save(imageEntity);

		Map<String, String> result = new HashMap<String, String>();
		result.put("mediaId", id);
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
		List<ImageEntity> imageEntityList = imageRepository.selectByUid(uid);
		String serverUrl = wechatProperties.getServerUrl();
		List<ImageVo> imageVoList = new LinkedList<>();
		imageEntityList.forEach(imageEntity -> {
			ImageVo imageVo = new ImageVo();
			imageVo.setMediaId(imageEntity.getMediaId());
			String imageUrl = serverUrl+"/api/stone-content/image/view?mediaId="+imageEntity.getMediaId();
			imageVo.setImageUrl(imageUrl);
			imageVoList.add(imageVo);
		});
		logger.info("image list response success  data : {}",imageVoList);
		return ServerResponse.createBySuccess(imageVoList);
	}

}
