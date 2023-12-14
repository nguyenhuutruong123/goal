package com.goal.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.goal.constants.GlobalConstant;
import com.google.gson.Gson;
import com.goal.common.errors.BusinessException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonDataUtil {
    private static final Logger log = LoggerFactory.getLogger(CommonDataUtil.class);
    public static ModelMapper modelMapper;

    public static ObjectMapper  objectMapper ;

    public static boolean isEmpty(String str) {
        return Objects.isNull(str) || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !(collection == null || collection.isEmpty());
    }

    public static boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean getBooleanValue(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    public static <T> List<String> getDeclareFields(Class<? extends T> objectClass) {
        Field[] fields = objectClass.getDeclaredFields();
        List<String> lines = new ArrayList<>(fields.length);

        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            lines.add(camelToSnake(field.getName()));
        });
        return lines;
    }
    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace("Đ","D");
    }

    public static ModelMapper getModelMapper() {
        if (Objects.isNull(modelMapper)) {
            modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
        }
        return modelMapper;
    }

    public static ObjectMapper getModelMapperES() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Disable timestamp-based serialization
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Disable unknown property errors
        return objectMapper;
    }
    public static String convertObjectToStringJson(Object o) {
        try {
            String json = "";
            if (!Objects.isNull(o)) {
                 json=new Gson().toJson(o);
            }
            return json;
        }catch (Exception e){
            e.getMessage();
        }
       return "";
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String yesNoToBoolean(String value) {

        if (value.toUpperCase().equals("YES")) {
            return "true";
        } else {
            return "false";
        }

    }
    public static String readMappingFromFile(String fileName) throws IOException {
        Path source = Paths.get("");
        Path resourcePath = Paths.get(source.toAbsolutePath() + File.separator + GlobalConstant.PATH_RESOURCE +fileName);
        return new String(Files.readAllBytes(resourcePath));
    }
    private static String camelToSnake(String str) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        str = str.replaceAll(regex, replacement).toLowerCase();
        return str;
    }

    public static String joiningString(String delimiter, String... values) {
        return Stream.of(values).filter(CommonDataUtil::isNotEmpty).collect(Collectors.joining(delimiter));
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(comparingByValueReverse());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> comparingByValueReverse() {
        return (Comparator<Map.Entry<K, V>> & Serializable)
            (c1, c2) -> c2.getValue().compareTo(c1.getValue());
    }

    public static boolean isObjectHasPropertyValue(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object refObj = field.get(obj);
                if (refObj instanceof Collection) {
                    if (CommonDataUtil.isNotEmpty((Collection) refObj)) {
                        return true;
                    }
                } else {
                    if (CommonDataUtil.isNotNull(refObj) && field.getModifiers() != (Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                log.error("Exception occurred in processing");
                throw new BusinessException(e.getMessage());
            }
        }
        return false;
    }
    public static String toEmpty(String str) {
        return isEmpty(str) ? "" : str;
    }
    public static Integer getIntValueOrDefault(Integer value) {
        return isNotNull(value) ? value : 0;
    }

    public static Integer parseIntFromString(String value) {
        return CommonDataUtil.isNotEmpty(value) ? Integer.parseInt(value) : 0;
    }


    public static String getSearchByValue(String columnName, String searchBy, String searchByValue) {
        return columnName.equals(searchBy) ? searchByValue : "";
    }

    public static String contentEmail(String typeRequestChange,String requestNo, String userApproved, String sku
        , String currentStatus, String nextStatus, String description, String createBy
        , Instant createAt, String link) {
        String action="";
        if(typeRequestChange.equals("GS")){
            action="Change Group Sku";
        }else if(typeRequestChange.equals("SS")){
            action="Change Selling Status";
        }else if(typeRequestChange.equals("LC")){
            action="Change Life Cycle";
        }
        LocalDateTime createAtTime = LocalDateTime.ofInstant(createAt, ZoneOffset.UTC);
        String html =
            "\n <html lang=\"vi\" > <head>  " +
                "\n <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "\n <style> " +
                "\n      body, " +
                "\n      table, " +
                "\n      td, " +
                "\n      a { " +
                "\n          -webkit-text-size-adjust: 100%; " +
                "\n          -ms-text-size-adjust: 100%; " +
                "\n      } " +
                "\n   " +
                "\n      table, " +
                "\n      td { " +
                "\n          mso-table-lspace: 0pt; " +
                "\n          mso-table-rspace: 0pt; " +
                "\n      } " +
                "\n   " +
                "\n      img { " +
                "\n          -ms-interpolation-mode: bicubic; " +
                "\n      } " +
                "\n   " +
                "\n      img { " +
                "\n          border: 0; " +
                "\n          height: auto; " +
                "\n          line-height: 100%; " +
                "\n          outline: none; " +
                "\n          text-decoration: none; " +
                "\n      } " +
                "\n   " +
                "\n      table { " +
                "\n          border-collapse: collapse !important; " +
                "\n      } " +
                "\n   " +
                "\n      body { " +
                "\n          height: 100% !important; " +
                "\n          margin: 0 !important; " +
                "\n          padding: 0 !important; " +
                "\n          width: 100% !important; " +
                "\n      } " +
                "\n  " +
                "\n      a[x-apple-data-detectors] { " +
                "\n          color: inherit !important; " +
                "\n          text-decoration: none !important; " +
                "\n          font-size: inherit !important; " +
                "\n          font-family: inherit !important; " +
                "\n          font-weight: inherit !important; " +
                "\n          line-height: inherit !important; " +
                "\n      } " +
                "\n  " +
                "\n      div[style*=\"margin: 16px 0;\"] { " +
                "\n          margin: 0 !important; " +
                "\n      } " +
                "\n  </style>  </head> " +
                "\n  " +
                "\n  <body style=\"background-color: #f7f5fa; margin: 0 !important; padding: 0 !important;\"> " +
                "\n  " +
                "\n      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> " +
                "\n          <tr> " +
                "\n              <td align=\"center\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"480\"> " +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n          <tr> " +
                "\n              <td bgcolor=\"#426899\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"720\"> " +
                "\n                      <tr bgcolor=\"#ffffff\"> " +
                "\n                          <td " +
                "\n                              style=\"padding-top: 20px;padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 25px;\"> " +
                "\n                              <b>Dear </b> " + userApproved + " " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                      <tr bgcolor=\"#ffffff\"> " +
                "\n                          <td " +
                "\n                              style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 25px;\">" +
                "\n                              <b>YOU HAVE A NEW REQUEST CHANGE TO REVIEW AND APPROVE OR NOT.</b> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n          <tr> " +
                "\n              <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"720\"> " +
                "\n                      <tr> " +
                "\n                          <td bgcolor=\"#ffffff\" align=\"left\"> " +
                "\n                              <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> " +
                "\n                                  <tr style=\"background-color: #426899;\"> " +
                "\n                                      <th " +
                "\n                                          style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 600; line-height: 25px; color: #fff; padding: 10px; width: 55%\"> " +
                "\n                                          REQUEST CHANGE INFORMATION</th> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; padding-top: 20px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\"> " +
                "\n                                          <b>Request NO: </b>" + requestNo + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Action: </b>"+action+"</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <th align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>SKU: </b>" + sku + "</th> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " ;
               if(typeRequestChange.equals("GS")){
                   html+=  "\n                                          <b>Current Group: </b>" + currentStatus + "</td> " ;
               }else{
                   html+=    "\n                                      <b>Current Status: </b>" + currentStatus + "</td> " ;
               }
                 html+=  "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " ;
               if(typeRequestChange.equals("GS")){
                   html+= "\n                                          <b>Next Group: </b>" + nextStatus + "</td> " ;
               }else{
                   html+=  "\n                                          <b>Next Status: </b>" + nextStatus + "</td> " ;
               }
               html+= "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Description: </b>" + description + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Created By: </b>" + createBy + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:30px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Created At: </b>" + createAtTime + "</td> " +
                "\n                                  </tr> " +
                "\n <tr> " +
                "                                    <td align=\"right\" valign=\"top\"\n" +
                "                                        style=\"padding-left:30px;padding-right:30px;padding-bottom:30px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\">\n" +
                "                                        <a href=\"" + link + "\"  target=\"_blank\" style=\"background-color: #426899; display: block; text-decoration: none; color: #fff; padding: 10px; text-align: center; width: 100px; font-size: 14px;\n" +
                "                                            font-weight: 600;\n" +
                "                                            line-height: 25px; border-radius: 4px;\">\n" +
                "                                            Approve\n" +
                "                                        </a>\n" +
                "                                    </td>\n" +
                "                                </tr>" +
                "\n <tr> " +
                "\n                      <td align=\"center\" style=\"padding-bottom: 15px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">Thank You | Yes4All</td>" +
                "                                </tr>" +
                "\n                    <tr>" +
                "\n                        <td align=\"center\"" +
                "\n  style=\"padding: 15px 0; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; border-top: 1px solid #ccc\"></td>" +
                "\n                         </tr>" +
                "\n                        <tr>" +
                "\n                            <td align=\"center\"" +
                "\n   style=\"padding-bottom: 15px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">" +
                "\n    (<span style=\"color: red\">*</span>) Please do not respond to this email as responses are not monitored. <br /> If you have any questions, please <a href=\"mailto:nguyen.nguyenvt@yes4all.com\" style=\"text-decoration: none\">email support</a>.</td>" +
                "\n                         </tr> " +
                "\n                              </table> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                 </tr>" +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n      </table> " +
                "\n  " +
                "\n  </body> </html> ";
        return html;
    }

    public static String contentEmailCustomField(String requestNo, String userApproved, String createBy
        , String field, String type, String defaultValue, String description
        , Instant createAt, String link) {

        LocalDateTime createAtTime = LocalDateTime.ofInstant(createAt, ZoneOffset.UTC);
        String html =
            "\n <html lang=\"vi\" > <head>  " +
                "\n <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "\n <style> " +
                "\n      body, " +
                "\n      table, " +
                "\n      td, " +
                "\n      a { " +
                "\n          -webkit-text-size-adjust: 100%; " +
                "\n          -ms-text-size-adjust: 100%; " +
                "\n      } " +
                "\n   " +
                "\n      table, " +
                "\n      td { " +
                "\n          mso-table-lspace: 0pt; " +
                "\n          mso-table-rspace: 0pt; " +
                "\n      } " +
                "\n   " +
                "\n      img { " +
                "\n          -ms-interpolation-mode: bicubic; " +
                "\n      } " +
                "\n   " +
                "\n      img { " +
                "\n          border: 0; " +
                "\n          height: auto; " +
                "\n          line-height: 100%; " +
                "\n          outline: none; " +
                "\n          text-decoration: none; " +
                "\n      } " +
                "\n   " +
                "\n      table { " +
                "\n          border-collapse: collapse !important; " +
                "\n      } " +
                "\n   " +
                "\n      body { " +
                "\n          height: 100% !important; " +
                "\n          margin: 0 !important; " +
                "\n          padding: 0 !important; " +
                "\n          width: 100% !important; " +
                "\n      } " +
                "\n  " +
                "\n      a[x-apple-data-detectors] { " +
                "\n          color: inherit !important; " +
                "\n          text-decoration: none !important; " +
                "\n          font-size: inherit !important; " +
                "\n          font-family: inherit !important; " +
                "\n          font-weight: inherit !important; " +
                "\n          line-height: inherit !important; " +
                "\n      } " +
                "\n  " +
                "\n      div[style*=\"margin: 16px 0;\"] { " +
                "\n          margin: 0 !important; " +
                "\n      } " +
                "\n  </style>  </head> " +
                "\n  " +
                "\n  <body style=\"background-color: #f7f5fa; margin: 0 !important; padding: 0 !important;\"> " +
                "\n  " +
                "\n      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> " +
                "\n          <tr> " +
                "\n              <td align=\"center\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"480\"> " +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n          <tr> " +
                "\n              <td bgcolor=\"#426899\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"720\"> " +
                "\n                      <tr bgcolor=\"#ffffff\"> " +
                "\n                          <td " +
                "\n                              style=\"padding-top: 20px;padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 25px;\"> " +
                "\n                              <br>Dear </br> " + userApproved + ", " +
                "\n                              <br>" + createBy + " CREATED A NEW FIELD.</br> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                      <tr bgcolor=\"#ffffff\"> " +
                "\n                          <td " +
                "\n                              style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 25px;\">" +
                "\n                              <b></b> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n          <tr> " +
                "\n              <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"720\"> " +
                "\n                      <tr> " +
                "\n                          <td bgcolor=\"#ffffff\" align=\"left\"> " +
                "\n                              <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> " +
                "\n                                  <tr style=\"background-color: #426899;\"> " +
                "\n                                      <th " +
                "\n                                          style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 600; line-height: 25px; color: #fff; padding: 10px; width: 55%\"> " +
                "\n                                          CUSTOM FIELD INFORMATION</th> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; padding-top: 20px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\"> " +
                "\n                                          <b>Field: </b>" + field + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Type: </b>" + type + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <th align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Default Value: </b>" + defaultValue + "</th> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Description: </b>" + description + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Created By: </b>" + createBy + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Created At: </b>" + createAt + "</td> " +
                "\n                                  </tr> " +
                "\n <tr> " +
                "                                    <td align=\"right\" valign=\"top\"\n" +
                "                                        style=\"padding-left:30px;padding-right:30px;padding-bottom:30px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\">\n" +
                "                                        <a href=\"" + link + "\"  target=\"_blank\" style=\"background-color: #426899; display: block; text-decoration: none; color: #fff; padding: 10px; text-align: center; width: 100px; font-size: 14px;\n" +
                "                                            font-weight: 600;\n" +
                "                                            line-height: 25px; border-radius: 4px;\">\n" +
                "                                            Approve\n" +
                "                                        </a>\n" +
                "                                    </td>\n" +
                "                                </tr>" +
                "\n <tr> " +
                "\n                      <td align=\"center\" style=\"padding-bottom: 15px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">Thank You | Yes4All</td>" +
                "                                </tr>" +
                "\n                    <tr>" +
                "\n                        <td align=\"center\"" +
                "\n  style=\"padding: 15px 0; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; border-top: 1px solid #ccc\"></td>" +
                "\n                         </tr>" +
                "\n                        <tr>" +
                "\n                            <td align=\"center\"" +
                "\n   style=\"padding-bottom: 15px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">" +
                "\n    (<span style=\"color: red\">*</span>) Please do not respond to this email as responses are not monitored. <br /> If you have any questions, please <a href=\"mailto:nguyen.nguyenvt@yes4all.com\" style=\"text-decoration: none\">email support</a>.</td>" +
                "\n                         </tr> " +
                "\n                              </table> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                 </tr>" +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n      </table> " +
                "\n  " +
                "\n  </body> </html> ";
        return html;
    }

    public static String contentEmailComboSku(String userApproved, String createBy
        , String parentSku, String newSku, String productName, String upc, String country,
                                              String channel,
                                              Instant createAt, String link,Boolean isApproved) {

        LocalDateTime createAtTime = LocalDateTime.ofInstant(createAt, ZoneOffset.UTC);
        String html =
            "\n <html lang=\"vi\" > <head>  " +
                "\n <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "\n <style> " +
                "\n      body, " +
                "\n      table, " +
                "\n      td, " +
                "\n      a { " +
                "\n          -webkit-text-size-adjust: 100%; " +
                "\n          -ms-text-size-adjust: 100%; " +
                "\n      } " +
                "\n   " +
                "\n      table, " +
                "\n      td { " +
                "\n          mso-table-lspace: 0pt; " +
                "\n          mso-table-rspace: 0pt; " +
                "\n      } " +
                "\n   " +
                "\n      img { " +
                "\n          -ms-interpolation-mode: bicubic; " +
                "\n      } " +
                "\n   " +
                "\n      img { " +
                "\n          border: 0; " +
                "\n          height: auto; " +
                "\n          line-height: 100%; " +
                "\n          outline: none; " +
                "\n          text-decoration: none; " +
                "\n      } " +
                "\n   " +
                "\n      table { " +
                "\n          border-collapse: collapse !important; " +
                "\n      } " +
                "\n   " +
                "\n      body { " +
                "\n          height: 100% !important; " +
                "\n          margin: 0 !important; " +
                "\n          padding: 0 !important; " +
                "\n          width: 100% !important; " +
                "\n      } " +
                "\n  " +
                "\n      a[x-apple-data-detectors] { " +
                "\n          color: inherit !important; " +
                "\n          text-decoration: none !important; " +
                "\n          font-size: inherit !important; " +
                "\n          font-family: inherit !important; " +
                "\n          font-weight: inherit !important; " +
                "\n          line-height: inherit !important; " +
                "\n      } " +
                "\n  " +
                "\n      div[style*=\"margin: 16px 0;\"] { " +
                "\n          margin: 0 !important; " +
                "\n      } " +
                "\n  </style>  </head> " +
                "\n  " +
                "\n  <body style=\"background-color: #f7f5fa; margin: 0 !important; padding: 0 !important;\"> " +
                "\n  " +
                "\n      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> " +
                "\n          <tr> " +
                "\n              <td align=\"center\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"480\"> " +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n          <tr> " +
                "\n              <td bgcolor=\"#426899\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"720\"> " +
                "\n                      <tr bgcolor=\"#ffffff\"> " +
                "\n                          <td " +
                "\n                              style=\"padding-top: 20px;padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 25px;\"> " +
                "\n                              <br>Dear </br> " + userApproved + ", " +
                "\n                              <br>" + createBy + " CREATED A NEW SKU for " + parentSku + ".</br> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                      <tr bgcolor=\"#ffffff\"> " +
                "\n                          <td " +
                "\n                              style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 25px;\">" +
                "\n                              <b></b> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n          <tr> " +
                "\n              <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"720\"> " +
                "\n                      <tr> " +
                "\n                          <td bgcolor=\"#ffffff\" align=\"left\"> " +
                "\n                              <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> " +
                "\n                                  <tr style=\"background-color: #426899;\"> " +
                "\n                                      <th " +
                "\n                                          style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 600; line-height: 25px; color: #fff; padding: 10px; width: 55%\"> " +
                "\n                                         INFORMATION</th> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; padding-top: 20px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\"> " +
                "\n                                          <b>Parent SKU: </b>" + parentSku + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>New SKU: </b>" + newSku + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <th align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Product name: </b>" + productName + "</th> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>UPC: </b>" + upc + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Country: </b>" + country + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Channel: </b>" + channel + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Created By: </b>" + createBy + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Created At: </b>" + createAt + "</td> " +
                "\n                                  </tr> " ;
        if(isApproved) {
            html+=  "\n <tr> " +
                "                                    <td align=\"right\" valign=\"top\"\n" +
                "                                        style=\"padding-left:30px;padding-right:30px;padding-bottom:30px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\">\n" +
                "                                        <a href=\"" + link + "\"  target=\"_blank\" style=\"background-color: #426899; display: block; text-decoration: none; color: #fff; padding: 10px; text-align: center; width: 100px; font-size: 14px;\n" +
                "                                            font-weight: 600;\n" +
                "                                            line-height: 25px; border-radius: 4px;\">\n" +
                "                                            Approve\n" +
                "                                        </a>\n" +
                "                                    </td>\n" +
                "                                </tr>" +
                "\n <tr> " ;
        }
        html+=  "\n                      <td align=\"center\" style=\"padding-bottom: 15px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">Thank You | Yes4All</td>" +
                "                                </tr>" +
                "\n                    <tr>" +
                "\n                        <td align=\"center\"" +
                "\n  style=\"padding: 15px 0; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; border-top: 1px solid #ccc\"></td>" +
                "\n                         </tr>" +
                "\n                        <tr>" +
                "\n                            <td align=\"center\"" +
                "\n   style=\"padding-bottom: 15px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">" +
                "\n    (<span style=\"color: red\">*</span>) Please do not respond to this email as responses are not monitored. <br /> If you have any questions, please <a href=\"mailto:nguyen.nguyenvt@yes4all.com\" style=\"text-decoration: none\">email support</a>.</td>" +
                "\n                         </tr> " +
                "\n                              </table> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                 </tr>" +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n      </table> " +
                "\n  " +
                "\n  </body> </html> ";
        return html;
    }
    public static String contentEmailChangeSalesPic(String newSalesPic, String updateBy
        , String sku, String skuName, String aSin, String previousSalesPic
        , Instant updateAt) {

        LocalDateTime updateAtTime = LocalDateTime.ofInstant(updateAt, ZoneOffset.UTC);
        String html =
            "\n <html lang=\"vi\" > <head>  " +
                "\n <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "\n <style> " +
                "\n      body, " +
                "\n      table, " +
                "\n      td, " +
                "\n      a { " +
                "\n          -webkit-text-size-adjust: 100%; " +
                "\n          -ms-text-size-adjust: 100%; " +
                "\n      } " +
                "\n   " +
                "\n      table, " +
                "\n      td { " +
                "\n          mso-table-lspace: 0pt; " +
                "\n          mso-table-rspace: 0pt; " +
                "\n      } " +
                "\n   " +
                "\n      img { " +
                "\n          -ms-interpolation-mode: bicubic; " +
                "\n      } " +
                "\n   " +
                "\n      img { " +
                "\n          border: 0; " +
                "\n          height: auto; " +
                "\n          line-height: 100%; " +
                "\n          outline: none; " +
                "\n          text-decoration: none; " +
                "\n      } " +
                "\n   " +
                "\n      table { " +
                "\n          border-collapse: collapse !important; " +
                "\n      } " +
                "\n   " +
                "\n      body { " +
                "\n          height: 100% !important; " +
                "\n          margin: 0 !important; " +
                "\n          padding: 0 !important; " +
                "\n          width: 100% !important; " +
                "\n      } " +
                "\n  " +
                "\n      a[x-apple-data-detectors] { " +
                "\n          color: inherit !important; " +
                "\n          text-decoration: none !important; " +
                "\n          font-size: inherit !important; " +
                "\n          font-family: inherit !important; " +
                "\n          font-weight: inherit !important; " +
                "\n          line-height: inherit !important; " +
                "\n      } " +
                "\n  " +
                "\n      div[style*=\"margin: 16px 0;\"] { " +
                "\n          margin: 0 !important; " +
                "\n      } " +
                "\n  </style>  </head> " +
                "\n  " +
                "\n  <body style=\"background-color: #f7f5fa; margin: 0 !important; padding: 0 !important;\"> " +
                "\n  " +
                "\n      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> " +
                "\n          <tr> " +
                "\n              <td align=\"center\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"480\"> " +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n          <tr> " +
                "\n              <td bgcolor=\"#426899\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"720\"> " +
                "\n                      <tr bgcolor=\"#ffffff\"> " +
                "\n                          <td " +
                "\n                              style=\"padding-top: 20px;padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 25px;\"> " +
                "\n                              <br>Dear </br> " + newSalesPic + ", " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                      <tr bgcolor=\"#ffffff\"> " +
                "\n                          <td " +
                "\n                              style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 25px;\">" +
                "\n                              <b></b> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n          <tr> " +
                "\n              <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"720\"> " +
                "\n                      <tr> " +
                "\n                          <td bgcolor=\"#ffffff\" align=\"left\"> " +
                "\n                              <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> " +
                "\n                                  <tr style=\"background-color: #426899;\"> " +
                "\n                                      <th " +
                "\n                                          style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 600; line-height: 25px; color: #fff; padding: 10px; width: 55%\"> " +
                "\n                                          INFORMATION</th> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; padding-top: 20px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\"> " +
                "\n                                          <b>SKU: </b>" + sku + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Product Name: </b>" + skuName + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <th align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>ASIN: </b>" + aSin + "</th> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Previous Sales PIC: </b>" + previousSalesPic + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Updated By: </b>" + updateBy + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Updated At: </b>" + updateAtTime + "</td> " +
                "\n                                  </tr> " +
                "\n <tr> " +
                "\n                      <td align=\"center\" style=\"padding-bottom: 15px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">Thank You | Yes4All</td>" +
                "                                </tr>" +
                "\n                    <tr>" +
                "\n                        <td align=\"center\"" +
                "\n  style=\"padding: 15px 0; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; border-top: 1px solid #ccc\"></td>" +
                "\n                         </tr>" +
                "\n                        <tr>" +
                "\n                            <td align=\"center\"" +
                "\n   style=\"padding-bottom: 15px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">" +
                "\n    (<span style=\"color: red\">*</span>) Please do not respond to this email as responses are not monitored. <br /> If you have any questions, please <a href=\"mailto:nguyen.nguyenvt@yes4all.com\" style=\"text-decoration: none\">email support</a>.</td>" +
                "\n                         </tr> " +
                "\n                              </table> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                 </tr>" +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n      </table> " +
                "\n  " +
                "\n  </body> </html> ";
        return html;
    }
    public static String contentEmailChangeCategory(String user,String level, String updateBy
        , String title, String previousTitle
        , Instant updateAt) {

        LocalDateTime updateAtTime = LocalDateTime.ofInstant(updateAt, ZoneOffset.UTC);
        String html =
            "\n <html lang=\"vi\" > <head>  " +
                "\n <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "\n <style> " +
                "\n      body, " +
                "\n      table, " +
                "\n      td, " +
                "\n      a { " +
                "\n          -webkit-text-size-adjust: 100%; " +
                "\n          -ms-text-size-adjust: 100%; " +
                "\n      } " +
                "\n   " +
                "\n      table, " +
                "\n      td { " +
                "\n          mso-table-lspace: 0pt; " +
                "\n          mso-table-rspace: 0pt; " +
                "\n      } " +
                "\n   " +
                "\n      img { " +
                "\n          -ms-interpolation-mode: bicubic; " +
                "\n      } " +
                "\n   " +
                "\n      img { " +
                "\n          border: 0; " +
                "\n          height: auto; " +
                "\n          line-height: 100%; " +
                "\n          outline: none; " +
                "\n          text-decoration: none; " +
                "\n      } " +
                "\n   " +
                "\n      table { " +
                "\n          border-collapse: collapse !important; " +
                "\n      } " +
                "\n   " +
                "\n      body { " +
                "\n          height: 100% !important; " +
                "\n          margin: 0 !important; " +
                "\n          padding: 0 !important; " +
                "\n          width: 100% !important; " +
                "\n      } " +
                "\n  " +
                "\n      a[x-apple-data-detectors] { " +
                "\n          color: inherit !important; " +
                "\n          text-decoration: none !important; " +
                "\n          font-size: inherit !important; " +
                "\n          font-family: inherit !important; " +
                "\n          font-weight: inherit !important; " +
                "\n          line-height: inherit !important; " +
                "\n      } " +
                "\n  " +
                "\n      div[style*=\"margin: 16px 0;\"] { " +
                "\n          margin: 0 !important; " +
                "\n      } " +
                "\n  </style>  </head> " +
                "\n  " +
                "\n  <body style=\"background-color: #f7f5fa; margin: 0 !important; padding: 0 !important;\"> " +
                "\n  " +
                "\n      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> " +
                "\n          <tr> " +
                "\n              <td align=\"center\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"480\"> " +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n          <tr> " +
                "\n              <td bgcolor=\"#426899\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"720\"> " +
                "\n                      <tr bgcolor=\"#ffffff\"> " +
                "\n                          <td " +
                "\n                              style=\"padding-top: 20px;padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 25px;\"> " +
                "\n                              <br>Dear </br> " + user + ", " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                      <tr bgcolor=\"#ffffff\"> " +
                "\n                          <td " +
                "\n                              style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 25px;\">" +
                "\n                              <b></b> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n          <tr> " +
                "\n              <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\"> " +
                "\n                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"720\"> " +
                "\n                      <tr> " +
                "\n                          <td bgcolor=\"#ffffff\" align=\"left\"> " +
                "\n                              <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> " +
                "\n                                  <tr style=\"background-color: #426899;\"> " +
                "\n                                      <th " +
                "\n                                          style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 600; line-height: 25px; color: #fff; padding: 10px; width: 55%\"> " +
                "\n                                          INFORMATION</th> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; padding-top: 20px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\"> " +
                "\n                                          <b>Level: </b>" + level + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Previous Title: </b>" + previousTitle + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <th align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>New Title: </b>" + title + "</th> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Updated By: </b>" + updateBy + "</td> " +
                "\n                                  </tr> " +
                "\n                                  <tr> " +
                "\n                                      <td align=\"left\" valign=\"top\" " +
                "\n                                          style=\"padding-left:30px;padding-right:15px;padding-bottom:10px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; padding-top: 10px;\"> " +
                "\n                                          <b>Updated At: </b>" + updateAtTime + "</td> " +
                "\n                                  </tr> " +
                "\n <tr> " +
                "\n                      <td align=\"center\" style=\"padding-bottom: 15px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">Thank You | Yes4All</td>" +
                "                                </tr>" +
                "\n                    <tr>" +
                "\n                        <td align=\"center\"" +
                "\n  style=\"padding: 15px 0; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px; border-top: 1px solid #ccc\"></td>" +
                "\n                         </tr>" +
                "\n                        <tr>" +
                "\n                            <td align=\"center\"" +
                "\n   style=\"padding-bottom: 15px; font-family: Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 25px;\">" +
                "\n    (<span style=\"color: red\">*</span>) Please do not respond to this email as responses are not monitored. <br /> If you have any questions, please <a href=\"mailto:nguyen.nguyenvt@yes4all.com\" style=\"text-decoration: none\">email support</a>.</td>" +
                "\n                         </tr> " +
                "\n                              </table> " +
                "\n                          </td> " +
                "\n                      </tr> " +
                "\n                 </tr>" +
                "\n                  </table> " +
                "\n              </td> " +
                "\n          </tr> " +
                "\n      </table> " +
                "\n  " +
                "\n  </body> </html> ";
        return html;
    }

}
