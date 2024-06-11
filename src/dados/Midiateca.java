package dados;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Midiateca implements Iterador {
	private int contador;
	private List<Midia> midias;
	private Map<Integer, Midia> midiasPorCodigo;

	public Midiateca() {
		this.midias = new ArrayList<>();
		this.midiasPorCodigo = new HashMap<>();
		this.reset();
	}

	public boolean cadastraMidia(Midia midia) {
		if (midiasPorCodigo.containsKey(midia.getCodigo())) {
			return false;
		}
		midias.add(midia);
		midiasPorCodigo.put(midia.getCodigo(), midia);
		return true;
	}

	public Midia consultaPorCodigo(int codigo) {
		return midiasPorCodigo.get(codigo);
	}

	public List<Midia> consultaPorCategoria(Categoria categoria) {
		List<Midia> resultado = new ArrayList<>();
		for (Midia midia : midias) {
			if (midia.getCategoria() == categoria) {
				resultado.add(midia);
			}
		}
		return resultado;
	}

	public boolean removeMidia(int codigo) {
		Midia midia = midiasPorCodigo.remove(codigo);
		if (midia != null) {
			midias.remove(midia);
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		contador = 0;
	}

	@Override
	public boolean hasNext() {
		return contador < midias.size();
	}

	@Override
	public Object next() {
		return midias.get(contador++);
	}
}
