package com.bshuai.content.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bshuai.content.util.QRCodeUtil;
import com.google.zxing.WriterException;

import io.swagger.annotations.Api;

@Api(description = "二维码生成")
@RestController
@RequestMapping(value = "qrcode")
public class QrcodeController {

	@GetMapping(value = "view")
	public void view(String text, Integer width, Integer height, HttpServletResponse response)
			throws IOException, WriterException {

		if (width == null) {
			width = 360;
		}
		if (height == null) {
			height = 360;
		}

		response.setContentType("image/png");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		QRCodeUtil.writeToStream(width, height, text, response.getOutputStream());

	}

}
