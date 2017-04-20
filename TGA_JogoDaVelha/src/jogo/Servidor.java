package jogo;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static JogoDaVelha jogo = new JogoDaVelha();
    
    public static void main(String[] args) throws Exception {
        
        int porta = 1234;
        
        ServerSocket servidor = new ServerSocket(porta);
        
        while(true) {
        	Scanner scanner = new Scanner(System.in);
        	
        	String estadoJogo;
        	String nome;
        	char simbolo;
        	
        	System.out.println("Digite seu nome: ");
            nome = scanner.nextLine();
            jogo.setNome(nome);
            double numSimbolo = (Math.random()*2);
            int valor = (int)numSimbolo;
            if(valor == 0){
            	simbolo = 'O';
            }
            else {
            	simbolo = 'X';
            }
            jogo.setSimbolo(simbolo);
        	
        	System.out.println(nome +", aguardando conexao, voce sera o caracter: "+simbolo);
        	Socket conexao = servidor.accept();
        	System.out.println("Conexao foi estabelecida");
        	
        	BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            DataOutputStream saidaCliente = new DataOutputStream(conexao.getOutputStream());
            
            saidaCliente.writeBytes(jogo.getNome()+"\n");
            saidaCliente.writeBytes(valor+"\n");
            
            jogo.montarTabuleiro();

            while(true) {
                estadoJogo = jogo.inserirSimbolo();
                
                saidaCliente.writeBytes(estadoJogo+"\n");
                
                if(jogo.vitoria())
                {
                    System.out.println("O jogador "+nome+ "venceu");
                    break;
                }
                
                estadoJogo = entradaCliente.readLine();
                jogo.atualizarTabuleiro(estadoJogo);

                if(jogo.vitoria())
                {
                    System.out.println("O cliente venceu!!!");
                    break;
                }
            }
        }
    }
}