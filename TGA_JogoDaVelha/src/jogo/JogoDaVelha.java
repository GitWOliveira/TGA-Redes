package jogo;

import java.io.*;

public class JogoDaVelha {
    private char[][] tabuleiro = new char[3][3];
    private String nome;
    private char simbolo;
    
    public JogoDaVelha() {
    	this.nome = " ";
        this.simbolo = ' ';
    }
    
    public JogoDaVelha(String nome, char simbolo) {
    	this.nome = nome;
        this.simbolo = simbolo;
    }
    
    public void montarTabuleiro() {
        for(int i=0;i<3;i++){
        	for(int j=0;j<3;j++){
            	tabuleiro[i][j] = ' ';
            }
        }
    }
    
    public void mostrarTabuleiro() {
        System.out.println("Estado do TABULEIRO: \n");
        
        for(int i=1;i<10;i++){
        	if((i==3)||(i==6)){
            	System.out.println(i+"\n----------");
            }
        	else if(i==9){
                System.out.println(i);
        	}
            else {
            	System.out.print(i+" | ");
            }
        }
        
        System.out.println("Estado atual do TABULEIRO: \n");
        
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if((i==0 && j==2)||(i==1 && j==2)){
                    System.out.println(tabuleiro[i][j]+"\n----------");
                }
                else if (i==2 && j==2){
                    System.out.println(tabuleiro[i][j]);
                }
                else{
                    System.out.print(tabuleiro[i][j]+" | ");
                }
            }
        }
    }
    
    public void setNome(String nome){
    	this.nome = nome;
    }
    
    public String getNome(){
    	return this.nome;
    }
    
    public void setSimbolo(char simbolo){
    	this.simbolo = simbolo;
    }
    
    public char getSimbolo(){
    	return this.simbolo;
    }
    
    public String inserirSimbolo() throws Exception {
        BufferedReader doUsuario = new BufferedReader(new InputStreamReader(System.in));
        String estadoTabuleiro = "";
        String campo;
        int numeroCampo; 
        
            do {
            	System.out.print(this.getNome()+", eh a sua vez(1 a 9 para inserir, 0 para sair): \n\n");
            	campo = doUsuario.readLine();
            	try {
                	numeroCampo = Integer.parseInt(campo);
                	if(numeroCampo == 0){
                		System.exit(0);
                	}
                	else if(numeroCampo > 9){
                		System.out.println("Jogada invalida. Jogue Novamente.");
                	}
                	else if(numeroCampo < 1){
                		System.out.println("Jogada invalida. Jogue Novamente.");
                	}
                	if(numeroCampo == 1){
                		if(tabuleiro[0][0] == ' ')
                			tabuleiro[0][0] = this.getSimbolo();
                		else{
                			System.out.println("Jogada invalida. Jogue Novamente.");
                		}
                	}
                	else if(numeroCampo == 2){
                		if(tabuleiro[0][1] == ' '){
                			tabuleiro[0][1] = this.getSimbolo();
                			break;
                		}
                		else{
                			System.out.println("Jogada invalida. Jogue Novamente.");
                		}
                	}
                	else if(numeroCampo == 3){
                		if(tabuleiro[0][2] == ' '){
                			tabuleiro[0][2] = this.getSimbolo();
                			break;
                		}
                		else{
                			System.out.println("Jogada invalida. Jogue Novamente.");
                		}
                	}
                	else if(numeroCampo == 4){
                		if(tabuleiro[1][0] == ' '){
                			tabuleiro[1][0] = this.getSimbolo();
                			break;
                		}
                		else{
                			System.out.println("Jogada invalida. Jogue Novamente.");
                		}
                	}
                	else if(numeroCampo == 5){
                		if(tabuleiro[1][1] == ' '){
                			tabuleiro[1][1] = this.getSimbolo();
                			break;
                		}
                		else{
                			System.out.println("Jogada invalida. Jogue Novamente.");
                		}
                	}
                	else if(numeroCampo == 6){
                		if(tabuleiro[1][2] == ' '){
                			tabuleiro[1][2] = this.getSimbolo();
                			break;
                		}
                		else{
                			System.out.println("Jogada invalida. Jogue Novamente.");
                		}
                	}
                	else if(numeroCampo == 7){
                		if(tabuleiro[2][0] == ' '){
                			tabuleiro[2][0] = this.getSimbolo();
                			break;
                		}
                		else{
                			System.out.println("Jogada invalida. Jogue Novamente.");
                		}
                	}
                	else if(numeroCampo == 8){
                		if(tabuleiro[2][1] == ' '){
                			tabuleiro[2][1] = this.getSimbolo();
                			break;
                		}
                		else{
                			System.out.println("Jogada invalida. Jogue Novamente.");
                		}
                	}
                	else if(numeroCampo == 9){
                		if(tabuleiro[2][2] == ' '){
                			tabuleiro[2][2] = this.getSimbolo();
                			break;
                		}
                		else{
                			System.out.println("Jogada invalida. Jogue Novamente.");
                		}
                	}

                } catch (NumberFormatException e) {
                	numeroCampo = 10;
                    System.out.println("Jogada invalida. Jogue Novamente.");
                }
            }while(true);
        
        mostrarTabuleiro();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	estadoTabuleiro += tabuleiro[i][j];
            }
        }
        
        return estadoTabuleiro;
    }
    
    public void atualizarTabuleiro(String situacao) {
        int cont = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = situacao.charAt(cont);
                cont++;
            }
        }
        
        mostrarTabuleiro();
    }
    
    public boolean vitoria() {
        if(tabuleiro[0][0]!=' ' && tabuleiro[0][0]==tabuleiro[0][1] && tabuleiro[0][1]==tabuleiro[0][2])
            return true;
        else if (tabuleiro[1][0]!=' ' && tabuleiro[1][0]==tabuleiro[1][1] && tabuleiro[1][1]==tabuleiro[1][2])
            return true;
        else if (tabuleiro[2][0]!=' ' && tabuleiro[2][0]==tabuleiro[2][1] && tabuleiro[2][1]==tabuleiro[2][2])
            return true;
        else if (tabuleiro[0][0]!=' ' && tabuleiro[0][0]==tabuleiro[1][0] && tabuleiro[1][0]==tabuleiro[2][0])
            return true;
        else if (tabuleiro[0][1]!=' ' && tabuleiro[0][1]==tabuleiro[1][1] && tabuleiro[1][1]==tabuleiro[2][1])
            return true;
        else if (tabuleiro[0][2]!=' ' && tabuleiro[0][2]==tabuleiro[1][2] && tabuleiro[1][2]==tabuleiro[2][2])
            return true;
        else if (tabuleiro[0][0]!=' ' && tabuleiro[0][0]==tabuleiro[1][1] && tabuleiro[1][1]==tabuleiro[2][2])
            return true;
        else if (tabuleiro[0][2]!=' ' && tabuleiro[0][2]==tabuleiro[1][1] && tabuleiro[1][1]==tabuleiro[2][0])
            return true;
        
        return false;
    }
}