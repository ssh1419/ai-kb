package edu.iastate.cs472.proj2;

import java.util.Scanner;

public class ExpTree1 extends Exp
{
    private String pl;
    private PureStack<Operator> optStack;

    public ExpTree1(String st){
        pl = st;
        optStack = new ArrayBasedStack();
    }
    public void toExpTree(){
        Scanner scan = new Scanner(pl);
        newExp = "";
        while(scan.hasNext()){
            String word = scan.next();

            if(checkVariable(word)){
                newExp = newExp + word + " ";
            }
            else if(checkOperator(word)){
                Operator op = new Operator(word);

                if(!optStack.isEmpty()){
                    Operator top = optStack.peek();

                    if(op.getOp().equals(")")){
                        while(!optStack.isEmpty() && !top.getOp().equals("(") ){
                            stack2postfix(op);
                        }
                        if(top.getOp().equals("(")){
                            optStack.pop();
                        }
                    }else {
                        if(top.compareTo(op)<0) optStack.push(op);
                        else stack2postfix(op);
                    }
                }else if(!op.getOp().equals(")")) {
                    optStack.push(op);
                }
            }
        }
        while(!optStack.isEmpty()){
            Operator op = optStack.pop();
            newExp = newExp + op.getOp() + " ";
        }
    }

    public void stack2postfix(Operator op) {
        if(!optStack.isEmpty()){
            Operator first = optStack.peek();

            if(op.getOp().equals(")")){
                while(!optStack.isEmpty() && !first.getOp().equals("(")){
                    Operator temp_op = optStack.pop();
                    newExp = newExp + temp_op.getOp() + " ";
                    if(!optStack.isEmpty()){
                        first = optStack.peek();
                    }
                }if(first.getOp().equals("(")){
                    optStack.pop();
                }
            }
            else {
                if(first.compareTo(op)<0){
                    optStack.push(op);
                }
                else{
                    while(!optStack.isEmpty() && first.compareTo(op)>=0) {
                        Operator temp_op = optStack.pop();
                        newExp = newExp + temp_op.getOp() + " ";
                        if(!optStack.isEmpty()){
                            first = optStack.peek();
                        }
                    }
                    optStack.push(op);
                }
            }
        }
        else if(!op.getOp().equals(")")) {
            optStack.push(op);
        }
    }
}
