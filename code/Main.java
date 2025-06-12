//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.EmptyStackException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        System.out.println("Welcome to the Dijkstra Calculator!!");
        System.out.println();

        System.out.print("Enter an expression: ");
        String expression = in.nextLine();
        System.out.println();

        // Evaluate the expression and display the result.
        try
        {
            double result = evaluate(expression);
            System.out.println("The expression evaluates to: " + result);
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Please ensure the expression is valid (e.g., (1 + (2 * 3))).");
        }
        catch (EmptyStackException e)
        {
            System.err.println("Calculation error: " + e.getMessage());
            System.err.println("Invalid expression or mismatched parentheses detected.");
        }
        catch (StackOverflowError e)
        {
            System.err.println("Calculator capacity exceeded: " + e.getMessage());
            System.err.println("The expression might be too long or complex.");
        }
        catch (ArithmeticException e) // Catch division by zero specifically.
        {
            System.err.println("Math error: " + e.getMessage());
        }
        finally
        {
            in.close(); // Close the scanner.
        }
    }

    // Evaluates a mathematical expression using the Dijkstra's two-stack algorithm.
    public static double evaluate(String expression)
    {
        char[] expressionArray = expression.toCharArray();

        // Stacks for operands and operators.
        Stack operandStack = new Stack(expressionArray.length);
        Stack operatorStack = new Stack(expressionArray.length);

        int i = 0; // Current position in the expression string.

        // Iterate through each character of the expression.
        while (i < expressionArray.length)
        {
            char currentCharacter = expressionArray[i];

            // Handling spaces.
            if (Character.isWhitespace(currentCharacter))
            {
                i++;
            }
            // Handling opening parentheses.
            else if (currentCharacter == '(')
            {
                operatorStack.push(String.valueOf(currentCharacter));
                i++;
            }
            // Handling numbers (digits and decimal points).
            else if (Character.isDigit(currentCharacter) || currentCharacter == '.') // Allow decimal point at start for numbers like .5
            {
                int startOfNumber = i;

                // Read the full number.
                while (i < expressionArray.length && (Character.isDigit(expressionArray[i]) || expressionArray[i] == '.'))
                {
                    i++;
                }

                // Parse the number and push it onto the operand stack.
                String numberString = expression.substring(startOfNumber, i);
                operandStack.push(numberString);
            }
            // To handle operators (applying precedence rules).
            else if (isOperator(currentCharacter))
            {
                // While there are operators on the stack with higher or equal precedence, apply them.
                while (!operatorStack.isEmpty() && precedence(currentCharacter) <= precedence(operatorStack.peek().charAt(0)))
                {
                    applyTopOperation(operandStack, operatorStack);
                }
                operatorStack.push(String.valueOf(currentCharacter));
                i++;
            }
            // Handle closing parentheses, triggering an operation within the scope.
            else if (currentCharacter == ')')
            {
                // Apply operations until an opening parenthesis is found.
                while (!operatorStack.isEmpty() && operatorStack.peek().charAt(0) != '(')
                {
                    applyTopOperation(operandStack, operatorStack);
                }
                // Pop the opening parenthesis.
                if (!operatorStack.isEmpty() && operatorStack.peek().charAt(0) == '(')
                {
                    operatorStack.pop();
                }
                else
                {
                    throw new IllegalArgumentException("Mismatched parentheses. Missing opening parenthesis.");
                }
                i++;
            }
            // Handling invalid characters.
            else
            {
                throw new IllegalArgumentException("Invalid character: '" + currentCharacter + "' at position " + i);
            }
        }

        // Apply any remaining operations in the operator stack.
        while (!operatorStack.isEmpty())
        {
            applyTopOperation(operandStack, operatorStack);
        }

        // The final result should be the only value left on the operand stack.
        if (operandStack.isEmpty()) { // Handle cases where input might be empty or only spaces.
            throw new IllegalArgumentException("Expression is empty or results in no value.");
        }
        return Double.parseDouble(operandStack.pop());
    }

    // Helper method to apply the top operation from the operator stack to the operand stack.
    private static void applyTopOperation(Stack operandStack, Stack operatorStack)
    {
        String operatorString = operatorStack.pop();
        char currentOperator = operatorString.charAt(0);
        double rightOperand = Double.parseDouble(operandStack.pop());
        double leftOperand = Double.parseDouble(operandStack.pop());
        double resultValue = applyOperation(currentOperator, leftOperand, rightOperand);
        operandStack.push(String.valueOf(resultValue));
    }


    // Checks if a character is a supported arithmetic operator.
    private static boolean isOperator(char character)
    {
        return character == '+' || character == '-' || character == '*' || character == '/';
    }

    // Determines the precedence of an operator. Higher number means higher precedence.
    private static int precedence(char operator)
    {
        switch (operator)
        {
            case '+':
            case '-':
            {
                return 1;
            }
            case '*':
            case '/':
            {
                return 2;
            }
            case '(': // Lowest precedence for '(' when it's on the stack.
            {
                return 0;
            }
            default:
            {
                return -1; // Should not happen for valid operators.
            }
        }
    }

    // This performs the arithmetic operation based on the given operator.
    public static double applyOperation(char operator, double leftOperand, double rightOperand)
    {
        switch (operator)
        {
            case '+':
            {
                return leftOperand + rightOperand;
            }
            case '-':
            {
                return leftOperand - rightOperand;
            }
            case '*':
            {
                return leftOperand * rightOperand;
            }
            case '/':
            {
                if (rightOperand == 0)
                {
                    throw new ArithmeticException("Division by zero is not allowed.");
                }
                return leftOperand / rightOperand;
            }
            default:
            {
                throw new IllegalArgumentException("Unsupported operator: " + operator);
            }
        }
    }
}
