
package ch.hearc.p2.java.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ch.hearc.p2.java.model.Equation;

public class ControllerIO
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public ControllerIO()
		{
		//Rien
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void save(Equation equation, File file) throws IOException
		{
		file.createNewFile();

		FileOutputStream fos = new FileOutputStream(file.toString()+".nso");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		oos.writeObject(equation);

		oos.close();
		bos.close();
		fos.close();
		}

	public Equation load(File file) throws ClassNotFoundException, IOException
		{
		if (!file.exists() || file.isDirectory()) { throw new IOException("File is directory or doesn't exists.");}	//verifier

		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(bis);

		Equation equation = (Equation)ois.readObject();

		ois.close();
		bis.close();
		fis.close();

		return equation;
		}
	}
