package Structure;

public abstract class AbstractNode  {
    String data, type;

    public String toString(){
        return /*type+":"+*/data;
    }

    public String getType(){ return type; }

    public String getData() {
        return data;
    }
}
