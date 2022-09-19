package CampoMinadoBeta;

import javax.swing.JOptionPane;

public class Main {

	public void executaJogo() {

		//Declaração da variável para a escolha do nível do jogo.
		String[] niveis = { "Nível Iniciante", "Nível Intermediário" };

		//Escolha do nível do jogo.
		String nivel = (String) JOptionPane.showInputDialog(null, "Selecione a dificuldade do jogo: ", "Escolha", 3,
				null, niveis, null);

		//Instanciação da janela do jogo.
		Tabuleiro tab = new Tabuleiro();
		
		if (nivel == niveis[0]) {
			//Se o nível escolhido for o "iniciante", o tabuleiro será setado com o tamanho de 9x9 
			//e com 10 minas espalhadas pelo tabuleiro (O método setSize define as dimensões do JFrame).
			tab.setTam(9);
			tab.setMinas(10);
			tab.setSize(464, 486);
		} else {
			//Se o nível escolhido for o "intermediário", o tabuleiro será setado com o tamanho de 16x16 
			//e com 40 minas espalhadas pelo tabuleiro (O método setSize define as dimensões do JFrame).
			tab.setTam(16);
			tab.setMinas(40);
			tab.setSize(655, 676);
		}

		//Chamada do método que gera as células no tabuleiro.
		tab.geraCelulas();

	}

	public static void main(String[] args) {
		
		//Instanciação da classe executável.
		Main main = new Main();
		//Chamada do método que executa o jogo.
		main.executaJogo();
	}
}
