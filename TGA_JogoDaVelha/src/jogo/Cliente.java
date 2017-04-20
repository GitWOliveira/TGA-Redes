package jogo;

import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente {
    private static JogoDaVelha jogo = new JogoDaVelha();

    public static void main(String[] args) throws Exception {
        
        String host = "127.0.0.1";
        int porta = 1234;
        String estadoJogo;
        String nomeJogador1;
        String simboloJogador1;
        
        System.out.println("Estabelecendo conexao");
        Socket socketCliente = new Socket(host, porta);
        System.out.println("Conexao estabelecida com sucesso");
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Digite seu nome: ");
        jogo.setNome(scanner.nextLine());
        
        BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
        DataOutputStream saidaServidor = new DataOutputStream(socketCliente.getOutputStream());
        
        nomeJogador1 = entradaServidor.readLine();
        simboloJogador1 = entradaServidor.readLine();
        if(simboloJogador1.charAt(0) == '0'){
        	jogo.setSimbolo('X');
        }
        else {
        	jogo.setSimbolo('O');
        }
        System.out.println("Ola "+jogo.getNome()+ " voce sera o caracter: "+jogo.getSimbolo());
        jogo.montarTabuleiro();
        
        while(true) {
        	estadoJogo = entradaServidor.readLine();
            jogo.atualizarTabuleiro(estadoJogo);

            if(jogo.vitoria())
            {
                System.out.println("O jogador "+nomeJogador1+ " venceu!");
                break;
            }
            
            estadoJogo = jogo.inserirSimbolo();
            
            saidaServidor.writeBytes(estadoJogo+"\n");
            
            if(jogo.vitoria())
            {
                System.out.println("Parabens "+jogo.getNome()+ " voce venceu!");
                break;
            }
        }
        
        entradaServidor.close();
        saidaServidor.close();
        socketCliente.close();
        scanner.close();
    }
}