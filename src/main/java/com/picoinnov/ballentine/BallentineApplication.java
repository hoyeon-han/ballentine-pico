package com.picoinnov.ballentine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BallentineApplication {

	public static void main(String[] args) {
		String command = "start";
		if (args.length > 0) {
			command = args[args.length - 1];
		}
		switch (command) {
		case "start":
			SpringApplication.run(BallentineApplication.class, args);
			break;
		case "stop":
			System.exit(0);
			break;
		default:
		}
	}

}
