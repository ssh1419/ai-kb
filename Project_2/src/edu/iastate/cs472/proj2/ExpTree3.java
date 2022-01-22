package edu.iastate.cs472.proj2;

public class ExpTree3 {
    public static ExpTree2 Tree (Exp tree_exp) {
        ArrayBasedStack<ExpTree2> tree_list = new ArrayBasedStack<ExpTree2>();
        while (tree_exp.scanExp()) {
            String final_opt = tree_exp.checkNext();

            if (tree_exp.checkOperator(final_opt)) {
                ExpTree2 right = tree_list.pop();
                ExpTree2 left;
                int num_leaf = tree_exp.num_child.get(final_opt);
                if (num_leaf == 2) {
                    left = tree_list.pop();
                } else {
                    left = null;
                }
                ExpTree2 temp_tree = new ExpTree2(final_opt, left, right);
                tree_list.push(temp_tree);
            } else {

                ExpTree2 child = new ExpTree2(final_opt, null, null);
                tree_list.push(child);
            }
        }
        return tree_list.pop();
    }
}
