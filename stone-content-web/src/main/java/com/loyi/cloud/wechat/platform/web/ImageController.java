package com.loyi.cloud.wechat.platform.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

	@ResponseBody
	// @RequiresUser
	@PostMapping(value = "upload")
	public Map<String, String> upload(@RequestParam(value = "file", required = false) MultipartFile file)
			throws IOException {

		String id = attachService.upload(file);

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

}
