package com.shorturl.demo.controller;

import com.shorturl.demo.service.ShortUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
public class ShortUrlController {

  @Autowired
  ShortUrlService shortUrlService;
  @Autowired
  Environment env;

  @PostMapping(value = {"/ws/api/v1/getexcel"})
  public void getExcel(@RequestParam("files") MultipartFile excelFile,
                       HttpServletResponse response) throws IOException {
    if (excelFile.isEmpty()) {
      log.debug("excel is empty");
    }
    String token = env.getProperty("token");
    shortUrlService.getExcel(excelFile, token, response);

  }

}
