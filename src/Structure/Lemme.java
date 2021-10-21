package Structure;

public class Lemme extends AbstractNode{
    public Lemme(String _data){ data = getLemme(_data); type = "LEMME"; }

    /**
     * --- A FAIRE ---
     * @param _word
     * @return lemme de _word
     */
    private String getLemme(String _word){
        // call vers JDM pour trouver le Lemme
        return "lemmeDe_"+_word;
    }
}
