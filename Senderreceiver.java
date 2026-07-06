import java.util.*;
class Base
{
    void sender()
    {
        System.out.println("Sender: Hi");
    }
    void receiver1()
    {
        System.out.println("Receiver: Hello");
    }
    void receiver2()
    {
        System.out.println("Receiver: Bye");
    }
}
class Senderreceiver {
    public static void main(String[] args) 
    {
        int a,b,c,r=0,again;
        
        boolean o=true;
        Base ba=new Base();
        Scanner sc=new Scanner(System.in);
        ba.sender();
        ba.receiver1();
        while(o)
        {
            System.out.println("choose the operation among these:\n1.+\n2.-\n3.*\n4./");
            c=sc.nextInt();
            System.out.println("print a,b");
            a=sc.nextInt();
            b=sc.nextInt();
            if (c==4 && b==0)
            {System.out.println("b should not be 0 choose another:");
            b=sc.nextInt();
                
            }
            
            switch(c)
            {case 1:r=a+b;
            break;
            case 2:r=a-b;
            break;
            case 3:r=a*b;
            break;
            case 4:r=a/b;
            break;
            default:System.out.println("operation not available");
                
            }
            System.out.println("Result is :"+r);
            System.out.println("1 to continue 0 to stop:");
            again=sc.nextInt();
            if (again==0)
            o=false;
            
        }
        ba.receiver2();
    
        
       
    }
}