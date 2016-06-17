package com.bfyd.easypay.pay.wxpay;

import android.provider.MediaStore;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by zyk on 2016/6/9.
 * 不支持嵌套
 */
public class XMLParser {

    public static Map<String,Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  getStringStream(xmlString);
        Document document = builder.parse(is);

        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
            i++;
        }
        return map;

    }

    public static <T> T fromXML(String xmlString , Class<T> classOfT) throws ParserConfigurationException, IOException, SAXException, JSONException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  getStringStream(xmlString);
        Document document = builder.parse(is);

        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
//        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject json = new JSONObject();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
//                map.put(node.getNodeName(),node.getTextContent());
                String name = node.getNodeName();
                String content = node.getTextContent();
//                System.out.println("name:"+name+"   text:"+content);
                json.put(name,content);
            }
            i++;
        }
        String jsonStr = json.toString();
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, classOfT);

    }

    public static <T extends Object> String toXML( T c, String charset) throws IllegalAccessException, UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        Field[] field = c.getClass().getDeclaredFields();
        int count = field.length;
        for(int i = 0 ;i<count;i++) {
            Field f = field[i];
            Object o = f.get(c);
            if(o != null) {
                String name = f.getName();
                System.out.println("name:"+name);
                sb.append("<"+name+">");
                sb.append(o);
                sb.append("</"+name+">");
            }
        }
        sb.append("</xml>");
        return new String(sb.toString().getBytes(), charset);
    }

    public static <T extends Object> String toXML(T c) throws IllegalAccessException, UnsupportedEncodingException {
        return toXML(c, "UTF-8");
    }

    private static InputStream getStringStream(String sInputString) {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
        }
        return tInputStringStream;
    }
}
