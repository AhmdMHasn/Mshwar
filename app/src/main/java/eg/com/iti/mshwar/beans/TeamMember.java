package eg.com.iti.mshwar.beans;

public class TeamMember {

    private String name;
    private int image;
    private String urlLinkedIn;
    private String urlGitHub;

    @Override
    public String toString() {
        return "TeamMember{" +
                "name='" + name + '\'' +
                ", image=" + image +
                ", urlLinkedIn='" + urlLinkedIn + '\'' +
                ", urlGitHub='" + urlGitHub + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUrlLinkedIn() {
        return urlLinkedIn;
    }

    public void setUrlLinkedIn(String urlLinkedIn) {
        this.urlLinkedIn = urlLinkedIn;
    }

    public String getUrlGitHub() {
        return urlGitHub;
    }

    public void setUrlGitHub(String urlGitHub) {
        this.urlGitHub = urlGitHub;
    }
}
