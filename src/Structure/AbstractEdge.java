package Structure;

import org.jgrapht.graph.DefaultEdge;

public abstract class AbstractEdge extends DefaultEdge {
    String data, type;

    // getSource & getTarget

    public String toString(){ return type+":"+data; }

    public String getType(){
        return type;
    }

    public String getData() {
        return data;
    }
}