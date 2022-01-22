package edu.iastate.cs472.proj2;

public class Operator implements Comparable<Operator>
{
    public String operator;

    private   int inputPrecedence;
    private   int stackPrecedence;

    /**
     * Constructor
     *
     * ch == '~' if the operator is unary minus.
     *
     * Refer to the table in Section 2.2 of the project description when setting the values of
     * inputPrecedence and stackPrecedence.
     *
     * @param ch
     */
    public Operator(String ch)
    {
        operator = ch;
        if(ch.equals("~")) {
            inputPrecedence = 5;
            stackPrecedence = 5;
        }
        else if(ch.equals("&&")) {
            inputPrecedence = 4;
            stackPrecedence = 4;
        }
        else if(ch.equals("||")) {
            inputPrecedence = 3;
            stackPrecedence = 3;
        }
        else if(ch.equals("=>")) {
            inputPrecedence = 2;
            stackPrecedence = 2;
        }
        else if(ch.equals("<=>")) {
            inputPrecedence = 1;
            stackPrecedence = 1;
        }
        else if(ch.equals("(")) {
            inputPrecedence = 6;
            stackPrecedence = -1;
        }
        else if(ch.equals(")")) {
            inputPrecedence = 0;
            stackPrecedence = 0;
        }
    }


    /**
     * Returns 1, 0, -1 if the stackPrecedence of this operator is greater than, equal to,
     * or less than the inputPrecedence of the parameter operator op. It's for determining
     * whether this operator on the stack should be output before pushing op onto the stack.
     */
    @Override
    public int compareTo(Operator op)
    {
        if(this.stackPrecedence > op.inputPrecedence) return 1;
        else if(this.stackPrecedence == op.inputPrecedence) return 0;
        else return -1;  // TO MODIFY
    }


    /**
     *
     * @return char Returns the operator character.
     */
    public String getOp()
    {
        return operator;
    }
}