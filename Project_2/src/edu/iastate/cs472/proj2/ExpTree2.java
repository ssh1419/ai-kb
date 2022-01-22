package edu.iastate.cs472.proj2;

public class ExpTree2
{
    String center;
    ExpTree2 left;
    ExpTree2 right;

    public ExpTree2(String ct, ExpTree2 l, ExpTree2 r ){
        center = ct;
        left = l;
        right = r;
    }

    private static String reorder (ExpTree2 tree){

        StringBuffer tree_stack = new StringBuffer();

        if((tree.left == null) && (tree.right == null)){
            tree_stack.append(tree.center);
        }
        else if (tree.left == null){
            tree_stack.append(tree.center);
            tree_stack.append(reorder(tree.right));
        }
        else {
            tree_stack.append(reorder(tree.left) + " ");
            tree_stack.append(tree.center + " ");
            tree_stack.append(tree.right);
        }
        return tree_stack.toString();
    }

    public String toString() {
        return reorder(this);
    }

}
