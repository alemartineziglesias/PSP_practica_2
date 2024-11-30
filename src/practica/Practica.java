package practica;

import java.awt.EventQueue;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class Practica extends JFrame 
{
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JList<String> list = new JList<String>();
    private ArrayList<String> listaArchivos = new ArrayList<String>();
    private ArrayList<Reproductor> reproductoresActivos = new ArrayList<Reproductor>();

    public static void main(String[] args) 
    {
        EventQueue.invokeLater(() -> 
        {
            try 
            {
                Practica frame = new Practica();
                frame.setVisible(true);
            } 
            catch(Exception e) 
            {
                e.printStackTrace();
            }
        });
    }

    public Practica() 
    {
        setTitle("Mi Música");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(10, 11, 414, 182);
        listaArchivos.add("Buscando...");
		list.setListData(listaArchivos.toArray(new String[0]));
		listaArchivos.clear();
        list.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent e) 
            {
                if(e.getClickCount() == 2) 
                {
                    reproducirArchivo(list.getSelectedValue());
                }
            }
        });
        contentPane.add(scroll);

        JButton btnReproducir = new JButton("Reproducir");
        btnReproducir.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnReproducir.setBounds(88, 207, 89, 43);
        btnReproducir.addActionListener(new ActionListener()
        {
        	@Override
        	public void actionPerformed(ActionEvent e)
			{
        		reproducirArchivo(list.getSelectedValue());
			}
        });
        contentPane.add(btnReproducir);

        JButton btnParar = new JButton("Parar");
        btnParar.setBounds(248, 207, 89, 43);
        btnParar.addActionListener(new ActionListener()
        {
        	@Override
        	public void actionPerformed(ActionEvent e)
			{
        		 pararArchivo(list.getSelectedValue());
			}
        });
        contentPane.add(btnParar);

        iniciarBusquedaArchivos();
    }

    private void reproducirArchivo(String archivo) 
    {
        for(Reproductor reproductor : reproductoresActivos) 
        {
            if(reproductor.getArchivo().equals(archivo)) 
            {
                System.out.println("El archivo ya se está reproduciendo: " + archivo);
                return;
            }
        }
        Reproductor reproductor = new Reproductor(archivo);
		reproductoresActivos.add(reproductor);
		reproductor.start();
		System.out.println("Reproduciendo: " + archivo);
    }

    private void pararArchivo(String archivo) 
    {
        Reproductor hiloParaDetener = null;
        for (Reproductor reproductor : reproductoresActivos) 
        {
            if (reproductor.getArchivo().equals(archivo)) 
            {
                reproductor.detener();
                hiloParaDetener = reproductor;
                System.out.println("Deteniendo: " + archivo);
            }
        }
        if (hiloParaDetener != null) 
        {
            reproductoresActivos.remove(hiloParaDetener);
        }
    }

    private void iniciarBusquedaArchivos() 
    {
        Thread busquedaThread = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                File[] unidades = File.listRoots();
                for (File unidad : unidades) 
                {
                    encontrarArchivos(unidad);
                }
                if (listaArchivos.isEmpty()) 
                {
                    SwingUtilities.invokeLater(new Runnable() 
                    {
                        @Override
                        public void run() 
                        {
                            actualizarLista(new String[]{"No se ha encontrado ningún archivo"});
                        }
                    });
                } 
                else 
                {
                    SwingUtilities.invokeLater(new Runnable() 
                    {
                        @Override
                        public void run() 
                        {
                            actualizarLista(listaArchivos.toArray(new String[0]));
                        }
                    });
                }
            }
        });
        busquedaThread.start();
    }

    private void actualizarLista(String[] elementos)
    {
        list.setListData(elementos);
    }

    private void encontrarArchivos(File directorio) 
    {
        File[] archivos = directorio.listFiles();
        if (archivos != null)
        {
            for (File archivo : archivos) 
            {
                if (archivo.isDirectory()) 
                {
                    encontrarArchivos(archivo);
                }
                else if (archivo.isFile() && archivo.getName().toLowerCase().endsWith(".mp3")) 
                {
                    listaArchivos.add(archivo.getAbsolutePath().toLowerCase());
                }
            }
        }
    }
}