package Myhotel;

public class User {
    private String login;
    private String password;
    private Model.Ac_type Ac_type;
    private String name;
    private int id;


    public User(String login, String password, Model.Ac_type ac_type, String name, int id) {
        this.login = login;
        this.password = password;
        Ac_type = ac_type;
        this.name = name;
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Model.Ac_type getAc_type() {
        return Ac_type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
