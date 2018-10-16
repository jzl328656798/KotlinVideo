package yue.shen.kotlin.video.mvp.actions

/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 下午2:01
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */

class RequestException : Throwable {
    /**
     * The Msg.
     */
    var msg = ""
    /**
     * The Code.
     */
    var code = 0
    /**
     * The E.
     */
    lateinit var e: Exception

    constructor(detailMessage: String, code: Int) : super(detailMessage) {
        this.msg = detailMessage
        this.code = code
    }


    /**
     * Instantiates a new Request exception.
     *
     * @param detailMessage the detail message
     * @param code          the code
     * @param e             the e
     */
    constructor(detailMessage: String, code: Int, e: Exception) : super(detailMessage) {
        this.msg = detailMessage
        this.code = code
        this.e = e
    }

    /**
     * Instantiates a new Request exception.
     *
     * @param detailMessage the detail message
     * @param cause         the cause
     * @param code          the code
     * @param e             the e
     */
    constructor(detailMessage: String, cause: Throwable, code: Int, e: Exception) : super(detailMessage, cause) {
        this.msg = detailMessage
        this.code = code
        this.e = e
    }

}