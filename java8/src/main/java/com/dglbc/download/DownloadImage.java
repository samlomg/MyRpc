package com.dglbc.download;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map;


public class DownloadImage {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String bing = "https://bing.com";
        String store = "D:\\壁纸\\";

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet("https://bing.com/HPImageArchive.aspx?format=js&idx=0&n=8&nc="+new Date().getTime()+"&pid=hp&video=1");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
//            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
//                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                String re = EntityUtils.toString(responseEntity);
//                System.out.println("响应内容为:" + re);
                BingImages images = JSON.parseObject(re, BingImages.class);

                images.getImages().forEach(image -> {
                    String url = bing + image.getUrl();
                    System.out.println(url);
                    String fileName = getParam(image.getUrl(), "id");
                    try {
                        download(url, fileName, store);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // TODO Auto-generated method stub
//        download("http://avatar.csdn.net/1/3/B/1_li1325169021.jpg", "1_li1325169021.jpg","d:\\image\\");
    }

    public static String getParam(String url, String name) {
        String params = url.substring(url.indexOf("?") + 1, url.length());
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        return split.get(name);
    }


    public static void download(String urlString, String filename, String savePath) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5 * 1000);
            // 输入流
            is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf = new File(savePath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            // 获取图片的扩展名
            String extensionName = filename.substring(filename.lastIndexOf(".") + 1);
            // 新的图片文件名 = 编号 +"."图片扩展名
//        String newFileName = goods.getProductId()+ "." + extensionName;
            os = new FileOutputStream(sf.getPath() + "\\" + filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            os.close();
            is.close();
        }


    }

}