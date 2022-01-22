package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PLResolution {

    public PLResolution() {

    }

    public Boolean Resolution (List<String> tree, List<String> alpha){

        List<String> clause = new ArrayList<String>();
        for (int i = 0; i < tree.size(); i++) {
            String a = tree.get(i);
            clause.add(a);
        }

        for (int i = 0; i < alpha.size(); i++) {
            String a = alpha.get(i);
            clause.add(a);
        }

        List<String> N = new ArrayList<String>();

        while (true){
            List<List<String>> pair_list = getPairs(clause);

            for (int i = 0; i <pair_list.size(); i++){

               List<String> pair = pair_list.get(i);

                List<String> resolvents = PLResolve(pair.get(0), pair.get(1));

                //System.out.println(resolvents);

                if(resolvents.contains("EMPTY")){
                    return true;
                }
                if (!resolvents.isEmpty()) {
                    List<String> uu = new ArrayList<String>();
                    String good = String.join(" ", resolvents);
                    uu.add(good);
                    N = union(N, uu);

                }
            }
            if (intersect(N, clause).size() == N.size()){
               return false;
            }
            clause = union(N, clause);
            //System.out.println(clause);
        }
    }

    public static List<String> union(List<String> list1, List<String> list2){
        HashSet<String> temp = new HashSet<String>();



        temp.addAll(list1);


        temp.addAll(list2);





        return new ArrayList<String>(temp);
    }

    public static List<String> intersect(List<String> list1, List<String> list2){
        List<String> temp = new ArrayList<String>();

        for (String str: list1) {
            if(list2.contains(str)){
                temp.add(str);
            }
        }
        return temp;
    }



    private List<List<String>> getPairs (List<String> clauseList){
        List<List<String>> pair = new ArrayList<List<String>>();

        for(int i = 0; i <clauseList.size(); i++) {
            for(int j = i; j <clauseList.size(); j++){
                List<String> temp_pair = new ArrayList<String>();

                String first = clauseList.get(i);
                String second = clauseList.get(j);

                temp_pair.add(first);
                temp_pair.add(second);
                pair.add(temp_pair);

            }
        }
        return pair;
    }


    public List<String> PLResolve(String c1, String c2) {

        List<String> resolvents = new ArrayList<String>();
        List<String> new_clause1 = split_clause(c1);
        List<String> new_clause2 = split_clause(c2);

        resolvents = resolve(new_clause1, new_clause2);

        return resolvents;

    }


    public List<String> split_clause(String clause) {

            List<String> new_clause = new ArrayList<String>();
            String[] temp = clause.split(" ");
            for(int i = 0; i <temp.length; i++) {
                new_clause.add(temp[i]);

            }
            return new_clause;
    }

    public List<String> resolve(List<String> clause1, List<String> clause2) {

        List<String> resolved = new ArrayList<String>();
        List<String> temp_resolved = new ArrayList<String>();
        List<List<Object>> list = new ArrayList<List<Object>>();

        List<List<Object>> list2 = new ArrayList<List<Object>>();


        for (int i = 0; i < clause1.size(); i++) {
            String temp = clause1.get(i);
            if ((temp.charAt(0)) == '~') {

                temp = temp.replace("~", "");
                List<Object> temp_list = new ArrayList<Object>();
                temp_list.add(temp);
                temp_list.add(-1);

                list.add(temp_list);

            } else {
                List<Object> temp_list = new ArrayList<Object>();
                temp_list.add(temp);
                temp_list.add(+1);

                list.add(temp_list);

            }
        }

        for (int i = 0; i < clause2.size(); i++) {
            String temp2 = clause2.get(i);
            if ((temp2.charAt(0)) == '~') {
                temp2 = temp2.replace("~", "");
                List<Object> temp_list2 = new ArrayList<Object>();
                temp_list2.add(temp2);
                temp_list2.add(-1);
                list2.add(temp_list2);

            } else {
                List<Object> temp_list2 = new ArrayList<Object>();
                temp_list2.add(temp2);
                temp_list2.add(+1);
                list2.add(temp_list2);
            }

        }


        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {

                if (list.get(i).get(0).equals(list2.get(j).get(0)) ) {

                    int a = (int) list.get(i).get(1);
                    int b = (int) list2.get(j).get(1);

                    int x = a + b;

                    if (x == 0) {

                        if (list2.size() < list.size()) {

                            list.remove(i);

                            for (int k = 0; k < list.size(); k++) {

                                int num = (int) list.get(k).get(1);

                                if (num == -1){
                                    String cl = (String) "~" + list.get(k).get(0);
                                    resolved.add(cl);
                                }else{
                                    String cl = (String) list.get(k).get(0);
                                    resolved.add(cl);
                                }

                            }

                        } else if (list2.size() > list.size()) {

                            list2.remove(j);

                            for (int k = 0; k < list2.size(); k++) {

                                int num = (int) list2.get(k).get(1);

                                if (num == -1){
                                    String cl = (String) "~" + list2.get(k).get(0);
                                    resolved.add(cl);
                                }else{
                                    String cl = (String) list2.get(k).get(0);
                                    resolved.add(cl);
                                }

                            }
                        }
                        else if ((list2.size() == list.size()) &&(list.size() ==1) ) {

                            resolved.add("EMPTY");

                        }
                    }
                }
            }
        }return resolved;
    }

}