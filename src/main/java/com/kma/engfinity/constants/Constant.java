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
        public static Integer NOT_FOUND = 404;
        public static Integer SERVICE_ERROR = 500;
    }

    public static class ErrorMessage {
        public static String SUCCESS = "Success!";
        public static String BAD_REQUEST = "Bad request!";
        public static String NOT_FOUND = "Data not found!";
        public static String PERMISSION_DENIED = "Permission denied!";
        public static String SERVICE_ERROR = "Service error!";
    }

    public static class HireType {
        public static String HIRE = "HIRE";
        public static String HIRED = "HIRED";
    }

    public static class RedisKey {
        public static String MESSENGER_MEMBERS = "MESSENGER_MEMBERS_";
    }

    public static class WithdrawalRequestStatus {
        public static String INIT = "INIT";
        public static String CANCELLED = "CANCELLED";
        public static String REJECTED = "REJECTED";
        public static String ACCEPTED = "ACCEPTED";
    }

    public static class OrderBy {
        public static String DESC = "DESC";
        public static String ASC = "ASC";
    }

    public static class TopicType {
        public static String EXERCISE = "EXERCISE";
        public static String EXAM = "EXAM";
    }

    public static class BooleanConstant {
        public static String TRUE = "TRUE";
        public static String FALSE = "FALSE";
    }
}
