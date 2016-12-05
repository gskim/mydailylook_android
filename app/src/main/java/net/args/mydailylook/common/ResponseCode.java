package net.args.mydailylook.common;

/**
 * Created by arseon on 2016-08-26.
 */
public class ResponseCode {
    /**
     * 정상
     */
    public static final String RESPONSE_1 = "1";

    /**
     * 토큰과 디바이스아이디 불일치
     */
    public static final String RESPONSE_2 = "2";

    /**
     * 등록되지않은 디바이스아이디 또는 등록되지않은 토큰
     */
    public static final String RESPONSE_3 = "3";

    /**
     * 정상이나 프로필등록을 하지 않은 회원
     */
    public static final String RESPONSE_4 = "4";

    /**
     * 정상이나 이메일 인증을 하지 않은 회원
     */
    public static final String RESPONSE_5 = "5";

}
