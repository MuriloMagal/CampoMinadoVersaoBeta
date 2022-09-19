package CampoMinadoBeta;

import java.util.ArrayList;

import javax.swing.JButton;

public class Celula extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x;
	int y;
	boolean mina;
	boolean aberta;
	ArrayList<JButton> vizinhas;
	
	public Celula() {
		vizinhas = new ArrayList<JButton>();
	}	
	
	public Celula(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isMina() {
		return mina;
	}

	public void setMina(boolean mina) {
		this.mina = mina;
	}

	public boolean isAberta() {
		return aberta;
	}

	public void setAberta(boolean aberta) {
		this.aberta = aberta;
	}

	public ArrayList<JButton> getVizinhas() {
		return vizinhas;
	}

	public void setVizinhas(ArrayList<JButton> vizinhas) {
		this.vizinhas = vizinhas;
	}
	
	public void addVizinha(JButton vizinha) {
		this.vizinhas.add(vizinha);
	}

}
