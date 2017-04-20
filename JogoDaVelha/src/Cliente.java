// Movimentos do mouse
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Comunicação multiplayer
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// Desenho do grid e janelas
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Cliente {

    private JFrame frame = new JFrame("Jogo da Velha");
    private JLabel messageLabel = new JLabel("");
    private ImageIcon icone;
    private ImageIcon iconeOponente;

    private Quadrado[] tabuleiro = new Quadrado[9];
    private Quadrado quadradoAtual;

    private static int PORTA = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // Cria o cliente, conecta no servidor e cria o grid do tabuleiro
    public Cliente(String enderecoServidor) throws Exception {

        // Configuração da conexão
        socket = new Socket(enderecoServidor, PORTA);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Criação do tabuleiro usando o gridlayout
        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, "South");
        JPanel panelTabuleiro = new JPanel();
        panelTabuleiro.setBackground(Color.black);
        panelTabuleiro.setLayout(new GridLayout(3, 3, 2, 2));
        
        // Adiciona listener do mouse em cada quadrado
        for (int i = 0; i < tabuleiro.length; i++) {
            final int j = i;
            tabuleiro[i] = new Quadrado();
            tabuleiro[i].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    quadradoAtual = tabuleiro[j];
                    out.println("JOGADA " + j);}});
            panelTabuleiro.add(tabuleiro[i]);
        }
        
        // Adiciona tabuleiro no centro do frame
        frame.getContentPane().add(panelTabuleiro, "Center");
    }

    // A threat do cliente vai receber as mensagens do servidor.
    // A primeira mensagem de BEMVINDO vai definir o simbolo do player
    // Depois vai entrar em loop esperando pelas outra mensagens 
    // E tratando de forma apropriada
    // Se um jogador não quiser jogar novamente, envia a mensagem SAIR
    // e o loop é encerrado
    
    public void play() throws Exception {
        String resposta;
        try {
            resposta = in.readLine();
            if (resposta.startsWith("BEMVINDO")) {
                char simbolo = resposta.charAt(9);
                icone = new ImageIcon(simbolo == 'X' ? "x.gif" : "o.gif");
                iconeOponente  = new ImageIcon(simbolo == 'X' ? "o.gif" : "x.gif");
                frame.setTitle("Jogo da Velha - Jogador " + simbolo);
            }
            while (true) {
                resposta = in.readLine();
                if (resposta.startsWith("JOGADA_VALIDA")) {
                    messageLabel.setText("Movimento valido, por favor aguarde");
                    quadradoAtual.setIcon(icone);
                    quadradoAtual.repaint();
                } else if (resposta.startsWith("OPONENTE_JOGOU")) {
                    int loc = Integer.parseInt(resposta.substring(15));
                    tabuleiro[loc].setIcon(iconeOponente);
                    tabuleiro[loc].repaint();
                    messageLabel.setText("Oponente jogou, sua vez");
                } else if (resposta.startsWith("VITORIA")) {
                    messageLabel.setText("Voce venceu!");
                    break;
                } else if (resposta.startsWith("DERROTA")) {
                    messageLabel.setText("Voce perdeu!");
                    break;
                } else if (resposta.startsWith("EMPATE")) {
                    messageLabel.setText("O jogo empatou!");
                    break;
                } else if (resposta.startsWith("MENSAGEM")) {
                    messageLabel.setText(resposta.substring(9));
                }
            }
            out.println("SAIR");
        }
        finally {
            socket.close();
        }
    }

    private boolean jogarNovamente() {
        int resposta = JOptionPane.showConfirmDialog(frame,
            "Quer jogar novamente?",
            "Jogo da Velha e muito legal",
            JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return resposta == JOptionPane.YES_OPTION;
    }

    // O quadrado do tabuleiro (9 quadrados) na janela do cliente
    // Cada quadrado recebe a chamada de setIcon() para preencher com X ou O
    
    @SuppressWarnings("serial")
	static class Quadrado extends JPanel {
        JLabel label = new JLabel((Icon)null);

        public Quadrado() {
            setBackground(Color.white);
            add(label);
        }

        public void setIcon(Icon icon) {
            label.setIcon(icon);
        }
    }

    // Roda o cliente como aplicação
    public static void main(String[] args) throws Exception {
        while (true) {
            String enderecoServidor = (args.length == 0) ? "localhost" : args[1];
            Cliente client = new Cliente(enderecoServidor);
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setSize(240, 160);
            client.frame.setVisible(true);
            client.frame.setResizable(false);
            client.play();
            if (!client.jogarNovamente()) {
                break;
            }
        }
    }
}