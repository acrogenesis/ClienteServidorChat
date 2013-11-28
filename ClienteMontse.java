import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ClienteMontse {
	
	JFrame ventana_chat=null;
	
	//conectar
	JTextField txt_nick = null;
	JButton btn_conectar = null;
	JPanel contenedor_btnConnect = null;
		
	//enviar mensaje
	JButton btn_enviar = null;
	JTextField txt_mensaje = null;
	JTextField txt_ip_dm = null;
	JPanel contenedor_btntxt= null;
	
	//Area de Mensajes del chat 
	JTextArea area_chat = null;
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
	
	//Sesion
	String nickname = null;
	
	public ClienteMontse (){
		
		hacerInterfaz();
	}
	
	public void hacerInterfaz(){
		//JFRAME
		ventana_chat = new JFrame("Chat");
		
		//conectar 
		btn_conectar = new JButton("Conectar");
		txt_nick = new JTextField();
		contenedor_btnConnect = new JPanel();
		contenedor_btnConnect.setLayout(new GridLayout(1,2));
		contenedor_btnConnect.add(txt_nick);
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
		
		conectar();
		
		
	}
	
	
	//leer mensajes que llegan
	/*public void leer(){
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
	}*/
	
	public void conectar()
	{
		
		btn_conectar.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				
				if(txt_nick.getText() != null)
				{
					
					nickname = txt_nick.getText();
					System.out.println(nickname);
					
				}
			}
			
		});
	}
	
	public void escribir(){

				try{

					escritor = new PrintWriter(socket.getOutputStream(), true);
					
					btn_enviar.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e){
							
							//si el mensaje no es un direct message 
							if(txt_ip_dm.getText() == null && txt_ip_dm.getText() == ""){
								
								
								String text = txt_mensaje.getText();
								txt_mensaje.setText("");
								paquete = new DatagramPacket(buffer, buffer.length, dirServidor, PUERTO);
							}
						}
					});
					//si no pones true no permite enviar mensajes
				}catch(Exception e){
					e.printStackTrace();
				}
		
	}
  

	String appendNickname(String nick, String message){
    
		String newmessage;
    	newmessage = nick + "π" + "message";
    	return newmessage;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new ClienteMontse();

	}

}
