package com.mds.common;

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
     * token异常
     */
    public static final int CODE_TOKEN_ERROR = 3;

    /**
     * 登录异常
     */
    public static final int CODE_LOGIN_ERROR = 4;

    /**
     * 无请求数据
     */
    public static final int CODE_NODATA = 300;

    /**
     * 数据错误
     */
    public static final int CODE_ERRORPARAM = 301;

    /**
     * 服务器异常
     */
    public static final int CODE_WARNING = 303;

    /**
     * 请求方法错误
     */
    public static final int CODE_METHODERROR = 305;




    /*-------------------------------------------返回提示信息-----------------------------------------------*/
    /**
     * 操作成功
     */
    public static final String MSG_SUCCESS = "Successful operation";

    /**
     * 操作失败
     */
    public static final String MSG_FAIL = "operation failed";

    /**
     * 无请求数据
     */
    public static final String MSG_NODATA = "Request no data";

    /**
     * 服务器异常
     */
    public static final String MSG_WARNING = "Server exception";

    /**
     * 请求方法错误
     */
    public static final String MSG_METHODERROR = "Please use the correct request method";


    /*-------------------------------------------全局魔法值定义-----------------------------------------------*/

    public static final int ONE = 1;

    public static final int TWO = 2;

    public static final int THREE = 3;

    public static final int FOUR = 4;

    public static final int FIVE = 5;

    public static final int SIX = 6;

    public static final int SEVEN = 7;

    public static final int EIGHT = 8;

    public static final int NINE = 9;

    public static final int TEN = 10;

    /**
     * 不知道警告
     */
    public static final String UNKNOW = "unknown";

    /**
     * IPUtil里面的number
     */
    public static final int IPUTIL_NUMBER = 15;

    /**
     * 本地ip
     */
    public static final String LOCALHOST = "127.0.0.1";

    /**
     * 其他ip
     */
    public static final String OTHER_IP = "0:0:0:0:0:0:0:1";

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 空符号
     */
    public static final String EMPTY = "";


    public final static String CAPTCHA_KEY = "captcha";

    public final static Integer STATUS_ON = 0;
    public final static Integer STATUS_OFF = 1;

    public static final String DEFULT_PASSWORD = "12345678";
    public static final int ZERO = 0;

    /**
     * token前缀
     */
    public static final String TOKEN_PRE = "token:";

    /**
     * role前缀
     */
    public static final String ROLE_PRE = "role:";

    /**
     * netty心跳逻辑判断
     */
    public static final String HEART_BEAT = "heartbeat";

    /**
     * netty tcp注册信息
     */
    public static final String REGISTER_INFO = "register";
}
