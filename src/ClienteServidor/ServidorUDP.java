package ClienteServidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServidorUDP {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(5000);
        String msg;
        String msgRecebida;
        List<DatagramPacket> clientes = new ArrayList<>();
        while (true){
            //recebendo uma mensagem do cliente
            byte[] cartaAReceber = new byte[1000];
            DatagramPacket envelopeAReceber = new DatagramPacket(cartaAReceber, cartaAReceber.length);
            socket.receive(envelopeAReceber);
            msgRecebida = new String(envelopeAReceber.getData()).trim();

            if(msgRecebida.equalsIgnoreCase("x")){
                //remove da lista
                for (int i = 0; i < clientes.size(); i++) {
                    if(clientes.get(i).getAddress().equals(envelopeAReceber.getAddress())){
                        clientes.remove(clientes.get(i));
                    }
                }
            }
            boolean esta = false;
            for (int i = 0; i < clientes.size(); i++) {
                if(clientes.get(i).getAddress().equals(envelopeAReceber.getAddress())){
                    esta = true;
                }
            }
            if(esta==false) {
                clientes.add(envelopeAReceber);
                System.out.println("Cliente conectado");
            }
            if(msgRecebida.equalsIgnoreCase("fim") || msgRecebida.equalsIgnoreCase("")) {
                //enviando lista de clientes
                byte[] cartaAEnviar;
                String lista;
                lista = "{";
                int id = 1;
                for (int i = 0; i < clientes.size(); i++) {
                    lista += id + i + "= [Usuário: " + clientes.get(i).getAddress().getHostName()
                            + " | IP: " + clientes.get(i).getAddress().getHostAddress()
                            + " | Porta: " + clientes.get(i).getPort() + "], ";
                }
                lista += "}";
                msg = "Esta é a lista de clientes: " + lista + "\n Digite o número de algum deles caso deseje se comunicar, \n Digite 0 para aguardar comunicação, ou \n Digite x para finalizar aplicação.";
                cartaAEnviar = msg.getBytes();
                DatagramPacket envelopeAEnviar = new DatagramPacket(cartaAEnviar, cartaAEnviar.length, envelopeAReceber.getAddress(), envelopeAReceber.getPort());
                socket.send(envelopeAEnviar);

                //aguardando retorno
                byte[] cartaAReceber2 = new byte[1000];
                DatagramPacket envelopeAreceber2 = new DatagramPacket(cartaAReceber2, cartaAReceber2.length);
                socket.receive(envelopeAreceber2);
                msgRecebida = "";
                msgRecebida = new String(envelopeAreceber2.getData()).trim();
                System.out.println("Mensagem recebida de " + envelopeAreceber2.getAddress().getHostAddress() + " | " + envelopeAreceber2.getAddress().getHostName() + ": " + msgRecebida);
                if (msgRecebida.equalsIgnoreCase("x")) {

                } else if (!msgRecebida.equalsIgnoreCase("0")) {
                    //enviando ip cliente para comunicação direta
                    byte[] cartaAEnviarComCliente;
                    msg = "ip:" + clientes.get(Integer.parseInt(msgRecebida) - 1).getAddress().getHostAddress();
                    cartaAEnviarComCliente = msg.getBytes();
                    DatagramPacket ipCliente = new DatagramPacket(cartaAEnviarComCliente, cartaAEnviarComCliente.length, envelopeAreceber2.getAddress(), envelopeAreceber2.getPort());
                    socket.send(ipCliente);
                }
            }
            if(msgRecebida.equalsIgnoreCase("x")){
                //remove da lista
                for (int i = 0; i < clientes.size(); i++) {
                    if(clientes.get(i).getAddress().equals(envelopeAReceber.getAddress())){
                        clientes.remove(clientes.get(i));
                    }
                }
            }

        }
    }
}
