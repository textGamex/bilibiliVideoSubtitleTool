package com.lrc.tools;

import com.Data;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.translate.demo.TransApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 留恋千年
 * @since 2021-9-30
 * @version 1.2.1
 */
public final class LrcTranslate
{
    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer;
    private static final TransApi API = new TransApi(Data.APP_ID, Data.SECURITY_KEY);

    public static String[] getTranslateLyricArray(List<String> original, String targetLanguage)
    {
        return getTranslateLyricArray(original.toArray(new String[0]), targetLanguage);
    }

    /**
     * 翻译传入的歌词数组
     *
     * @param original 原来的歌词数组, 需要带时间标签
     * @param targetLanguage 要翻译到的语言
     * @return 已经翻译好的歌词数组
     */
    public static String[] getTranslateLyricArray(String[] original, String targetLanguage)
    {
        var list = new ArrayList<String>(25);
        for (var s : original)
        {
            list.add(LrcTools.getLyricString(s));
        }
        var map = getTranslationResultsMap(getTranslateLyric(list), targetLanguage);
        var array = new ArrayList<>(List.of(original));

        for (var s : map.keySet())
        {
            for (int i = 0, max = array.size(); i < max; ++i)
            {
                int index = 0;
                if (array.get(i).contains(s))
                {
                    index = i;
                    array.set(index, LrcTools.replaceLyric(array.get(index), map.get(s)));
                }
            }
        }
        return array.toArray(new String[0]);
    }
    /**
     *
     * @param query 翻译文本
     * @param targetLanguage 翻译到的语言
     * @return 一个保存着翻译后文本的集合
     */
    private static String[] getTranslationResults(String query, String targetLanguage)
    {
        var array = new ArrayList<String>(32);
        String translationText = API.getTransResult(query, "auto", targetLanguage);
        var json = JSONObject.parseObject(translationText);
        List<String> list = JSON.parseArray(json.getJSONArray("trans_result").toJSONString(), String.class);

        for (String s : list)
        {
            var jsonObject = JSONObject.parseObject(s);
            array.add(jsonObject.getString("dst"));
        }
        return array.toArray(new String[0]);
    }

    private static Map<String, String> getTranslationResultsMap(String query, String targetLanguage)
    {
        var map = new HashMap<String, String>(64);
        String translationText = API.getTransResult(query, "auto", targetLanguage);
        var json = JSONObject.parseObject(translationText);
        List<String> list = JSON.parseArray(json.getJSONArray("trans_result").toJSONString(), String.class);

        for (String s : list)
        {
            var jsonObject = JSONObject.parseObject(s);
            var originalText =  jsonObject.getString("src");
            var translateText = jsonObject.getString("dst");
            map.put(originalText, translateText);
        }
        return map;
    }

    /**
     * 把每一句歌词合并成一整个字符串, 以便进行翻译, 在每一句结束后换行.
     *
     * @param list 包含歌词的集合
     * @return 包含每一句歌词的字符串
     */
    private static String getTranslateLyric(List<String> list)
    {
        var stringBuilder = new StringBuilder(25);
        for (var s : list)
        {
            stringBuilder.append(s).append('\n');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
