package kg.megacom.Tinder.models;

public class Users {
        private long id;
        private String fullName;
        private Gender gender;
        private String login;
        private String password;
        private String info;

        public Users(){}
        public Users(long id, String fullName, String login, String password, String info, Gender gender) {
            this.id = id;
            this.fullName = fullName;
            this.login = login;
            this.password = password;
            this.info = info;
            this.gender = gender;
        }

        public long getId() {return id;}
        public void setId(long id) {this.id = id;}

        public String getFullName() {return fullName;}
        public void setFullName(String fullName) {this.fullName = fullName;}

        public String getLogin() {return login;}
        public void setLogin(String login) {this.login = login;}

        public String getPassword() {return password;}
        public void setPassword(String password) {this.password = password;}

        public String getInfo() {return info;}
        public void setInfo(String info) {this.info = info;}

        public Gender getGender() {return gender;}
        public void setGender(Gender gender) {this.gender = gender;}

    @Override
    public String toString() {
        return "id: " + id +
                " ФИО: " + fullName +
                " Пол: " + gender +
                " Логин: " + login +
                " Пароль: " + password +
                " Информация: " + info;
    }
}
