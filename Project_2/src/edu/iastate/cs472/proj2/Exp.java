package edu.iastate.cs472.proj2;

import java.util.HashMap;
import java.util.Scanner;

public class Exp {

    public static  int pri = 0;
    private HashMap<String, Integer> opt;
    HashMap<String, Integer> num_child;
    private String pl;
    private int pos;


    /**
     * Postfix
     */

    protected String newExp;
    protected HashMap<Character, Integer> varStore;
    protected Exp() {
        opt = new HashMap<String, Integer>();
        num_child = new HashMap<String, Integer>();
    }
    protected static boolean checkOperator(String st){
        if(st.equals("~") || st.equals("&&") || st.equals("||") || st.equals("=>") || st.equals("<=>") || st.equals("(") || st.equals(")") ){
            return true;
        }
        return false;
    }
    protected static boolean checkVariable(String st){
        int len = st.length();
        for (int i = 0; i <len; i++){
            if((Character.isLetter(st.charAt(i))==false)){
                return false;
            }
        }return true;
    }

    /**
     * Expression Tree and CNF
     */
    public  void convert2expr (String expr) {
        pl = expr;
    }

    public  void updateOpt (String optr, int num, int priority) {
        num_child.put(optr, num);
        opt.put(optr, priority);
    }

    private void nospace () {
        int count = 0;
        int len = pl.length();
        while (((pos + count)< len)
                && Character.isWhitespace(pl.charAt(pos + count))) {
            count += 1;
        }
        pos = pos + count;
    }

    public  String checkNext () {
        if (!scanExp()) {
            return null;
        }
        String optr = get_opt(pos);
        if (optr == null) {
            int count = 0;
            while (((pos + count)< pl.length())
                    && (!Character.isWhitespace(pl.charAt(pos + count))
                    && (get_opt(pos + count) == null) )) {
                count = count + 1;
            }
            optr = pl.substring(pos, pos + count);
        }
        pos = pos + optr.length();
        return optr;
    }

    public  boolean scanExp () {
        nospace();
        return (pos < pl.length());
    }

    private String get_opt (int pos) {
        String optr = null;
        for (String opt : opt.keySet()) {
            int len = opt.length();
            int pos2end = pos + len;
            if (((pos2end - 1)< pl.length())
                    && pl.substring(pos, pos2end).equals(opt)) {
                optr = opt;
                break;
            }
        }
        return optr;
    }
}