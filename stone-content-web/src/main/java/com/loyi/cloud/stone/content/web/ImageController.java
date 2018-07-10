package com.loyi.cloud.stone.content.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import com.loyi.cloud.stone.content.entity.AttachEntity;
import com.loyi.cloud.stone.content.model.ServerResponse;
import com.loyi.cloud.stone.content.model.vo.ImageVo;

import com.loyi.stone.content.api.domain.Attach;
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

import com.loyi.cloud.stone.content.dao.AttachRepository;
import com.loyi.cloud.stone.content.service.AttachService;

@Controller
@RequestMapping(value = "image")
public class ImageController extends BaseController {

	@Autowired
	AttachRepository attachRepository;

	@Autowired
	AttachService attachService;


	@ResponseBody
	// @RequiresUser
	@PostMapping(value = "upload")
	public Map<String, String> upload(@RequestParam(value = "file", required = false) MultipartFile file)
			throws IOException {
		String uid = getLoginUID();
		String id = attachService.upload(file,uid);
		AttachEntity attachEntity = attachRepository.findOne(id);
		String fileName = attachEntity.getFilename();
		Map<String, String> result = new HashMap<String, String>();
		result.put("mediaId", id);
		result.put("filename",fileName);
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
