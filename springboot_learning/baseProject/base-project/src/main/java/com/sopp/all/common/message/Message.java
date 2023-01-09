package com.sopp.all.common.message;

import com.sopp.all.common.constats.Constants;
import lombok.Data;

/**
 * @author Sopp
 * @create 2019-04-08
 * @Description：Global return information definition class
 */
@Data
public class Message {

    /*---------------------------------------------Define basic parameters to start-----------------------------------------------------*/

    /**
     * Return status code
     */
    private int code;

    /**
     * Return to the front end prompt information
     */
    private String msg;

    /**
     * Return data
     */
    private Object data;

    /**
     * Paging data
     */
    private int count;

    /*---------------------------------------------Define the end of the basic parameters-----------------------------------------------------*/

    /*---------------------------------------------Define the constructor to start-----------------------------------------------------*/

    public Message() {

    }

    /**
     * Functional Description: Construction Method - No Data
     *
     * @param: [codeSuccess, msgSuccess]
     * @return:
     * @auther: Sopp
     * @date: 2019/4/8 10:52
     */
    public Message(int codeSuccess, String msgSuccess) {
        this.code = codeSuccess;
        this.msg = msgSuccess;
    }

    /**
     * Functional Description: Construction Method - with data
     *
     * @param: [codeSuccess, msgSuccess, object]
     * @return:
     * @auther: Sopp
     * @date: 2019/4/8 10:54
     */
    public Message(int codeSuccess, String msgSuccess, Object object) {
        this.code = codeSuccess;
        this.msg = msgSuccess;
        this.data = object;
    }

    /**
     * Functional Description: Construction Method - with data and paging
     *
     * @param: [codeSuccess, msgSuccess, object, count]
     * @return:
     * @auther: Sopp
     * @date: 2019/4/8 11:07
     */
    public Message(int codeSuccess, String msgSuccess, Object object, int count) {
        this.code = codeSuccess;
        this.msg = msgSuccess;
        this.data = object;
        this.count = count;
    }

    /*---------------------------------------------Define the constructor end-----------------------------------------------------*/





    /*---------------------------------------------Define the return method to start-----------------------------------------------------*/

    /**
     * Functional Description: Method of successful operation - no return value
     *
     * @param: null
     * @return: Message
     * @auther: Sopp
     * @date: 2019/4/8 10:37
     */
    public static Message success() {
        return new Message(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS);
    }

    /**
     * Functional Description: The method of successful operation - there is a return value
     *
     * @param: [object]
     * @return: com.sopp.all.common.message.Message
     * @auther: Sopp
     * @date: 2019/4/8 10:45
     */
    public static Message success(Object object) {
        return new Message(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS, object);
    }

    /**
     * Functional Description: Operation successful - with return value and paging information
     *
     * @param: [object, count]
     * @return: com.sopp.all.common.message.Message
     * @auther: Sopp
     * @date: 2019/4/8 11:07
     */
    public static Message successList(Object object, int count) {
        return new Message(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS, object, count);
    }

    /**
     * Functional Description: Operation failed
     *
     * @param: null
     * @return: com.sopp.all.common.message.Message
     * @auther: Sopp
     * @date: 2019/4/8 10:59
     */
    public static Message fail() {
        return new Message(Constants.CODE_FAIL, Constants.MSG_FAIL);
    }

    /**
     * Functional Description: Error
     *
     * @param: [msg]
     * @return: com.sopp.all.common.message.Message
     * @auther: Sopp
     * @date: 2019/4/8 11:02
     */
    public static Message error(String msg) {
        return new Message(Constants.CODE_ERROR, msg);
    }


    /**
     * Functional Description: No request data
     *
     * @param: null
     * @return: com.sopp.all.common.message.Message
     * @auther: Sopp
     * @date: 2019/4/8 11:03
     */
    public static Message noData() {
        return new Message(Constants.CODE_NODATA, Constants.MSG_NODATA);
    }

    /**
     * Function description: Server exception
     *
     * @param: : null
     * @return: com.sopp.all.common.message.Message
     * @auther: Sopp
     * @date: 2019/4/19 10:03
     */
    public static Message warning() {
        return new Message(Constants.CODE_WARNING, Constants.MSG_WARNING);
    }
}
