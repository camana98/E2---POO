package app;

import dados.Midiateca;
import dados.Video;
import dados.Musica;
import dados.Categoria;
import dados.Midia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ACMEMidia {

	private Midiateca midiateca;
	private static final String ARQUIVO_ENTRADA = "entrada.txt";
	private static final String ARQUIVO_SAIDA = "saida.txt";

	public ACMEMidia() {
		this.midiateca = new Midiateca();
	}

	public void executa() {
		List<String> linhasSaida = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_ENTRADA))) {
			String linha;

			// Cadastrar vídeos - PASSO 1
			while ((linha = br.readLine()) != null && !linha.equals("-1")) {
				int codigo = Integer.parseInt(linha);
				String titulo = br.readLine();
				int ano = Integer.parseInt(br.readLine());
				Categoria categoria = Categoria.valueOf(br.readLine().toUpperCase());
				int qualidade = Integer.parseInt(br.readLine());

				Video video = new Video(codigo, titulo, ano, categoria, qualidade);

				if (midiateca.cadastraMidia(video)) {
					linhasSaida.add(String.format("1:%d,%s,%d,%s,%d", codigo, titulo, ano, categoria.getNome(), qualidade));
				} else {
					linhasSaida.add(String.format("1:Erro-video com codigo repetido: %d", codigo));
				}
			}

			// Cadastrar músicas - PASSO 2
			while ((linha = br.readLine()) != null && !linha.equals("-1")) {
				int codigo = Integer.parseInt(linha);
				String titulo = br.readLine();
				int ano = Integer.parseInt(br.readLine());
				Categoria categoria = Categoria.valueOf(br.readLine().toUpperCase());
				double duracao = Double.parseDouble(br.readLine());

				Musica musica = new Musica(codigo, titulo, ano, categoria, duracao);

				if (midiateca.cadastraMidia(musica)) {
					linhasSaida.add(String.format(Locale.US, "2:%d,%s,%d,%s,%.2f", codigo, titulo, ano, categoria.getNome(), duracao));
				} else {
					linhasSaida.add(String.format("2:Erro-musica com codigo repetido: %d", codigo));
				}
			}

			// Mostrar os dados de uma determinada mídia - PASSO 3
			if ((linha = br.readLine()) != null) {
				int codigo = Integer.parseInt(linha);
				Midia midia = midiateca.consultaPorCodigo(codigo);
				if (midia == null) {
					linhasSaida.add("3:Codigo inexistente");
				} else {
					if (midia instanceof Video) {
						Video video = (Video) midia;
						linhasSaida.add(String.format(Locale.US, "3:%d,%s,%d,%s,%d,%.2f",
								video.getCodigo(), video.getTitulo(), video.getAno(),
								video.getCategoria().getNome(), video.getQualidade(), video.calculaLocacao()));
					} else if (midia instanceof Musica) {
						Musica musica = (Musica) midia;
						linhasSaida.add(String.format(Locale.US, "3:%d,%s,%d,%s,%.2f,%.2f",
								musica.getCodigo(), musica.getTitulo(), musica.getAno(),
								musica.getCategoria().getNome(), musica.getDuracao(), musica.calculaLocacao()));
					}
				}
			}

			// Aqui continuaremos com as operações restantes

		} catch (IOException e) {
			e.printStackTrace();
		}

		gravarDados(linhasSaida);
	}

	public void gravarDados(List<String> linhas) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_SAIDA))) {
			for (String linha : linhas) {
				bw.write(linha);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ACMEMidia acmeMidia = new ACMEMidia();
		acmeMidia.executa();
	}
}
