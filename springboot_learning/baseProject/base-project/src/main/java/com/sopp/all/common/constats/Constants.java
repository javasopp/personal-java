package com.sopp.all.common.constats;

/**
 * @author Sopp
 * @create 2019-04-08
 * @Description：一个全局的常量类，用来保存全局常亮
 */
public class Constants {
    /*--------------------------------------------返回状态码------------------------------------------------*/
    /**
     * 请求成功
     */
    public static final int CODE_SUCCESS = 0;

    /**
     * 请求失败
     */
    public static final int CODE_FAIL = 1;

    /**
     * 错误
     */
    public static final int CODE_ERROR = 2;

    /**
     * 无请求数据
     */
    public static final int CODE_NODATA = 300;

    /**
     * 服务器异常
     */
    public static final int CODE_WARNING = 303;


    /*-------------------------------------------返回提示信息-----------------------------------------------*/
    /**
     * 操作成功
     */
    public static final String MSG_SUCCESS = "操作成功";

    /**
     * 操作失败
     */
    public static final String MSG_FAIL = "操作失败";

    /**
     * 无请求数据
     */
    public static final String MSG_NODATA = "请求无数据";

    /**
     * 服务器异常
     */
    public static final String MSG_WARNING = "服务器异常";


    /*-------------------------------------------全局魔法值定义-----------------------------------------------*/

    /**
     * IPUtil里面的number
     */
    public static final int IPUTIL_NUMBER = 15;

    /*----------------------symbol符号常量定义----------------------*/

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 本地ip
     */
    public static final String LOCALHOST = "127.0.0.1";

    /**
     * 其他ip
     */
    public static final String OTHER_IP = "0:0:0:0:0:0:0:1";

    /**
     * 不知道警告
     */
    public static final String UNKNOW = "unknown";

}
