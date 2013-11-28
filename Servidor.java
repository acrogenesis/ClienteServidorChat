/*
Servidor UDP que recibirá datagramas de clientes, en la parte de datos vendrá un string.
Se responderá con el String transformado a mayúsculas al cliente que lo haya mandadado
Atenderá a todos los clientes que manden un string en el orden que los vaya recibiendo
La aplicación escucha datagramas en el puerto 5000
*/

import java.io.*;
import java.net.*;
import java.util.*;

class Clientes{

  private String nickname;
  private InetAddress dir;
  private int puerto;
  public boolean status;

  public Clientes(){

  }

  public void setNickname(String s){
    nickname = s;
  }
  public void setInetAddress(InetAddress d){
    dir = d;
  }

  public void setPuerto(int d){
    puerto = d;
  }

  public void setStatus(boolean s){
	
	status = s;
}
  public String getNickname(){
    return nickname;
  }
  public InetAddress getDir(){
    return dir;
  }

  public int getPuerto(){
    return puerto;
  }

  public boolean getStatus(){
	return status;
	}	
}

public class Servidor{

  public static ArrayList<Clientes> usuarios = new ArrayList<Clientes>();
  public static HashMap<String, InetAddress> map = new HashMap<String, InetAddress>();


  static String parseMessage(String s , String ip){
    String message;
    String messageData;
    String nickname;
    String[] parts = s.split("π");
    nickname = parts[0];
    messageData = parts[1];
    //Nickname(ipaddress): mensaje
    message = nickname + "(" + ip + "): " + messageData;
    return message;
  }

  static String getNickname(String s){
    String[] parts = s.split("π");
    String nickname = parts[0];
    return nickname;
  }

public static void sendPrivateMessage(DatagramSocket yo, String aMandar, String aQuien)
{
	DatagramPacket paquete;
	byte[] buffer;
	buffer = new byte[100]; // Crear el arreglo de bytes que almacenará el string a transmitir
	buffer = aMandar.getBytes();     // Transformamos el string a arreglo de bytes
	boolean enviado = false;
	for(int i = 0; i < usuarios.size(); i++){
	      Clientes userARecibir = usuarios.get(i);
	      //System.out.println("cant usuarios: "+ usuarios.size() +" userARecibir: " + userARecibir.getNickname() + " aMandar: " + niki);
	      if(userARecibir.getNickname() == aQuien && userARecibir.getStatus())
			{
				InetAddress _dmDir = userARecibir.getDir();
				int _dmPuerto = userARecibir.getPuerto();
				paquete = new DatagramPacket(buffer,buffer.length, _dmDir, _dmPuerto);
				      try{
				        yo.send(paquete);
						System.out.println("sending message");
				      }catch(IOException e){
				        System.out.println(e.getMessage());
				        System.exit(1);
				      }
				enviado= true; //usuario si existe
				break;
			}
	}
	
	if(enviado){ 
		System.out.println("Mensaje privado fue enviado");
	}
	
}

  public static void sendMessage(DatagramSocket yo, String aMandar, String niki)
{
    DatagramPacket paquete;
    byte[] buffer;
    buffer = new byte[100]; // Crear el arreglo de bytes que almacenará el string a transmitir
    buffer = aMandar.getBytes();     // Transformamos el string a arreglo de bytes
    for(int i = 0; i < usuarios.size(); i++){
      Clientes userARecibir = usuarios.get(i);
      //System.out.println("cant usuarios: "+ usuarios.size() +" userARecibir: " + userARecibir.getNickname() + " aMandar: " + niki);
      if(userARecibir.getNickname() != niki && userARecibir.getStatus()){
        paquete = new DatagramPacket(buffer,buffer.length, userARecibir.getDir(), userARecibir.getPuerto());
      try{
        yo.send(paquete);
        System.out.println("sending message: " + aMandar);
      }catch(IOException e){
        System.out.println(e.getMessage());
        System.exit(1);
      }
    }
  }
}


