package com.ojk.retrofitmvp.entity;

public class CommonTonggEntity {


    /**
     * code : 0
     * msg : 失败
     * response : {"list":null,"cont":null}
     */

    private int code;
    private String msg;
    private ResponseBean response;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * list : null
         * cont : null
         */

        private Object list;
        private Object cont;

        public Object getList() {
            return list;
        }

        public void setList(Object list) {
            this.list = list;
        }

        public Object getCont() {
            return cont;
        }

        public void setCont(Object cont) {
            this.cont = cont;
        }
    }

}
