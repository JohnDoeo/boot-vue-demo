package com.johndoeo.bootvueserver.constant;

public interface OperationCode {
    int SET = 555;
    int GET = 666;
    int ADD = 777;
    int UPDATE = 888;
    int DELETE = 999;
    int NO_USER = 400;
    int NO_LOGIN = 401;
    int NO_RIGHT = 402;
    int FAILED = -1;
    String CODE = "code";
    String RESULT = "result";
    String MESSAGE = "message";
}