  public static void main(String[] args){
    DatagramSocket yo = null; // Socket del servidor para recibir datagramas
    DatagramPacket paquete; // Paquete para recibir los datos
    InetAddress dirCliente = null;// Dirección IP del cliente que manda los datos
    int puertoCliente; // Puerto del cliente que manda los datos
    byte[] buffer = new byte[100]; // Memoria donde se pondrán los datos recibidos (arreglo de bytes)
    String recibido; // String para guardar los bytes recibidos transformados a caracteres
    String aMandar; // El string que se responderá al cliente, se transformará a bytes 
    final int PUERTO = 5000; // Puerto en cual se recibirán los datagramas.

    // Crear el socket que escuchará en el PUERTO 
    try{
      yo = new DatagramSocket(PUERTO);
    }catch(SocketException e){
      System.out.println(e.getMessage());
      System.exit(1);
    }
    // Técnicamente todavía no escucha, si no hasta por datagramas sino hasta que se lee del socket (receive)
    // Pero es un buen lugar para poner el letrero, ya que lo primero que se hará en el ciclo es escuchar
    System.out.println("Socket escuchando en el puerto "+PUERTO); 
    boolean first = true;
    boolean esta = false;
    while(true){
      //reiniciar variable viejo
      buffer = new byte[100]; // Crear el buffer para almacenar el string recibido como arreglo de bytes
      paquete = new DatagramPacket(buffer, buffer.length); // Crear paquete para recibir el datagrama

      try{
        yo.receive(paquete);
      }catch(IOException e){
        System.out.println(e.getMessage());
        System.exit(1);
      }

      recibido = new String(paquete.getData()).trim(); // Extraer los datos recibidos y transformalos a String
      dirCliente = paquete.getAddress();// Obtener la dirección del cliente
      puertoCliente = paquete.getPort(); // Obtener el puerto del cliente
		System.out.println(recibido);
	//checar si es paquete de registro
	if(recibido.startsWith(".registrar")){
		
		String[] info = recibido.split(" ");
		
		if(map.get(info[1]) == null) //nickname ya esta siendo usado
		{
			map.put(info[1], dirCliente);
			Clientes c = new Clientes();
			c.setInetAddress(dirCliente);
			c.setPuerto(puertoCliente);
			c.setNickname(info[1]);
			c.setStatus(true);
			usuarios.add(c);
			System.out.println("Se agrego al usuario " + info[1] + " " + c.getDir().toString());
			String registrado = "Bienvenido al Chat " + info[1];
			byte [] register = new byte[100];
			register = registrado.getBytes();
			DatagramPacket registro = new DatagramPacket(register, register.length, dirCliente, puertoCliente);
			try{
				yo.send(registro);
			}catch(IOException ex){
				System.out.println(ex.getMessage());
				System.exit(1);
			}
			sendMessage(yo, "Usuario " + info[1] + " inicio sesion", info[1]);
			
		}
		else{
			String _noRegistro = "Nickname ya existe!!";
			byte[] _noRegister = new byte[100];
			_noRegister = _noRegistro.getBytes();
			DatagramPacket _noPaquete = new DatagramPacket(_noRegister, _noRegister.length, dirCliente, puertoCliente);
			try{
							yo.send(_noPaquete);
			}catch(IOException ex){
							System.out.println(ex.getMessage());
							System.exit(1);
			}
			
		}
		
		System.out.println(usuarios.size() + "\n");
	}else{
		System.out.println("Usuarios de chat " + usuarios.size()+ "\n");
      // Imprime la dirección y puerto del cliente y el string mandado (recibido)
      System.out.println("recibi: " + dirCliente.toString()+" "+recibido);
		System.out.println(recibido);
		if(recibido.startsWith(".dm")) //es un mensaje privado
		{   System.out.println("privado");
			String[] info = recibido.split(" ");
			String _aQuien = info[1];
			String _dm = info[2];
			String mandar = parseMessage(_dm, dirCliente.toString());
			sendPrivateMessage(yo, mandar.trim(), _aQuien);
		}else{
				System.out.println("aqui");
     			 //aMandar = new String(recibido.toUpperCase()); // Transformamos a mayúsculas el string recibido
      			aMandar = parseMessage(recibido, dirCliente.toString());

      			//un for para enviar paquete a todos los usuarios conectados
      			sendMessage(yo, aMandar.trim(), getNickname(aMandar));
		}
		}
    }
  //yo.close();
  }
}
