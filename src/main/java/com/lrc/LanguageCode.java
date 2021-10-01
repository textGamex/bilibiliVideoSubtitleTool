package com.lrc;

/**
 * @author 留恋千年
 * @since 2021-9-30
 * @version 1.0.0
 */
public enum LanguageCode
{
//    /**简体中文*/
//    CHINESE("zh"),
    /**繁体中文*/
    TRADITIONAL_CHINESE("cht"),
    /**英语*/
    ENGLISH("en"),
    /**日语*/
    JAPANESE("jp"),
    /**德语*/
    GERMAN("de"),
    /**法语*/
    FRENCH("fra"),
    /**俄语*/
    RUSSIAN("ru"),
    /**意大利语*/
    ITALIAN("it"),
    /**西班牙语*/
    Spanish("spa");

    LanguageCode(String code)
    {
        this.code = code;
    }

    public String getLanguageCode()
    {
        return code;
    }
    private final String code;
}
