/*
Servidor UDP que recibirá datagramas de clientes, en la parte de datos vendrá un string.
Se responderá con el String transformado a mayúsculas al cliente que lo haya mandadado
Atenderá a todos los clientes que manden un string en el orden que los vaya recibiendo
La aplicación escucha datagramas en el puerto 5000
*/

import java.io.*;
import java.net.*;

public class Servidor{
	
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
  public static void main(String[] args){
    DatagramSocket yo = null; // Socket del servidor para recibir datagramas
    DatagramPacket paquete; // Paquete para recibir los datos
    InetAddress dirCliente = null;// Dirección IP del cliente que manda los datos
    int puertoCliente; // Puerto del cliente que manda los datos
    byte[] buffer = new byte[80]; // Memoria donde se pondrán los datos recibidos (arreglo de bytes)
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

    while(true){
      buffer = new byte[80]; // Crear el buffer para almacenar el string recibido como arreglo de bytes
      paquete = new DatagramPacket(buffer, buffer.length); // Crear paquete para recibir el datagrama
      try{
        yo.receive(paquete);
      }catch(IOException e){
              System.out.println(e.getMessage());
              System.exit(1);
      }
      recibido = new String(paquete.getData()); // Extraer los datos recibidos y transformalos a String
      dirCliente = paquete.getAddress();// Obtener la dirección del cliente
      puertoCliente = paquete.getPort(); // Obtener el puerto del cliente

      // Imprime la dirección y puerto del cliente y el string mandado (recibido)
      //System.out.println(dirCliente.toString()+"("+puertoCliente+") >>"+recibido);

      //aMandar = new String(recibido.toUpperCase()); // Transformamos a mayúsculas el string recibido
      aMandar = parseMessage(recibido, dirCliente.toString());
      buffer = new byte[80]; // Crear el arreglo de bytes que almacenará el string a transmitir
      buffer = aMandar.getBytes();      // Transformamos el string a arreglo de bytes
      // Llenamos el paquete con los bytes a enviar y el destino (dirIP y puerto)
      paquete = new DatagramPacket(buffer,buffer.length, dirCliente, puertoCliente);
      try{
              yo.send(paquete);
      }catch(IOException e){
              System.out.println(e.getMessage());
              System.exit(1);
      }
    }
  //yo.close();
  }
}
