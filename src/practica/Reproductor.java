package practica;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Reproductor extends Thread
{
	private String archivo;
	private Player player;
	
	public Reproductor(String archivo)
	{
		this.archivo = archivo;
        FileInputStream fis;
		try 
		{
			fis = new FileInputStream(archivo);
			this.player = new Player(fis);
		}
		catch (FileNotFoundException e) 
		{
			System.out.println("No se encontró el archivo que desea reproducir: " +  e.toString());
		} 
		catch (JavaLayerException e) 
		{
			System.out.println("Error al usar el reproductor: " + e.toString());
		}
	}
	
	public String getArchivo() 
    {
        return archivo;
    }
	
	public void detener() 
    {
        if (player != null) 
        {
            player.close();
        }
    }
	
	@Override
    public void run() 
    {
        try
        {
            if (player != null) 
            {
                player.play();
            }
        } 
        catch (JavaLayerException e) 
        {
            e.printStackTrace();
        }
    }
}
