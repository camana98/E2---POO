package dados;

public abstract class Midia {

	private int codigo;
	private String titulo;
	private int ano;
	private Categoria categoria;

	public Midia(int codigo, String titulo, int ano, Categoria categoria) {
		this.codigo = codigo;
		this.titulo = titulo;
		this.ano = ano;
		this.categoria = categoria;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getAno() {
		return ano;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public abstract double calculaLocacao();
}
