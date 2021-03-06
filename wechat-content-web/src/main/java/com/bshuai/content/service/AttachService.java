package com.bshuai.content.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bshuai.content.config.WechatProperties;
import com.bshuai.content.dao.AttachRepository;
import com.bshuai.content.entity.AttachEntity;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Service
public class AttachService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    AttachRepository attachRepository;

	@Autowired
    WechatProperties properties;

	public AttachEntity uploadBase64(String base64,String uid,String imageType){
		//默认png

		String id = UUID.randomUUID().toString();
		String filename = id+"."+imageType;
		checkFileExsit();
		String imagePath = generateImage(base64,filename);
        logger.info("genertate image success imagePath : {}",imagePath);
		AttachEntity entity = saveAttach(uid, id, filename);
		return entity;
	}


	public String getBase64StringBy(String mediaId){
		AttachEntity attachEntity = attachRepository.findOne(mediaId);
		String filename = attachEntity.getFilename();
		String filePath = properties.getFilePath()+File.separator + filename;
		String imageStr = getImageStr(filePath);
		return imageStr;
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



	public AttachEntity tailoredImage(String attachId,String uid,double scale) throws IOException {
	    AttachEntity attachEntity = attachRepository.findOne(attachId);
	    String fileName = attachEntity.getFilename();
	    File fromPic = new File(properties.getFilePath() + File.separator+fileName);
	    String id = UUID.randomUUID().toString();
	    String filename = id + "_thumb.png";
        BufferedImage image = ImageIO.read(fromPic);
        int imageWidth = image.getWidth();
        String toPicPath = properties.getFilePath() + File.separator+ id+"_thumb.png";
        File toPic = new File(toPicPath);
        int width = imageWidth;
        int height = (int) (imageWidth * scale);
        Thumbnails.of(fromPic).sourceRegion(Positions.CENTER,width,height).size(width,height).toFile(toPic);
        AttachEntity tailAttachEntity = saveAttach(uid,id,filename);
        return tailAttachEntity;
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

	/**
	 * @Description: 根据图片地址转换为base64编码字符串
	 * @Author:
	 * @CreateTime:
	 * @return
	 */
	public static String getImageStr(String imgFile) {
		InputStream inputStream = null;
		byte[] data = null;
		try {
			inputStream = new FileInputStream(imgFile);
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 加密
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}
}
