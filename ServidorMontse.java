import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;

/*
 *InputStream: to read from network
 *
 *OutputSream: write same data across the network to client (which reads it through inputstream)
 * 
 *  Send Data via Socket: get OutputSream from socket and write to OutputStream
 *  
 *  Read Data from Scoket: get its InputStream, and read input from this second stream
 * 
 * */


public class ServidorMontse {
	
	JFrame ventana_chat=null;
	JButton btn_enviar = null;
	JTextField txt_mensaje = null;
	JTextArea area_chat = null;
	JPanel contenedor_areachat = null;
	JPanel contenedor_btntxt= null;
	JScrollPane scroll = null;
	
	//Socket
	ServerSocket servidor = null;
	Socket socket = null;
	BufferedReader lector = null;
	PrintWriter escritor = null;
	
	
	public Servidor(){
		
		hacerInterfaz();
	}
	
	public void hacerInterfaz(){
		ventana_chat = new JFrame("Servidor");
		btn_enviar = new JButton("Enviar");
		txt_mensaje = new JTextField();
		area_chat = new JTextArea(10,12); //10 filas, 12 columna
		area_chat.setEditable(false);
		scroll = new JScrollPane(area_chat);
		contenedor_areachat = new JPanel(); //constructor
		contenedor_areachat.setLayout(new GridLayout(1,1)); //un layout de una fila por columna (nada mas cabe una columna
		contenedor_areachat.add(scroll);
		contenedor_btntxt = new JPanel();
		contenedor_btntxt.setLayout(new GridLayout(1,2));
		contenedor_btntxt.add(txt_mensaje);
		contenedor_btntxt.add(btn_enviar);
		ventana_chat.setLayout(new BorderLayout());
		ventana_chat.add(contenedor_areachat, BorderLayout.NORTH);
		ventana_chat.add(contenedor_btntxt, BorderLayout.SOUTH);
		ventana_chat.setSize(300, 220);
		ventana_chat.setVisible(true);
		ventana_chat.setResizable(false);
		ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Thread principal = new Thread(new Runnable () {
		public void run(){
			try{
			servidor = new ServerSocket(9000);
				while(true){
					socket = servidor.accept(); //socket obtiene getInpuStream y getOutputsream
					leer();
					escribir();
				}
			
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
							area_chat.append("Cliente dice: " + mensaje_recibido+"\n");
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
		new ServidorMontse();

	}

}