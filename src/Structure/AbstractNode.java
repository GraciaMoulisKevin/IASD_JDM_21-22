package Structure;

public abstract class AbstractNode implements InterfaceNode {
    String data;
    AbstractNode(String _data) { data = _data; }
    public String toString(){ return data; }
}
