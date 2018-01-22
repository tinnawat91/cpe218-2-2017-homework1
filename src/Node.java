public class Node {
    public Node left;//node left and node right
    public Node right;
    Character check;//check a must be char
    public Node(char a){
        check=a;
    }
    public String toString(){
        return check.toString();
    }
}
