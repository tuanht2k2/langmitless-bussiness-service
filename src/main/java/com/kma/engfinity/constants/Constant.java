package com.kma.engfinity.constants;

public class Constant {
    public static class Status {
        public static String OTP_FAILED = "FAILED";
        public static String OTP_SUCCESS = "SUCCESS";
        public static String OTP_BLOCKED = "BLOCKED";

        public static String SUCCESS = "SUCCESS";
    }

    public static class ErrorCode {
        public static Integer OK = 200;
        public static Integer BAD_REQUEST = 400;
        public static Integer SERVICE_ERROR = 500;
    }

    public static class ErrorMessage {
        public static String SUCCESS = "Success!";
        public static String BAD_REQUEST = "Bad request!";
        public static String SERVICE_ERROR = "Service error!";
    }

    public static class HireType {
        public static String HIRE = "HIRE";
        public static String HIRED = "HIRED";
    }

    public static class RedisKey {
        public static String MESSENGER_MEMBERS = "MESSENGER_MEMBERS_";
    }
}
