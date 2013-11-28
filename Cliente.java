import javax.swing.*;  
import java.awt.*;  
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.io.IOException;

public class Cliente {
	
		JFrame ventana_chat=null;
		
		//conectar sesion 
		static JButton btn_conectar = null;
		JPanel contenedor_btnConnect = null;
		static String nickname = null;
			
		//enviar mensaje
		static JButton btn_enviar = null;
		static JTextField txt_mensaje = null;
		static JTextField txt_ip_dm = null;
		JPanel contenedor_btntxt= null;
		
		//Area de Mensajes del chat 
		static JTextArea area_chat = null;
		JScrollPane scroll = null;
		JPanel contenedor_areachat = null;
		
		//conexión con socket 
		DatagramSocket yo = null;		// Socket del cliente para comunicarse con el servidor
		InetAddress dirServidor = null;	// Dirección IP del servidor
		DatagramPacket paquete;	
		final int PUERTO = 5000; //puerto para con servidor
		
		
		ServerSocket socketDM = null;
		Socket socket = null;
		BufferedReader lector = null;
		PrintWriter escritor = null;
		byte[] buffer = new byte[80];		// Memoria donde se pondrá el string (bytes) a mandar
		
		public Cliente(){
			hacerInterfaz();
		}
		
		public void hacerInterfaz(){
				//JFRAME
				ventana_chat = new JFrame("Chat");
				
				//conectar 
				btn_conectar = new JButton("Conectar");
				contenedor_btnConnect = new JPanel();
				contenedor_btnConnect.setLayout(new GridLayout(1,1));
				contenedor_btnConnect.add(btn_conectar);
					
				
				//ver mensajes
				area_chat = new JTextArea(25,12); //10 filas, 12 columna
				area_chat.setEditable(false);
				scroll = new JScrollPane(area_chat);
				contenedor_areachat = new JPanel(); //constructor
				contenedor_areachat.setLayout(new GridLayout(1,1)); //un layout de una fila por columna (nada mas cabe una columna
				contenedor_areachat.add(scroll);
				
				//escribir mensaje
				txt_mensaje = new JTextField();
				txt_ip_dm = new JTextField();
				btn_enviar = new JButton("Enviar"); // boton enviar 
				contenedor_btntxt = new JPanel();
				contenedor_btntxt.setLayout(new GridLayout(1,3));
				contenedor_btntxt.add(txt_mensaje);
				contenedor_btntxt.add(txt_ip_dm);
				contenedor_btntxt.add(btn_enviar);
				
				//ventana 
				ventana_chat.setLayout(new BorderLayout());
				ventana_chat.add(contenedor_areachat, BorderLayout.NORTH);
				ventana_chat.add(contenedor_btntxt, BorderLayout.CENTER);
				ventana_chat.add(contenedor_btnConnect, BorderLayout.SOUTH);
				ventana_chat.setSize(500, 500);
				ventana_chat.setVisible(true);
				ventana_chat.setResizable(false);
				ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
			}
			
	public static void conectar(){
		
		btn_conectar.addActionListener(new ActionListener()
		{
					
					public void actionPerformed(ActionEvent e)
					{
						String nombre = JOptionPane.showInputDialog("Nickname: ");
						nickname = nombre;
						System.out.println(nombre);
					}
					
		});
	}
		
	
	public static void main(String[] args) {
		
		new Cliente();
		conectar();
		
	}
}