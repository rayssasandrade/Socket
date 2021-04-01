package ClientesServidoresMulticast;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServidorUDP {
    static List<DatagramPacket> clientes = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Scanner teclado = new Scanner(System.in);
        MulticastSocket grupoMulticast = null;
        InetAddress ipGrupo = null;
        int portaMulticast;

        System.out.println("Qual a porta para a comunicação com os clientes?");
        int portaClientes = teclado.nextInt();
        DatagramSocket socket = new DatagramSocket(portaClientes);

        System.out.println("Qual a porta para a comunicação no grupo multicast?");
        portaMulticast = teclado.nextInt();
        try {
            grupoMulticast = new MulticastSocket(portaMulticast);
            ipGrupo = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
            grupoMulticast.joinGroup(ipGrupo);
        } catch (SocketException e) {}

        String msg;
        String msgRecebida;

        while (true){
            //recebendo uma mensagem do cliente
            byte[] cartaAReceber = new byte[1000];
            DatagramPacket envelopeAReceber = new DatagramPacket(cartaAReceber, cartaAReceber.length);
            socket.receive(envelopeAReceber);
            msgRecebida = new String(envelopeAReceber.getData()).trim();
            enviarParaGrupo(msgRecebida, grupoMulticast, ipGrupo, portaMulticast);

            if(msgRecebida.equalsIgnoreCase("x")){
                removerDaLista(envelopeAReceber.getAddress());
            }

            if(estaNaLista(envelopeAReceber.getAddress())==false) {
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
                removerDaLista(envelopeAReceber.getAddress());
            }

        }
    }

    public static void enviarParaGrupo(String msg, MulticastSocket grupoMulticast, InetAddress ipGrupo, int portaMulticast){
        DatagramPacket dtgrm = new DatagramPacket(msg.getBytes(),
                msg.length(), ipGrupo, portaMulticast);
        try {
            grupoMulticast.send(dtgrm);
        } catch (IOException e) { }
    }

    public static boolean estaNaLista(InetAddress enderecoCliente){
        boolean esta = false;
        for (int i = 0; i < clientes.size(); i++) {
            if(clientes.get(i).getAddress().equals(enderecoCliente)){
                esta = true;
            }
        }
        return esta;
    }

    public static void removerDaLista(InetAddress enderecoCliente){
        for (int i = 0; i < clientes.size(); i++) {
            if(clientes.get(i).getAddress().equals(enderecoCliente)){
                clientes.remove(clientes.get(i));
            }
        }
    }
}
