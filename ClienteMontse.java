import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ClienteMontse {
	
	JFrame ventana_chat=null;
	
	//enviar mensaje
	JButton btn_enviar = null;
	JTextField txt_mensaje = null;
	JTextField txt_ip_dm = null;
	JPanel contenedor_btntxt= null;
	
	//cambiar nick 
	JTextField txt_nick = null;
	JButton btn_change = null;
	JPanel contenedor_btnNick = null;
	
	//Area de Mensajes del chat 
	JTextArea area_chat = null;
	JScrollPane scroll = null;
	JPanel contenedor_areachat = null;
	
	Socket socket = null;
	BufferedReader lector = null;
	PrintWriter escritor = null;
	
	public ClienteMontse (){
		
		hacerInterfaz();
	}
	
	public void hacerInterfaz(){
		//JFRAME
		ventana_chat = new JFrame("Cliente");
		
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
		
		//cambiar nick
		txt_nick = new JTextField();
		btn_change = new JButton("Cambiar");
		contenedor_btnNick = new JPanel();
		contenedor_btnNick.setLayout(new GridLayout(1,2));
		contenedor_btnNick.add(txt_nick);
		contenedor_btnNick.add(btn_change);
		
		//ventana 
		ventana_chat.setLayout(new BorderLayout());
		ventana_chat.add(contenedor_areachat, BorderLayout.NORTH);
		ventana_chat.add(contenedor_btntxt, BorderLayout.CENTER);
		ventana_chat.add(contenedor_btnNick, BorderLayout.SOUTH);
		ventana_chat.setSize(500, 500);
		ventana_chat.setVisible(true);
		ventana_chat.setResizable(false);
		ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Thread principal = new Thread(new Runnable(){
			public void run(){
				try{
				socket = new Socket("localhost",9000);
				leer();
				escribir();
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
		});
		
		principal.start();
		
	}
	
	//leer mensajes que llegan
	public void leer(){
		Thread leer_hilo = new Thread(new Runnable(){
			public void run(){
				
				try{
					//leer lo que nos envían 
					lector = new BufferedReader(new InputStreamReader(socket.getInputStream())); //obtener entrada de socket y con lector tenemos metodos para leer la entrada 
						while(true){
							String mensaje_recibido = lector.readLine();
							area_chat.append("Servidor dice: " + mensaje_recibido + "\n");
						}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
		});
		
		leer_hilo.start(); //para ponerse a leer	
	}
	
	public void escribir(){
		Thread escribir_hilo = new Thread(new Runnable(){
			public void run(){
				try{
					
					
					escritor = new PrintWriter(socket.getOutputStream(), true);
					btn_enviar.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
						
						String enviar_mensaje = txt_mensaje.getText();
						escritor.println(enviar_mensaje);
						txt_mensaje.setText("");
						
					}});
					//si no pones true no permite enviar mensajes
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		});
		escribir_hilo.start();
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new ClienteMontse();

	}

}