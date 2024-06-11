package dados;

public class Video extends Midia {
	private int qualidade;

	public Video(int codigo, String titulo, int ano, Categoria categoria, int qualidade) {
		super(codigo, titulo, ano, categoria);
		this.qualidade = qualidade;
	}

	@Override
	public double calculaLocacao() {
		if (getAno() == 2024) {
			return 20.00;
		} else if (getAno() >= 2000 && getAno() <= 2023) {
			return 15.00;
		} else {
			return 10.00;
		}
	}

	public int getQualidade() {
		return qualidade;
	}
}
