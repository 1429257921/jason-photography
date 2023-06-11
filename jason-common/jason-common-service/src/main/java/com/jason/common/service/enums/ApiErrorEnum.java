package com.jason.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应vo错误信息枚举
 *
 * @author gzc
 * @since 2023/6/12
 */
@Getter
@AllArgsConstructor
public enum ApiErrorEnum {
    /*
     * https://www.cnblogs.com/1111zhiping-tian/p/7577388.html
     * 100：（继续）请求者应当继续提出请求。服务器返回此代码表示已收到请求的第一部分，正在等待其余部分
     * 101：（切换协议）请求者已要求服务器切换协议，服务器已确认并准备切换
     * 2xx：表示成功处理了请求的状态代码
     * 200（成功）服务器已成功处理了请求，通常，这表示服务器提供了请求的页面
     * 204（重置内容）服务器成功处理了请求，但没有返回任何内容
     * 206（部分内容）服务器成功处理了部分GET请求
     * 3xx（重定向）：表示要完成请求，需要进一步操作。通常，这些状态代码用来重定向
     * 301:（永久移动）请求的页面已永久移动到新位置。服务器返回此响应（对GET和HEAD请求的响应）时，会自动将请求者转到新位置
     * 302（临时移动）服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求
     * 303（查看其他位置）请求者应当对不同的位置使用单独的GET请求来检索响应时，服务器返回此代码
     * 304（未修改）自从上次请求后，请求的页面未修改过。服务器返回此响应时，不会返回网页内容
     * 4xx（请求错误）：这些状态代码表示请求可能出错，妨碍了服务器的处理
     * 400：（错误请求）服务器不理解请求的语法
     * 401（未授权）请求要求身份验证。对于需要登录的网页，服务器可能返回此响应
     * 403（禁止）服务器拒绝请求
     * 404（未找到）服务器找不到请求的网页
     * 405（方法禁用）禁用请求中指定的方法
     * 406（不接受）无法使用请求的内容特性响应请求的网页
     * 408（请求超时）服务器等候请求时发生超时
     * 414（请求的URI过长）请求的URI（通常为网址）过长，服务器无法处理
     * 415（不支持的媒体类型）请求的格式不受请求页面的支持
     * 416（请求范围不符合要求）如果页面无法提供请求的范围，则服务器会返回此状态码
     * 5xx（服务器错误）这些状态代码表示服务器在尝试处理请求时发生内部错误。这些错误可能是服务器本身的错误，而不是请求出错
     * 500（服务器内部错误）服务器遇到错误，无法完成请求
     * 501（尚未实施）服务器不具备完成请求的功能，例如，服务器无法识别请求方法时可能会返回此代码
     * 502（错误网关）服务器作为网关和代理，从上游服务器收到无效响应
     * 503（服务器不可用）服务器目前无法使用（由于超载或者停机维护）。通常，这只是暂时状态
     * 504（网关超时）服务器作为网关或者代理，但是没有及时从上游服务器收到请求
     * 505（HTTP版本不受支持）服务器不支持请求中所用的HTTP协议版本
     * 428 Precondition Required (要求先决条件)
     * 429 Too Many Requests (太多请求)
     * 431 Request Header Fields Too Large (请求头字段太大)
     * 某些情况下，客户端发送 HTTP 请求头会变得很大，那么服务器可发送 431 Request Header Fields Too Large 来指明该问题。
     * 我不太清楚为什么没有 430 状态码，而是直接从 429 跳到 431，我尝试搜索但没有结果。唯一的猜测是 430 Forbidden 跟 403 Forbidden 太像了，为了避免混淆才这么做的，天知道！
     * 511 Network Authentication Required (要求网络认证)
     */
    /**
     * 成功段
     */
    SUCCESS(200, "成功"),
    ERROR(400, "失败"),
    IMPOSE_ERROR(4001, "操作频繁"),

    /**
     * 登录段
     */
    LOGIN_PASSWORD_ERROR(4002, "账户密码不匹配"),

    SQL_EXECUTE_ERROR(4999, "sql异常"),
    JSON_CONVERT_ERROR(4004, "JSON转换异常"),
    NETWORK_ERROR(4003, "网络繁忙"),
    INTERNAL_SERVER_ERROR(500, "系统繁忙"),

    ;

    private final Integer code;
    private final String msg;
}