package MultiCastUDP;

import java.io.IOException;
import java.net.*;

public class Servidor {
    public static void main(String args[]) throws IOException {
        int porta = 6868;
        InetAddress ipGrupo = null;
        MulticastSocket s = null;
        String msg = "mensagem default";

        // junta-se a um grupo de Multicast
        try {
            ipGrupo = InetAddress.getByName("224.225.226.227");
            s = new MulticastSocket(porta);
            s.joinGroup(ipGrupo);
        } catch (SocketException e) {

        }

        // le continuamente as mensagens
        byte[] buf = new byte[1512];
        while (true) {
            DatagramPacket recebido = new DatagramPacket(buf, buf.length);
            try {
                s.setSoTimeout(120000);
                s.receive(recebido);
            } catch (SocketTimeoutException e) {
                break;
            } catch (IOException e) {
            }
            String str = new String(recebido.getData());
            System.out.println("(" + recebido.getAddress().getHostAddress() +
                    ":" + recebido.getPort() + ") << " + str.trim());
        }
    }
}
