package ClienteServidor;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String args[]) throws IOException {
        Scanner teclado = new Scanner(System.in);
        int porta=8090;
        DatagramSocket socket = new DatagramSocket(12345);
        InetAddress ip = InetAddress.getByName("25.74.5.8");

        byte[] requisicaoServidor = new byte[200];
        byte[] respostaDoServidor;

        System.out.println("Iniciando requisiçao!");

        String msg = "";
        requisicaoServidor = msg.getBytes();
        DatagramPacket envelopeAEnviar = new DatagramPacket(requisicaoServidor, requisicaoServidor.length, ip, 5000);
        socket.send(envelopeAEnviar);
        System.out.println("Requisicao Concluida");

        while(true) {
            respostaDoServidor = new byte[1024];
            System.out.println("Resposta do Servidor: ");
            DatagramPacket envelopeAReceber = new DatagramPacket(respostaDoServidor, respostaDoServidor.length);
            socket.receive(envelopeAReceber);
            String msgRecebida = new String(envelopeAReceber.getData());
            System.out.println(msgRecebida);

            InetAddress ipUsuario = null;
            if(msgRecebida.charAt(0) == 'i' && msgRecebida.charAt(1) == 'p'){
                String[] ipUsuarioSplited = msgRecebida.split(":");
                String ipUsuarioPosi1 = ipUsuarioSplited[1].trim();
                ipUsuario = InetAddress.getByName(ipUsuarioPosi1);
                iniciarConversacao(teclado, socket, ipUsuario, envelopeAReceber);
            }

            //Enviando resposta ao servidor
            String resposta = teclado.nextLine();
            byte[] respostaAoServidor = new byte[100];
            respostaAoServidor = resposta.getBytes();
            DatagramPacket envelopeResposta = new DatagramPacket(respostaAoServidor, respostaAoServidor.length, ip, 5000);
            socket.send(envelopeResposta);

            if (resposta.equalsIgnoreCase("x")) {
                encerrarConexao(socket);
                System.out.println("Cliente deslogado!");
                break;
            } else if(resposta.equals("0")) {
                System.out.println("Aguardando um usuáriniciar conversaçao...");
                respostaDoServidor = new byte[200];
                envelopeAReceber = new DatagramPacket(respostaDoServidor, respostaDoServidor.length);
                socket.receive(envelopeAReceber);
                String msgChat = new String(envelopeAReceber.getData());
                System.out.println("Mensagem recebida de: " + envelopeAReceber.getAddress().getHostAddress() + " | " + envelopeAReceber.getAddress().getHostName() + ": " + msgChat);
                System.out.println("(Digite [FIM] para encerrar o chat)");
                iniciarConversacao(teclado, socket, ipUsuario, envelopeAReceber);
            }
        }
    }


    public static void encerrarConexao(DatagramSocket socket){
        socket.close();
        System.out.println("Conexão encerrada");
    }

    public static void iniciarConversacao(Scanner teclado, DatagramSocket socket, InetAddress ip, DatagramPacket dp) throws IOException {
        String resposta;
        while(true){
            resposta = teclado.nextLine();
            byte[] respostaAoCliente= new byte[100];
            respostaAoCliente = resposta.getBytes();
            DatagramPacket envelopeResposta = new DatagramPacket(respostaAoCliente, respostaAoCliente.length, ip, 5000);
            System.out.println("Enviando mensagem para o usuário: " + dp.getAddress().getHostName());
            socket.send(envelopeResposta);

            if(resposta.equalsIgnoreCase("fim")){
                break;
            }

            byte[] respostaDoUsuario = new byte[200];
            dp = new DatagramPacket(respostaDoUsuario, respostaDoUsuario.length);
            socket.receive(dp);
            String msgChat = new String(dp.getData());
            System.out.println("Mensagem recebida de: " + dp.getAddress().getHostAddress() + " | " + dp.getAddress().getHostName() + ": " + msgChat);
        }

        System.out.println("Encerrando chat...");
        String msg = "fim";
        byte[] requisicaoServidor = new byte[200];
        requisicaoServidor = msg.getBytes();
        DatagramPacket envelopeAEnviar = new DatagramPacket(requisicaoServidor, requisicaoServidor.length, InetAddress.getByName("25.74.5.8"), 5000);
        socket.send(envelopeAEnviar);
    }
}

