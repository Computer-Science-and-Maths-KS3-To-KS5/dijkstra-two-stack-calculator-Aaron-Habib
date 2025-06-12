import java.util.EmptyStackException;

public class Stack
{
    private int maxSize; // Changed from final
    private String[] stack; // Changed from final
    private int top = -1;

    public Stack(int maxSize)
    {
        this.maxSize = maxSize;
        this.stack = new String [maxSize];
    }

    public void push(String data)
    {
        if (isFull())
        {
            throw new StackOverflowError();
        }
        else
        {
            top++;
            stack[top] = data;
        }
    }

    public String pop()
    {
        if (isEmpty())
        {
            // Corrected: EmptyStackException does not take a message in its constructor.
            throw new EmptyStackException();
        }
        else
        {
            String poppedElement = stack[top]; // Store the element before decrementing top
            stack[top] = null; // Clear reference for garbage collection.
            top--;
            return poppedElement; // Return the stored element
        }
    }

    // Allows looking at the top element without removing it.
    public String peek()
    {
        if (isEmpty())
        {
            throw new EmptyStackException();
        }
        return stack[top];
    }

    public boolean isFull()
    {
        return top + 1 == maxSize;
    }

    public boolean isEmpty()
    {
        return top == -1;
    }
}
