package dados;

public class Musica extends Midia {
	private double duracao;

	public Musica(int codigo, String titulo, int ano, Categoria categoria, double duracao) {
		super(codigo, titulo, ano, categoria);
		this.duracao = duracao;
	}

	@Override
	public double calculaLocacao() {
		double valorPorMinuto;
		switch (getCategoria()) {
			case ACA:
				valorPorMinuto = 0.90;
				break;
			case DRA:
				valorPorMinuto = 0.70;
				break;
			case FIC:
				valorPorMinuto = 0.50;
				break;
			case ROM:
				valorPorMinuto = 0.30;
				break;
			default:
				valorPorMinuto = 0.0;
		}
		return duracao * valorPorMinuto;
	}

	public double getDuracao() {
		return duracao;
	}
}
