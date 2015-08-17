
package ch.hearc.p2.java.view.strings;

public class Strings
	{

	public static void setLanguage(LANGUAGE language)
	{
	switch(language)
	{
	case FR:
		WELCOME = "Bienvenue";
		LABEL_NEW = "Ici, vous pouvez créer un projet si vous n'en possédez aucun :";
		LABEL_LOAD = "Ici vous pouvez charger des anciens projet";
		BUTTON_NEW = "Créer";
		BUTTON_LOAD = "Charger";
		break;

	default:
		WELCOME = "Welcome";
		LABEL_NEW = "Here you can create a project if you don't have any yet :";
		LABEL_LOAD = "Here you can load old project :";
		BUTTON_NEW = "Create";
		BUTTON_LOAD = "Charge";
		break;

	}

	}

	public static enum LANGUAGE{
		FR, EN
	}

	public static String WELCOME = "Welcome";
	public static String LABEL_NEW = "Here you can create a project if you don't have any yet :";
	public static String LABEL_LOAD = "Here you can load old project :";
	public static String BUTTON_NEW = "Create";
	public static String BUTTON_LOAD = "Charge";

	}

