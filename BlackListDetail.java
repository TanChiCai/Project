public class BlackListDetail {

    private UserData user;
    private boolean blacklist;
    private String reason;

    public BlackListDetail() {
        
    }

    // Constructor
    public BlackListDetail(UserData user, boolean blacklist, String reason) {
        this.user = user;
        this.blacklist = blacklist;
        this.reason = reason;
    }

    // Getters
    public UserData getUser() {
        return user;
    }

    public boolean isBlacklisted() {
        return blacklist;
    }

    public String getReason() {
        return reason;
    }

    // Setters
    public void setUser(UserData user) {
        this.user = user;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    // toString method for easy printing
    @Override
    public String toString() {
        return  user.getUserid()+"\t"+user.getName()+"\t"+user.getPhoneNumber()
                 + '\t' +
                blacklist +"\t"+ reason + "\t";
    }
}
