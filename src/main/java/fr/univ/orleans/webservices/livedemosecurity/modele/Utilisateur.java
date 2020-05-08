package fr.univ.orleans.webservices.livedemosecurity.modele;

public class Utilisateur {
    private String login;
    private String password;
    private boolean isAdmin;


    public Utilisateur(String login, String password, boolean isAdmin) {
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
