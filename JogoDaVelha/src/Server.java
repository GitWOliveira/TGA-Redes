import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

 // Um servidor multiplayer de Jogo da Velha.  

//  Cliente -> Servidor       	 Servidor -> Cliente
//  ----------------         	 ----------------
//  JOGADA <n>  (0 <= n <= 8)    BEMVINDO <char>  (char in {X, O})
//  SAIR                      	 JOGADA_VALIDA
//                          	 OPONENTE_JOGOU <n>
//                            	 VITORIA 
//                             	 DERROTA 
//                             	 EMPATE 
//                               MENSAGEM <text>

public class Server {

	// Roda a aplicacao
	public static void main(String[] args) throws Exception {
		ServerSocket listener = new ServerSocket(8901);
		System.out.println("O servidor de Jogo da Velha está rodando...");
		try {
			while (true) {
				JogoDaVelha jogo = new JogoDaVelha();
				JogoDaVelha.Jogador jogadorX = jogo.new Jogador(listener.accept(), 'X');
				JogoDaVelha.Jogador jogadorO = jogo.new Jogador(listener.accept(), 'O');
				jogadorX.setOponente(jogadorO);
				jogadorO.setOponente(jogadorX);
				jogo.jogadorAtual = jogadorX;
				jogadorX.start();
				jogadorO.start();
			}
		} finally {
			listener.close();
		}
	}
}

class JogoDaVelha {
	
	// O tabuleiro tem 9 quadrados que são null.
	// Se for null, não pertence a nenhum player, caso contrario o array guarda a referencia do jogador
	// que jogou naquele quadrado
	private Jogador[] tabuleiro = { null, null, null, null, null, null, null, null, null };

	Jogador jogadorAtual;

	// Checa se tem vencedor
	public boolean temVencedor() {
		return (tabuleiro[0] != null && tabuleiro[0] == tabuleiro[1] && tabuleiro[0] == tabuleiro[2])
				|| (tabuleiro[3] != null && tabuleiro[3] == tabuleiro[4] && tabuleiro[3] == tabuleiro[5])
				|| (tabuleiro[6] != null && tabuleiro[6] == tabuleiro[7] && tabuleiro[6] == tabuleiro[8])
				|| (tabuleiro[0] != null && tabuleiro[0] == tabuleiro[3] && tabuleiro[0] == tabuleiro[6])
				|| (tabuleiro[1] != null && tabuleiro[1] == tabuleiro[4] && tabuleiro[1] == tabuleiro[7])
				|| (tabuleiro[2] != null && tabuleiro[2] == tabuleiro[5] && tabuleiro[2] == tabuleiro[8])
				|| (tabuleiro[0] != null && tabuleiro[0] == tabuleiro[4] && tabuleiro[0] == tabuleiro[8])
				|| (tabuleiro[2] != null && tabuleiro[2] == tabuleiro[4] && tabuleiro[2] == tabuleiro[6]);
	}

	// Checa se o tabuleiro esta cheio
	public boolean tabuleiroCheio() {
		for (int i = 0; i < tabuleiro.length; i++) {
			if (tabuleiro[i] == null) {
				return false;
			}
		}
		return true;
	}
	// Chamada pela thread do player quando algum jogador tenta executar uma jogada
	// e verifica se a jogada é valida
	// Também verifica se o jogador tentando executar a jogada é o jogador atual
	public synchronized boolean jogadaPermitida(int local, Jogador jogador) {
		if (jogador == jogadorAtual && tabuleiro[local] == null) {
			tabuleiro[local] = jogadorAtual;
			jogadorAtual = jogadorAtual.oponente;
			jogadorAtual.outroJogadorJogou(local);
			return true;
		}
		return false;
	}

	// Os jogadores são identificados com X e O
	class Jogador extends Thread {
		char simbolo;
		Jogador oponente;
		Socket socket;
		BufferedReader input;
		PrintWriter output;

		public Jogador(Socket socket, char simbolo) {
			this.socket = socket;
			this.simbolo = simbolo;
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);
				output.println("BEMVINDO " + simbolo);
				output.println("MENSAGEM Esperando jogador se conectar");
			} catch (IOException e) {
				System.out.println("Jogador perdeu a conexão: " + e);
			}
		}

		// Aceita a notificação de novo oponente
		public void setOponente(Jogador oponente) {
			this.oponente = oponente;
		}

		// Administra a mensagem de OPONENTE_JOGOU
		public void outroJogadorJogou(int local) {
			output.println("OPONENTE_JOGOU " + local);
			output.println(temVencedor() ? "DERROTA" : tabuleiroCheio() ? "EMPATE" : "");
		}

		// metodo run() da threat
		public void run() {
			try {
				// A threat só começa depois que todos da dupla se conectarem
				output.println("MENSAGEM Todos os jogadores estao conectados");

				// Informa o primeiro jogador que é a sua vez.
				if (simbolo == 'X') {
					output.println("MENSAGEM Sua vez");
				}

				// Continuamente recebe comandos do cliente e os processa
				while (true) {
					String comando = input.readLine();
					if (comando.startsWith("JOGADA")) {
						int local = Integer.parseInt(comando.substring(7));
						if (jogadaPermitida(local, this)) {
							output.println("JOGADA_VALIDA");
							output.println(temVencedor() ? "VITORIA" : tabuleiroCheio() ? "EMPATE" : "");
						} else {
							output.println("MENSAGEM ?");
						}
					} else if (comando.startsWith("SAIR")) {
						return;
					}
				}
			} catch (IOException e) {
				System.out.println("Jogador perdeu a conexão: " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}