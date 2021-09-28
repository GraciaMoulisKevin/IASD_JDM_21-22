package Structure;

import org.jgrapht.graph.DefaultEdge;

public abstract class AbstractEdge extends DefaultEdge {
    String data;

    public String toString(){ return data; }
}
