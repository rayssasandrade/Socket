package ClienteCliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class ClienteUDP2 {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(5000);
        String msg;
        String msgRecebida;
        InetAddress ip = InetAddress.getByName("25.78.99.1");
        do {
            //recebendo uma mensagem do clienteUDP1
            byte[] cartaAReceber = new byte[100];
            DatagramPacket envelopeAReceber = new DatagramPacket(cartaAReceber, cartaAReceber.length);
            socket.receive(envelopeAReceber);
            msgRecebida = new String(envelopeAReceber.getData());

            if(msgRecebida.equals("fim")){
                break;
            }
            System.out.println("Mensagem recebida de: " + envelopeAReceber.getAddress().getHostAddress() + " | " + envelopeAReceber.getAddress().getHostName() + ": "+ msgRecebida);


            //enviando uma mensagem para o clienteUDP1
            System.out.println(InetAddress.getLocalHost().getHostAddress() + " | " + InetAddress.getLocalHost().getHostName());
            Scanner teclado = new Scanner(System.in);
            msg = teclado.nextLine();


            byte[] cartaAEnviar = new byte[100];
            cartaAEnviar = msg.getBytes();
            DatagramPacket envelopeAEnviar = new DatagramPacket(cartaAEnviar, cartaAEnviar.length, ip, 5000);
            socket.send(envelopeAEnviar);
        } while (!msg.equals("fim"));

        //fechando comunicação
        socket.close();
    }
}