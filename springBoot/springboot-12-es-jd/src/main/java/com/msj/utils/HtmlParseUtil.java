package com.msj.utils;


import com.msj.pojo.Content;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
@Component
public class HtmlParseUtil {
    public List<Content> parseJD(String keywords) throws IOException {
        String url = "https://search.jd.com/Search?keyword=" + keywords;
        Connection connect = Jsoup.connect(url);
        connect.header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36 Edg/90.0.818.49");
        Document document = connect.get();
        Element element = document.getElementById("J_goodsList");
        //获取所有的li标签,就是每一个li标签了
        Elements elements= element.getElementsByTag("li");

        ArrayList<Content> goodsList = new ArrayList<>();
        for (Element el : elements) {
            Content content = new Content();
            //获取图片:eq(0)表示拿到所有img标签中的第一个标签，attr（”src“）表示拿到图片的src地址
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            //获取商品的价格  text():把价格编程文档
            String price = el.getElementsByClass("p-price").eq(0).text();
            //获取标题
            String title = el.getElementsByClass("p-name").eq(0).text();
            content.setImg(img);
            content.setPrice(price);
            content.setTitle(title);
            goodsList.add(content);
        }
        return goodsList;
    }

    public static void main(String[] args) throws IOException {
        new HtmlParseUtil().parseJD("vue").forEach(System.out::println);
    }
}
