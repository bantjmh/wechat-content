package com.loyi.cloud.stone.content.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.loyi.cloud.stone.content.config.WechatProperties;
import com.loyi.cloud.stone.content.dao.AttachRepository;
import com.loyi.cloud.stone.content.entity.AttachEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class AttachService {

	@Autowired
    AttachRepository attachRepository;

	@Autowired
    WechatProperties properties;

	public String upload(MultipartFile file,String uid) throws IOException {

		String id = UUID.randomUUID().toString();
		String originalFilename = file.getOriginalFilename();
		String type = originalFilename.substring(originalFilename.lastIndexOf("."));
		String filename = id + type;
		File folder = new File(properties.getFilePath());
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File target = new File(properties.getFilePath() + File.separator + filename);

		if (file.getSize() > 512 * 1024) {// 如果图片大于0.5M,压缩处理
			Thumbnails.of(file.getInputStream()).scale(1f).outputQuality(0.25f).toFile(target);
		} else {
			FileOutputStream os = new FileOutputStream(target);
			FileCopyUtils.copy(file.getInputStream(), os);
		}

		AttachEntity e = new AttachEntity();
		e.setId(id);
		e.setFilename(filename);
		e.setUid(uid);

		attachRepository.save(e);

		return id;
	}

	public void get(String code, HttpServletResponse response) throws IOException {
		AttachEntity e = attachRepository.findOne(code);
		if (e == null) {
			return;
		}

		FileInputStream is = new FileInputStream(new File(properties.getFilePath() + File.separator + e.getFilename()));

		FileCopyUtils.copy(is, response.getOutputStream());
	}

	public void remove(String id) {
		AttachEntity e = attachRepository.findOne(id);
		try {
			File file = new File(properties.getFilePath() + File.separator + e.getFilename());
			file.delete();
			attachRepository.delete(e.getId());
		} catch (Exception e1) {
		}
	}


}
