package com.example.Sop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SopApplication.class, args);
	}
	/*
	Когда место освободилось, сообщение отправляется в rabbit
	Когда не осталось мест и кто-то подписывается на уведомление о свободных местах
	Есть приритетные пользователи, они доплачивают 5% от цены экскурсии, там по очереди кто первый подписался, тому уведомление
	Там да? - да или нет? - нет, или есть 20 минут на согласие
	Если нет, сообщение следующему пользователю
	Если да, выкупает
	Когда приоритетные пользователи закончились, сообщение отправляется всем пользователям, которые подписались
	 */

}
