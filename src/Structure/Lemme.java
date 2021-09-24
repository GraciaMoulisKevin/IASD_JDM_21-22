package Structure;

public class Lemme extends AbstractNode{
    String provenance;

    public Lemme(String _data){ data = getLemme(_data); type = "LEMME"; provenance = _data;}
    public String getProvenance(){ return provenance; }

    /**
     * --- A FAIRE ---
     * @param _word
     * @return lemme de _word
     */
    private String getLemme(String _word){
        return _word;
    }
}
