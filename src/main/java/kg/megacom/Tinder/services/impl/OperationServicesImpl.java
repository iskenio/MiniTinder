package kg.megacom.Tinder.services.impl;

import kg.megacom.Tinder.models.Gender;
import kg.megacom.Tinder.models.Users;
import kg.megacom.Tinder.services.OperationServices;
import kg.megacom.Tinder.services.OrderServices;
import kg.megacom.Tinder.services.UserServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OperationServicesImpl implements OperationServices {
    Scanner scn = new Scanner(System.in);
    UserServices usersService = new UserServicesImpl();
    OrderServices orderService =new OrderServicesImpl();

    String border = "\n---------------------------------------------";

    @Override
    public void registration() {
        System.out.println("\n------Регистрация------");
        Users user = new Users();

        System.out.print("ФИО: ");
        user.setFullName(scn.nextLine());

        System.out.print("пол (M/F): ");
        char male = scn.next().charAt(0);
        while (!((male == 'M') | (male == 'F'))) {
            System.out.print("\nВведит 'M'(муж.) or 'F'(жен.)! \nпол (M/F): ");
            male = scn.next().charAt(0);
        }

        switch (male) {
            case ('M'):
                user.setGender(Gender.MALE);
                break;
            case ('F'):
                user.setGender(Gender.FEMALE);
                break;
            default:break;
        }

        System.out.print("login: ");
        user.setLogin(scn.next());
        scn.nextLine();

        System.out.print("password: ");
        user.setPassword(scn.next());
        scn.nextLine();

        System.out.print("info: ");
        user.setInfo(scn.nextLine());

        usersService.save(user);

        System.out.println(border + "\nРегистрация прошла успешно!" + border);
    }

    @Override
    public void signIn() {
        System.out.println("\n---------Вход----------");
        System.out.print("логин: ");
        String login = scn.next();
        System.out.print("пароль: ");
        String password = scn.next();

        for (int i = 3; i > 0; i--) {
            if (!usersService.logPass(login, password)) {
                System.out.println("\nНеверный логин или пароль! Попробуйте снова!!! \nУ вас попыток  -  " + i);
                System.out.print("\nлогин: ");
                login = scn.next();
                System.out.print("пароль: ");
                password = scn.next();
            }
        }

        if (usersService.logPass(login,password)) {
            System.out.println( "\n---------------MiniTINDER-----------------" +
                    "\nДобро пожаловать " + usersService.welcome(login, password).getFullName());

            String male;
            Gender gender;
            long senderId = usersService.welcome(login, password).getId();
            if (usersService.welcome(login, password).getGender().equals(Gender.MALE)) {
                male = "Девушки";
                gender = Gender.FEMALE;
            } else {
                male = "Парни";
                gender = Gender.MALE;
            }

            outerloop:
            while (true) {
                System.out.println("\n1. " + male +
                        "\n2. Взаимно" +
                        "\n3. В ожидании" +
                        "\n4. Заявки" +
                        "\n5. Не взаимно" +
                        "\n6. Выход");
                System.out.print("\nОтвет: ");

                switch (scn.nextInt()) {
                    case 1:
                        System.out.println("\n------------"+male+"-------------");
                        List<Long> idUsers = new ArrayList<>(usersService.idUsers(senderId));
                        if(!(usersService.findMale(gender, idUsers).isEmpty())) {
                            usersService.findMale(gender, idUsers).forEach(n -> System.out.println("\nID: *" + n.getId() +
                                    "\nФИО:  " + n.getFullName() +
                                    "\nПол:  " + n.getGender() +
                                    "\nИнфо: " + n.getInfo()));

                            outerloop2:
                            while (true) {
                                System.out.print(border + "\nОтправить заявку (Д/Н): \nОтвет: ");
                                char choose = scn.next().charAt(0);

                                while (!(choose == 'Д' || choose == 'Н')) {
                                    System.out.print("\nВведите верную команду Д(да) или Н(нет) \nОтвет: ");
                                    choose = scn.next().charAt(0);
                                }

                                if (choose == 'Д') {
                                    System.out.print("Кому!\nID: ");
                                    long recipId = scn.nextInt();

                                    if (orderService.checkId(senderId, recipId)) {
                                        System.out.println("\nВы уже отправяли заявку этому человечку!");
                                    } else {
                                        System.out.println(border + "\nЗаяка отправлена!");
                                        orderService.likeById(senderId, recipId);
                                    }

                                } else break outerloop2;
                            }
                        }else System.out.println(border + "В рекомендациях пусто!");

                        break;

                    case 2:
                        System.out.println("\n-----------------Взимно-------------------");
                        if (!orderService.mutually(senderId).isEmpty()) {
                            orderService.mutually(senderId).forEach(n -> System.out.println("ID: *" + n.getId() +
                                    "\nФИО:  " + n.getFullName() +
                                    "\nПол:  " + n.getGender() +
                                    "\nИнфо: " + n.getInfo() + border));
                        } else System.out.println("Нет ответа!" + border);
                        break;

                    case 3:
                        System.out.println("\n----------------Ожидание------------------");
                        if (!orderService.inWaiting(senderId).isEmpty()) {

                            orderService.inWaiting(senderId).forEach(n -> System.out.println("ID: *" + n.getId() +
                                    "\nФИО:  " + n.getFullName() +
                                    "\nПол:  " + n.getGender() +
                                    "\nИнфо: " + n.getInfo() + border));

                        } else System.out.println("В ожидании пусто!" + border);
                        break;

                    case 4:
                        System.out.println("\n-----------------Заявки-------------------");
                        if (!orderService.proposal(senderId).isEmpty()) {
                            orderService.proposal(senderId).forEach(n -> System.out.println("ID: *" + n.getId() +
                                    "\nФИО:  " + n.getFullName() +
                                    "\nПол:  " + n.getGender() +
                                    "\nИнфо: " + n.getInfo() + border));

                            outerloop3:
                            while (true) {
                                System.out.print("\nОтветить на заявку (Д/Н): \nОтвет: ");
                                char choose = scn.next().charAt(0);

                                while (!(choose == 'Д' || choose == 'Н')) {
                                    System.out.println("\nВведите верную команду Д(да) или Н(нет) \nОтвет: ");
                                    choose = scn.next().charAt(0);
                                }

                                if (choose == 'Д') {
                                    System.out.print("\nКому? - ID: ");
                                    long recipId = scn.nextInt();
                                    System.out.print("\n1. Принять \n2. Отклонить \nОтвет: ");
                                    int answer = scn.nextInt();

                                    while (!(answer == 1 | answer == 2)) {
                                        System.out.print("Введите верную команду 1 или 2! \n" +
                                                "\n1. Принять \n2. Отклонить \nОтвет: ");
                                        answer = scn.nextInt();
                                    }

                                    if (answer == 1) {
                                        System.out.println(border + "\nВзаимность проявлена! " + border);
                                        orderService.toAccept(1, senderId, recipId);
                                    } else {
                                        System.out.println(border + "\nОтказ" + border);
                                        orderService.toAccept(2, senderId, recipId);
                                    }
                                    break outerloop3;
                                }
                            }
                        }
                        System.out.println("Заявок нет!" + border);

                        break;
                    case 5:
                        System.out.println("\n---------------Отклонили------------------");
                        if (!orderService.notMutually(senderId).isEmpty()) {
                            orderService.notMutually(senderId).forEach(n -> System.out.println("ID: *" + n.getId() +
                                    "\nФИО:  " + n.getFullName() +
                                    "\nПол:  " + n.getGender() +
                                    "\nИнфо: " + n.getInfo() + border));
                        } else System.out.println("............." + border);
                        break;

                    case 6:
                        break outerloop;

                    default:
                        System.out.println("Неверная команда! ");
                }
            }
        } else System.out.println(border + "Попробуйте снова через 5мин." + border);
    }
}
