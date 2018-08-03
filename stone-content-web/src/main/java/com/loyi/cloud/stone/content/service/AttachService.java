package com.loyi.cloud.stone.content.service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.loyi.cloud.stone.content.config.WechatProperties;
import com.loyi.cloud.stone.content.dao.AttachRepository;
import com.loyi.cloud.stone.content.entity.AttachEntity;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Decoder;

@Service
public class AttachService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    AttachRepository attachRepository;

	@Autowired
    WechatProperties properties;

	public AttachEntity uploadBase64(String base64,String uid){
		//默认png

		String id = UUID.randomUUID().toString();
		String filename = id+".png";
		checkFileExsit();
		String imagePath = generateImage(base64,filename);
        logger.info("genertate image success imagePath : {}",imagePath);
		AttachEntity entity = saveAttach(uid, id, filename);
		return entity;
	}

	public AttachEntity uploadThumbBase64(String base64,String uid,double scale){
		String id = UUID.randomUUID().toString();
		String filename = id+".png";
		checkFileExsit();
		String imagePath = null;
		try {
			imagePath = generateThumb(base64,filename,scale);
		} catch (IOException e) {
			e.printStackTrace();
		}
		filename = imagePath.substring(imagePath.lastIndexOf("/")+1);
		logger.info("genertate image success imagePath : {}",imagePath);
		AttachEntity entity = saveAttach(uid, id, filename);
		return entity;
	}

	public String generateThumb(String base64,String filename,double scale) throws IOException {
		String imagePath = generateImage(base64,filename);
		File formPic = new File(imagePath);
		String id = filename.substring(0,filename.lastIndexOf('.'));
		BufferedImage image = ImageIO.read(formPic);
		int imageWidth = image.getWidth();
		String toPicPath = properties.getFilePath() + File.separator+id+"_thumb.png";
		File toPic = new File(toPicPath);
		int width = imageWidth;
		int height = (int) (imageWidth * scale);
		Thumbnails.of(formPic).sourceRegion(Positions.CENTER,width,height).size(width,height).toFile(toPic);
		if (formPic.exists()){
			formPic.delete();
		}
		return toPicPath;
	}

	private void checkFileExsit() {
		File folder = new File(properties.getFilePath());
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	public String upload(MultipartFile file,String uid) throws IOException {

		String id = UUID.randomUUID().toString();
		String originalFilename = file.getOriginalFilename();
		String type = originalFilename.substring(originalFilename.lastIndexOf("."));
		String filename = id + type;
		checkFileExsit();

		File target = new File(properties.getFilePath() + File.separator + filename);

		if (file.getSize() > 512 * 1024) {// 如果图片大于0.5M,压缩处理
			Thumbnails.of(file.getInputStream()).scale(1f).outputQuality(0.25f).toFile(target);
		} else {
			FileOutputStream os = new FileOutputStream(target);
			FileCopyUtils.copy(file.getInputStream(), os);
		}

		saveAttach(uid, id, filename);

		return id;
	}

	private AttachEntity saveAttach(String uid, String id, String filename) {
		AttachEntity e = new AttachEntity();
		e.setId(id);
		e.setFilename(filename);
		e.setUid(uid);

		attachRepository.save(e);
		return e;
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

	//base64字符串转化成图片
	public String generateImage(String imgStr,String filename)
	{   //对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) //图像数据为空
			return "";
		BASE64Decoder decoder = new BASE64Decoder();
		try
		{
			//Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for(int i=0;i<b.length;++i)
			{
				if(b[i]<0)
				{//调整异常数据
					b[i]+=256;
				}
			}
			//生成jpeg图片
			String imgFilePath = properties.getFilePath() + File.separator+ filename;//新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return imgFilePath;
		}
		catch (Exception e)
		{
			return "";
		}
	}
}
