
package CampoMinadoBeta;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Tabuleiro extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Declaração dos atributos do tabuleiro
	int tam;
	int minas;
	int jogadas;
	ArrayList<JButton> celulas;

	// Declaração das cores que são usadas no jogo.
	Color azul = new Color(0, 91, 255);
	Color verde = new Color(0, 220, 0);
	Color azulClaro = new Color(30, 144, 255);
	Color vermelho = new Color(255, 40, 50);
	Color cinza = new Color(211, 211, 211);

	// Declaração dos ícones que são usadas no jogo.
	ImageIcon iconMina = new ImageIcon("src//CampoMinado//Midia//mina.png");
	ImageIcon iconBandeira = new ImageIcon("src//CampoMinado//Midia//bandeira.png");
	ImageIcon iconBandeiraAzul = new ImageIcon("src//CampoMinado//Midia//bandeiraAzul.png");
	ImageIcon iconInterrogacao = new ImageIcon("src//CampoMinado//Midia//interrogacao.png");
	ImageIcon iconRelogio = new ImageIcon("src//CampoMinado//Midia//relogio.png");
	ImageIcon iconUm = new ImageIcon("src//CampoMinado//Midia//1.png");
	ImageIcon iconDois = new ImageIcon("src//CampoMinado//Midia//2.png");
	ImageIcon iconTres = new ImageIcon("src//CampoMinado//Midia//3.png");
	ImageIcon iconQuatro = new ImageIcon("src//CampoMinado//Midia//4.png");
	ImageIcon iconCinco = new ImageIcon("src//CampoMinado//Midia//5.png");
	ImageIcon iconSeis = new ImageIcon("src//CampoMinado//Midia//6.png");
	ImageIcon iconSete = new ImageIcon("src//CampoMinado//Midia//7.png");
	ImageIcon iconOito = new ImageIcon("src//CampoMinado//Midia//8.png");

	// Declaração do campo que informa a quantidade de bandeiras colocadas pelo(a)
	// jogador(a) no tabuleiro.
	JLabel quantBandeiras = new JLabel();
	JLabel cronometro = new JLabel();
	JButton apVermelho = new JButton();
	JButton apVerde = new JButton();
	JButton apAzul = new JButton();

	Timer tm = new Timer();

	// Declaração da variável que permite o jogador jogar novamente.
	int jogarNovamente;

	public Tabuleiro() {

		// Declaração das propriedades do tabuleiro/JFrame
		setLayout(null);
		setTitle("Campo Minado");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		celulas = new ArrayList<JButton>();
	}

	/**
	 * Método que gera as células no tabuleiro respeitando as características do
	 * nível escolhido
	 */
	public void geraCelulas() {

		for (int x = 0; x < this.tam; x++) {

			for (int y = 0; y < this.tam; y++) {

				Celula celula = new Celula();
				if (this.tam == 9) {
					celula.setBounds(x * 50, y * 50, 50, 50);
				} else {
					celula.setBounds(x * 40, y * 40, 40, 40);
				}

				celula.setFocusable(false);
				celula.setHorizontalTextPosition(JButton.CENTER);
				celula.setVerticalTextPosition(JButton.CENTER);
				celula.addActionListener(this::click);
				if (x < 10 && y < 10) {
					celula.setText("0" + x + ".0" + y + " m:X");
				} else if (x < 10 && y >= 10) {
					celula.setText("0" + x + "." + y + " m:X");
				} else if (x >= 10 && y < 10) {
					celula.setText(+x + ".0" + y + " m:X");
				} else if (x >= 10 && y >= 10) {
					celula.setText(+x + "." + y + " m:X");
				}
				celula.setForeground(new Color(1.0f, 1.0f, 1.0f, 0f));
				celula.setVisible(true);

				// Colocar e tirar bandeiras com o botao direito do mouse.
				celula.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseReleased(java.awt.event.MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON3) {
							ImageIcon bandeira;
							if (celula.getBackground() == vermelho) {
								bandeira = iconBandeiraAzul;
							} else {
								bandeira = iconBandeira;
							}
							if (!celula.getText().contains("aberta")) {
								if (celula.getIcon() == null) {
									// Tira a bandeira da célula.
									celula.setIcon(bandeira);
								} else if (celula.getIcon() == bandeira) {
									// Coloca a bandeira na célula.
									celula.setIcon(iconInterrogacao);
								} else if (celula.getIcon() == iconInterrogacao) {
									celula.setIcon(null);
								}
							}

							update();
						}
					}
				});
				adicionaCelula(celula);
				add(celula);
			}
		}

		carregaCelulas();
		update();
	}

	public void carregaCelulas() {
		for (JButton celula : celulas) {
			celula.setBackground(this.azulClaro);
		}
	}

	public void update() {

		for (JButton celula : celulas) {
			add(celula);
		}
	}

	public void click(ActionEvent e) {
		for (JButton celula : celulas) {
			if (e.getSource() == celula) {
				if (celula.getIcon() == null) {
					this.jogadas++;
					if (this.jogadas == 1) {
						carregaMinas(celula);
						abreCelula(celula);
					}

					if (!celula.getText().contains("aberta")) {
						if (celula.getText().contains("mina")) {
							tm.cancel();
							revelaMinas();
							emiteSom("explosao");
							jogarNovamente = JOptionPane
									.showConfirmDialog(null,
											"Você perdeu o jogo na " + this.jogadas + "ª jogada. "
													+ "\nDeseja jogar novamente?",
											"Confirmação", JOptionPane.YES_NO_OPTION);

							this.dispose();
							if (jogarNovamente == 0) {
								Main m = new Main();
								m.executaJogo();
							}

						} else {
							abreCelula(celula);
						}
					}
				}
			}
		}
	}

	public void carregaMinas(JButton celulaAberta) {

		Random aleatorio = new Random();

		int xCelula = Integer.parseInt(celulaAberta.getText().substring(0, 2));
		int yCelula = Integer.parseInt(celulaAberta.getText().substring(3, 5));

		int i = 0;

		while (i < this.minas) {
			int limite = this.tam - 2;
			int x = aleatorio.nextInt(limite);
			int y = aleatorio.nextInt(limite);
			for (JButton celula : celulas) {
				if(x > 0 || y > 0) {
					if (celula.getText().equals(x + "." + y + " m:X") || celula.getText().equals(x + ".0" + y + " m:X")
							|| celula.getText().equals("0" + x + "." + y + " m:X")
							|| celula.getText().equals("0" + x + ".0" + y + " m:X")) {
						if (x > xCelula + 1 && y > yCelula + 1 || x > xCelula + 1 && y < yCelula - 1
								|| x < xCelula - 1 && y < yCelula - 1 || x < xCelula - 1 && y > yCelula + 1) {
							if (!celula.getText().contains("mina")) {
								celula.setText(celula.getText() + " /mina:");
								i++;
							}
						}
					}
				}
			}

		}

		carregaVizinhas();
	}

	public void carregaVizinhas() {

		for (JButton celula : celulas) {
			int minas = 0;
			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					if (dx != 0 || dy != 0) {
						int x = Integer.parseInt(celula.getText().substring(0, 2)) + dx;
						int y = Integer.parseInt(celula.getText().substring(3, 5)) + dy;
						if (x >= 0 && x < this.tam && y >= 0 && y < this.tam) {
							for (JButton celulaCheca : celulas) {
								int xCheca = Integer.parseInt(celulaCheca.getText().substring(0, 2));
								int yCheca = Integer.parseInt(celulaCheca.getText().substring(3, 5));
								if (x == xCheca && y == yCheca) {
									if (celulaCheca.getText().contains("mina")) {
										minas++;

									}
								}
							}
						}
					}
				}
			}

			if (!celula.getText().contains("mina")) {
				celula.setText(celula.getText().replace("X", String.valueOf(minas)));
			}
		}
	}

	public void abreCelula(JButton celulaAberta) {

		celulaAberta.setText(celulaAberta.getText() + " /aberta");

		if (celulaAberta.getText().substring(8, 9).equals("0")) {
			for (JButton c : celulas) {
				if (c == celulaAberta) {

					for (int dx = -1; dx <= 1; dx++) {
						for (int dy = -1; dy <= 1; dy++) {
							int x = Integer.parseInt(c.getText().substring(0, 2)) + dx;
							int y = Integer.parseInt(c.getText().substring(3, 5)) + dy;
							if (x >= 0 && x < this.tam && y >= 0 && y < this.tam) {
								for (JButton celulaCheca : celulas) {
									int xCheca = Integer.parseInt(celulaCheca.getText().substring(0, 2));
									int yCheca = Integer.parseInt(celulaCheca.getText().substring(3, 5));
									if (x == xCheca && y == yCheca) {
										if (!celulaCheca.getText().contains("aberta")
												&& !celulaCheca.getText().contains("mina")) {
											celulaCheca.setText(celulaCheca.getText() + " /aberta");
										}
									}
								}
							}
						}
					}
				}
			}
			abreVaziasProximas();
		}
		executaAbertura();
	}

	public void abreVaziasProximas() {

		int confere = 0;

		do {
			// Esse laço serve para repetir o processo de abertura das células toda vez que
			// uma célula
			// vazia for aberta, porque caso contrário, algumas células que deveriam abrir
			// não abririam
			// por já terem sido percorridas no processo.

			confere = 0;

			for (JButton c : celulas) {

				if (!c.getText().contains("aberta")) {
					for (int dx = -1; dx <= 1; dx++) {
						for (int dy = -1; dy <= 1; dy++) {
							int x = Integer.parseInt(c.getText().substring(0, 2)) + dx;
							int y = Integer.parseInt(c.getText().substring(3, 5)) + dy;
							if (x >= 0 && x < this.tam && y >= 0 && y < this.tam) {
								for (JButton celulaCheca : celulas) {
									int xCheca = Integer.parseInt(celulaCheca.getText().substring(0, 2));
									int yCheca = Integer.parseInt(celulaCheca.getText().substring(3, 5));
									if (x == xCheca && y == yCheca) {
										if (celulaCheca.getText().substring(8, 9).equals("0")
												&& celulaCheca.getText().contains("aberta")) {
											c.setText(c.getText() + " /aberta");
											confere++;
										}
									}
								}
							}
						}
					}
				}
			}

		} while (confere != 0);
	}

	public void executaAbertura() {
		for (JButton c : celulas) {
			if (c.getText().contains("aberta")) {
				c.setBackground(cinza);
				defineNumero(c);
			}
		}

		update();
		checaCelulas();
	}

	public void defineNumero(JButton celula) {

		for (JButton c : celulas) {
			if (c == celula) {

				if (!celula.getText().substring(8, 9).equals("X")) {
					int quantMinas = Integer.parseInt(celula.getText().substring(8, 9));

					switch (quantMinas) {

					case 0:
						c.setIcon(null);
						break;
					case 1:
						c.setBackground(this.cinza);
						c.setIcon(iconUm);
						break;
					case 2:
						c.setBackground(this.cinza);
						c.setIcon(iconDois);
						break;
					case 3:
						c.setBackground(this.cinza);
						c.setIcon(iconTres);
						break;
					case 4:
						c.setBackground(this.cinza);
						c.setIcon(iconQuatro);
						break;
					case 5:
						c.setBackground(this.cinza);
						c.setIcon(iconCinco);
						break;
					case 6:
						c.setBackground(this.cinza);
						c.setIcon(iconSeis);
						break;
					case 7:
						c.setBackground(this.cinza);
						c.setIcon(iconSete);
						break;
					case 8:
						c.setBackground(this.cinza);
						c.setIcon(iconOito);
						break;
					default:
						break;
					}
				}
			}
		}
	}

	public void checaCelulas() {

		int celulasAbertas = 0;

		for (JButton celula : celulas) {
			if (celula.getText().contains("aberta")) {
				celulasAbertas++;
			}
		}

		if (celulasAbertas == this.tam * this.tam - this.minas) {
			tm.cancel();
			emiteSom("vitoria");
			revelaMinas();
			this.jogarNovamente = JOptionPane.showConfirmDialog(null,
					"Parabéns, você ganhou o jogo!!!" + "\nCom " + this.jogadas + " jogadas! " + 
							"\n\n Deseja jogar novamente?",
					"Confirmação", JOptionPane.YES_NO_OPTION);

			this.dispose();
			if (jogarNovamente == 0) {
				Main m = new Main();
				m.executaJogo();
			}

		}

	}

	public void revelaMinas() {
		for (JButton celula : celulas) {
			if (celula.getText().contains("mina")) {
				celula.setIcon(iconMina);
				celula.setBackground(cinza);
			}
		}
		update();
	}

	public void emiteSom(String nome) { // Método AudioAcerto para chamar na classe executavel.
		try {
			// URL do som que no caso esta no pendrive, mais ainda é uma fase de teste.
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File("src//CampoMinado//Midia//" + nome + ".wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			System.out.println("Erro ao executar SOM!");
			ex.printStackTrace();
		}
	}


	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public int getTam() {
		return tam;
	}

	public void setTam(int tam) {
		this.tam = tam;
	}

	public ArrayList<JButton> getCelulas() {
		return celulas;
	}

	public void setCelulas(ArrayList<JButton> celulas) {
		this.celulas = celulas;
	}

	public void adicionaCelula(JButton celula) {
		this.celulas.add(celula);
	}

	public int getMinas() {
		return minas;
	}

	public void setMinas(int minas) {
		this.minas = minas;
	}

	public int getJogarNovamente() {
		return jogarNovamente;
	}

	public void setJogarNovamente(int jogarNovamente) {
		this.jogarNovamente = jogarNovamente;
	}

	public int getJogadas() {
		return jogadas;
	}

	public void setJogadas(int jogadas) {
		this.jogadas = jogadas;
	}


}
