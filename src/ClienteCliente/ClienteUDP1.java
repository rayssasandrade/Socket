package ClienteCliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteUDP1 {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(5000);
        String msg;
        String msgRecebida;
        InetAddress ip = InetAddress.getByName("25.78.99.1");
        do{
            System.out.println(  InetAddress.getLocalHost().getHostAddress() + " | " + InetAddress.getLocalHost().getHostName() + ": ");
            Scanner teclado = new Scanner(System.in);
            msg = teclado.nextLine();
            //enviando uma mensagem para o clienteUDP2
            byte[] cartaAEnviar = new byte[100];
            cartaAEnviar = msg.getBytes();
            DatagramPacket envelopeAEnviar = new DatagramPacket(cartaAEnviar, cartaAEnviar.length, ip, 5000);
            socket.send(envelopeAEnviar);
            if(msg.equals("fim")){
                break;
            }
            //recebendo uma mensagem do clienteUDP2
            byte[] cartaAReceber = new byte[100];
            DatagramPacket envelopeAReceber = new DatagramPacket(cartaAReceber, cartaAReceber.length);
            socket.receive(envelopeAReceber);
            msgRecebida = new String(envelopeAReceber.getData());
            System.out.println("Mensagem recebida de " + envelopeAReceber.getAddress().getHostAddress() + " | " + envelopeAReceber.getAddress().getHostName() + ": " + msgRecebida);
        } while (!msgRecebida.equals("fim"));

        //fechando comunicação
        socket.close();
    }

}
