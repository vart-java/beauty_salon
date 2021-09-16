package com.artuhin.sproject.util;

public class ExceptionMessageTemplates {

    private ExceptionMessageTemplates(){

    }
    public static final String USER_NOT_FOUND_MESSAGE = "Sorry, user with such id not found.";
    public static final String USER_CAN_NOT_BE_UPDATED_MESSAGE = "Sorry, this user can not be updated. Check that data is correct.";
    public static final String PROCEDURE_NOT_FOUND_MESSAGE = "Sorry, procedure with such id not found.";
    public static final String PROCEDURE_CAN_NOT_BE_ARRANGED_MESSAGE = "Sorry, procedure can not be arranged at this time for this master.";
    public static final String MASTER_CAN_NOT_PERFORM_PROCEDURE_MESSAGE = "Sorry, this master can not perform %s";
}
