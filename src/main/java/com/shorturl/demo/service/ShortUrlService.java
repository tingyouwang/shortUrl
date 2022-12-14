package com.shorturl.demo.service;

import com.shorturl.demo.helper.RequestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.mashape.unirest.http.JsonNode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

@Service
@Slf4j
public class ShortUrlService {

  public void getExcel(MultipartFile excelFile, String key, HttpServletResponse response) throws IOException {
    log.debug("into service");
    InputStream inputStream = excelFile.getInputStream();
//    var file = new FileInputStream(String.valueOf(inputStream));
    Workbook workbook = new XSSFWorkbook(inputStream);
    Sheet sheet = workbook.getSheetAt(0);

    Iterator it = sheet.iterator();

    while(it.hasNext()) {
      Row row = (Row)it.next();
      Cell urlCell = row.getCell(12);
      if (!urlCell.getStringCellValue().equals("網址")) {
        String targetUrl = urlCell.getStringCellValue();

        JSONObject json = new JSONObject();
        json.put("url", targetUrl);

        String url = "https://api.reurl.cc/shorten";
        JsonNode apiRes = RequestHelper.reqReurl(key, json.toString(), url);
        if (null == apiRes) {
          continue;
        }
        String shortUrl = apiRes.getObject().get("short_url").toString();
        Cell shortUrlCell = row.createCell(13);
        shortUrlCell.setCellValue(shortUrl);
      }
    }

//    FileOutputStream outputStream = new FileOutputStream(fileLocation);
//    workbook.write(outputStream);

    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/vnd.ms-excel; charset=utf-8");
    response.setHeader("Content-Disposition", "attachment;filename=" + "testName.xlsx");

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    outputStream.flush();
    outputStream.close();
    workbook.close();
    log.debug("close");
  }
}