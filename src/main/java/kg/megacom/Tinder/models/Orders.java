package kg.megacom.Tinder.models;

public class Orders {
    private long id;
    private Users sendId;
    private Users recipId;
    private boolean match;

    public Orders(){};
    public Orders(long id, Users sendId, Users recipId, boolean match) {
        this.id = id;
        this.sendId = sendId;
        this.recipId = recipId;
        this.match = match;
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public Users getSendId() {return sendId;}
    public void setSendId(Users sendId) {this.sendId = sendId;}

    public Users getRecipId() {return recipId;}
    public void setRecipId(Users recipId) {this.recipId = recipId;}

    public boolean isMatch() {return match;}
    public void setMatch(boolean match) {this.match = match;}
}
