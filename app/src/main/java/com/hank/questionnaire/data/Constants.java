package com.hank.questionnaire.data;

import com.hank.questionnaire.QuestionnairesApplication;
import com.hank.questionnaire.util.EnvUtil;

/**
 * Created by facebank on 2017/6/19.
 */

public class Constants {

    public static final String BASE_URL = "http://192.168.199.238/dalidai-user-app/";

    public static String getBaseUrl(){
        EnvUtil envUtil = EnvUtil.getInstance();
        return envUtil.isOudingTest() ? envUtil.getDevelopUrl(QuestionnairesApplication.getApplication()):BASE_URL;
    }

    public static String PHONENUMBER_SIMPLE_REGEX = "^[1][0-9]{10}$";
    public static String PHONENUMBER_RIGOROUS_REGEX = "^[1][3,4,5,7,8][0-9]{9}$";

    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_TITLE_NAME = "title_name";

    /**
     * 请求权限code
     */
    public static final class REQUEST_PERMISSION{
        public static final int REQUEST_CODE = 1001;
    }

    /**
     * 用户信息Intent参数
     */
    public static final class INTENT_PARAMETER_KEY {
        /**
         * 手机号key
         */
        public static final String PHONE_NUM = "phone_num";

        public static final String BASIC_INFO = "basic_info";
        public static final String WORK_INFO = "work_info";
        public static final String SOCIAL_INFO = "social_info";
        public static final String INFO_TYPE = "info_type";
    }

}
