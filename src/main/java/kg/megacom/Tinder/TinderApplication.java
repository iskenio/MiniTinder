package kg.megacom.Tinder;

import kg.megacom.Tinder.services.OperationServices;
import kg.megacom.Tinder.services.impl.OperationServicesImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

public class TinderApplication {

	public static void main(String[] args) {

		SpringApplication.run(TinderApplication.class, args);


		Scanner scn = new Scanner(System.in);
		OperationServices operationService = new OperationServicesImpl();

		System.out.println("\n---------MiniTINDER-----------");

		while (true)  {
			System.out.println("\n1. Создать аккаунт \n2. Войти \n");
			System.out.print("Ответ: ");
			int choose = scn.nextInt();

			while (!(choose == 1 | choose == 2)){
				System.out.print("\nВыберите правильную команду!\nОтвет: ");
				choose = scn.nextInt();
			}

			switch (choose) {
				case 1:
					operationService.registration();
					break;
				case 2:
					operationService.signIn();
					break;
			}
		}
	}

}
