package Structure;

import java.util.ArrayList;

public class FollowedBy extends AbstractEdge{
    ArrayList<String> typeRelation = new ArrayList<>();

    public FollowedBy(String _data){ data = _data; type = "FOLLOWED_BY";}
    public FollowedBy(String _data, String _relation){ data = _data; type = "FOLLOWED_BY"; addRelation(_relation);}

    public void addRelation(String relation){ typeRelation.add(relation); }
    public void removeRelation(String relation){ typeRelation.remove(relation); }

    public ArrayList<String> getRelations(){ return typeRelation; }

}
