package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class MAIN {

    /**
     * Read file
     */

    public static List<String> fileLineRead(String name) throws IOException {
        List<String> read = new ArrayList<String>();
        BufferedReader in = new BufferedReader(new FileReader(name));
        String st;
        while ((st = in.readLine()) != null) {
            read.add(st);
        }
        in.close();
        return read;
    }

    /**
     * CNF
     */

    private static ExpTree2 rule1 (ExpTree2 tree){
        if (tree == null) {
            return null;
        }
        String newOpt = null;
        if(tree.center.equals("~")){
            String rchildcenter = tree.right.center;
            if (rchildcenter.equals("&&")) {
                newOpt = "||";
            }
            else if (rchildcenter.equals("||")) {
                newOpt = "&&";
            }
            else if (rchildcenter.equals("~")) {
                newOpt = "~";
            }
        }

        ExpTree2 conv_tree;
        if (newOpt != null) {

            ExpTree2 tree1;
            ExpTree2 child1 = tree.right;

            if(newOpt.equals("~")){
                tree1 = child1.right;
            }else {
                ExpTree2 x = new ExpTree2("~", null, child1.left);
                ExpTree2 y = new ExpTree2("~", null, child1.right);
                ExpTree2 conv_l = x;
                ExpTree2 conv_r = y;

                ExpTree2 result = new ExpTree2(newOpt, conv_l, conv_r);
                tree1 = result;
            }
            //double rules.
            conv_tree = rule1(tree1);
        }else {
            //Check when the "~" is inside the children.
            ExpTree2 conv_l = rule1(tree.left);
            ExpTree2 conv_r = rule1(tree.right);
            ExpTree2 result = new ExpTree2(tree.center, conv_l, conv_r);
            conv_tree = result;
        }
        return conv_tree;

    }

    private static ExpTree2 rule2 (ExpTree2 tree){
        if (tree == null) {
            return null;
        }
        int idx = 0;
        ExpTree2 conv_tree;
        if (tree.center.equals("||")){
            if (tree.left.center.equals("&&")){

                idx = -1;
            }else if (tree.right.center.equals("&&")){
                idx = -2;
            }
        }

        if (idx != 0){
            ExpTree2 tree1 = comb(idx, tree.left, tree.right);
            conv_tree = rule2(tree1);
        }
        else {
            ExpTree2 conv_l = rule2(tree.left);
            ExpTree2 conv_r = rule2(tree.right);
            ExpTree2 result = new ExpTree2(tree.center, conv_l, conv_r);
            conv_tree = result;
        }
        return conv_tree;
    }

    private static ExpTree2 comb(int idx,  ExpTree2 lef,  ExpTree2 righ) {
        ExpTree2 same;
        ExpTree2 comb_l;
        ExpTree2 comb_r;
        ExpTree2 conv_tree;
        if (idx == -1) {
            comb_l = lef.left;
            comb_r = lef.right;
            same = righ;

            ExpTree2 x = new ExpTree2("||", comb_l, same);
            ExpTree2 y = new ExpTree2("||", comb_r, same);
            ExpTree2 conv_l = x;
            ExpTree2 conv_r = y;
            ExpTree2 result = new ExpTree2("&&", conv_l, conv_r);
            conv_tree = result;
        } else {
            comb_l = righ.left;
            comb_r = righ.right;
            same = lef;

            ExpTree2 x = new ExpTree2("||", same, comb_l);
            ExpTree2 y = new ExpTree2("||", same, comb_r);
            ExpTree2 conv_l = x;
            ExpTree2 conv_r = y;
            ExpTree2 result = new ExpTree2("&&", conv_l, conv_r);
            conv_tree = result;
        }
        return conv_tree;
    }

    private static ExpTree2 implConv ( ExpTree2 tree){
        if (tree == null) {
            return null;
        }

        ExpTree2 l = implConv(tree.left);
        ExpTree2 r = implConv(tree.right);

        ExpTree2 conv_tree;
        if (tree.center.equals("=>")) {

            ExpTree2 x = new ExpTree2("~", null, l);
            ExpTree2 conv_l = x;
            ExpTree2 conv_r = r;
            ExpTree2 result = new ExpTree2("||", conv_l, conv_r);
            conv_tree = result;
        }
        else {
            ExpTree2 _result = new ExpTree2(tree.center, l, r);
            conv_tree= _result;

        }
        return conv_tree;
    }

    private static ExpTree2 biconConv ( ExpTree2 tree){
        if (tree == null) {
            return null;
        }

        ExpTree2 l = implConv(tree.left);
        ExpTree2 r = implConv(tree.right);

        ExpTree2 conv_tree;
        if (tree.center.equals("<=>")) {

            ExpTree2 x = new ExpTree2("=>", l, r);
            ExpTree2 y = new ExpTree2("=>", r, l);
            ExpTree2 conv_l = x;
            ExpTree2 conv_r = y;

            ExpTree2 result = new ExpTree2("&&", conv_l, conv_r);
            conv_tree = result;
        }
        else {
            ExpTree2 _result = new ExpTree2(tree.center, l, r);
            conv_tree= _result;
        }
        return conv_tree;
    }

    private static Exp calOpt () {
        Exp exp = new Exp() ;
        //idx: number of children
        exp.updateOpt("&&", 2, Exp.pri + 1);
        exp.updateOpt("~", 1, Exp.pri + 2);
        exp.updateOpt("=>", 2, Exp.pri + 1);
        exp.updateOpt("<=>", 2, Exp.pri + 1);
        exp.updateOpt("||", 2, Exp.pri + 1);

        return exp;
    }

    public static void main(String args[]) throws IOException {
        List<String> set = fileLineRead("kb.txt");
        List<String> part1 = new ArrayList<String>();
        List<String> part2 = new ArrayList<String>();
        set.removeAll(Arrays.asList("", null));
        String t;

        /**
         * Data Preprocess
         */

        for (int i = 0; i < set.size(); i++) {
            if (set.get(i).equals("Knowledge Base:")) {
                for (int j = i + 1; j < set.size(); j++) {
                    t = set.get(j);

                    if(!t.equals("Prove the following sentences by refutation:")) {
                        part1.add(t);
                    }else{
                        break;
                    }
                }
            } else if (set.get(i).equals("Prove the following sentences by refutation:")) {
                for (int j = i + 1; j < set.size(); j++) {
                    t = set.get(j);
                    while (!t.equals(null)) {
                        part2.add(t);

                        break;
                    }
                }
            }

        }

        List<String> part1_new = new ArrayList<String>();
        List<String> part2_new = new ArrayList<String>();
        List<String> part3_new = new ArrayList<String>();


        for (int i = 0; i < part1.size(); i++) {
            part1_new.add(part1.get(i).replaceAll("~", "~ "));
        }

        for (int i = 0; i < part2.size(); i++) {
            part2_new.add(part2.get(i).replaceAll("~", "~ "));
        }

        for (int i = 0; i < part2_new.size(); i++) {
            String x = "~" + part2.get(i);
            part3_new.add(x);
        }


        /**
         * Postfix
         */

        String exp = "";
        List<String> postfix1 = new ArrayList<String>();
        List<String> postfix2 = new ArrayList<String>();

        for (int i = 0; i < part1_new.size(); i++) {
            t = part1_new.get(i);
            while (!t.equals(null)) {
                exp = part1_new.get(i);
                ExpTree1 exp1 = new ExpTree1(exp);
                exp1.toExpTree();
                postfix1.add(exp1.newExp);
                break;
            }
        }

        for (int i = 0; i < part2_new.size(); i++) {
            t = part2_new.get(i);
            while (!t.equals(null)) {
                exp = part2_new.get(i);
                ExpTree1 exp1 = new ExpTree1(exp);
                exp1.toExpTree();
                postfix2.add(exp1.newExp);
                break;
            }
        }


        /**
         *  Final Round
         */

        System.out.println("knowledge base in clauses:" + "\n");

        List<String> cnf = new ArrayList<String>();

        for (int i = 0; i < postfix1.size(); i++) {

            t = postfix1.get(i);
            Exp tree_exp = calOpt();
            tree_exp.convert2expr(t);
            ExpTree2 result = ExpTree3.Tree(tree_exp);
            result = biconConv(result);
            result = implConv(result);
            result = rule1(result);
            result = rule2(result);
            String a = result.toString();

            a = a.replace("|| ", "");
            cnf.add(a);

            System.out.println(result);
        }
        System.out.print("\n");




        for (int i = 0; i < postfix2.size(); i++){


            System.out.println("****************" );
            System.out.println("Goal sentence:" + (i+1) + "\n");
            System.out.println(part2.get(i) );
            System.out.println("****************" + "\n");

            System.out.println("Negated goal in clauses: " + "\n");

            System.out.println('~' + part2.get(i) + "\n" );

            System.out.println("Proof by refutation: " + "\n");


            /**
             * Resolve
             */

            PLResolution finalllll = new PLResolution();
            List<String> alpha = new ArrayList<String>();
            String temp = part3_new.get(i);

            alpha.add(temp);
            Boolean a = finalllll.Resolution(cnf, alpha);

            if (a == true){
                System.out.println("The KB entails " + part2.get(i));

            }
            else {
                System.out.println("The KB does not entail" + part2.get(i));

            }
        }
    }
}





