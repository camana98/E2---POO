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
				try {
					int codigo = Integer.parseInt(linha);
					String titulo = br.readLine();
					int ano = Integer.parseInt(br.readLine());
					String categoriaStr = br.readLine().toUpperCase();
					Categoria categoria;
					try {
						categoria = Categoria.valueOf(categoriaStr);
					} catch (IllegalArgumentException e) {
						linhasSaida.add(String.format("1:Erro-categoria inválida: %s", categoriaStr));
						br.readLine(); // Ignore the next line (quality) since category is invalid
						continue;
					}
					int qualidade = Integer.parseInt(br.readLine());

					Video video = new Video(codigo, titulo, ano, categoria, qualidade);

					if (midiateca.cadastraMidia(video)) {
						linhasSaida.add(String.format("1:%d,%s,%d,%s,%d", codigo, titulo, ano, categoria.getNome(), qualidade));
					} else {
						linhasSaida.add(String.format("1:Erro-video com codigo repetido: %d", codigo));
					}
				} catch (NumberFormatException e) {
					linhasSaida.add(String.format("1:Erro-dado inválido: %s", e.getMessage()));
				}
			}

			// Cadastrar músicas - PASSO 2
			while ((linha = br.readLine()) != null && !linha.equals("-1")) {
				try {
					int codigo = Integer.parseInt(linha);
					String titulo = br.readLine();
					int ano = Integer.parseInt(br.readLine());
					String categoriaStr = br.readLine().toUpperCase();
					Categoria categoria;
					try {
						categoria = Categoria.valueOf(categoriaStr);
					} catch (IllegalArgumentException e) {
						linhasSaida.add(String.format("2:Erro-categoria inválida: %s", categoriaStr));
						br.readLine(); // Ignore the next line (duration) since category is invalid
						continue;
					}
					double duracao = Double.parseDouble(br.readLine());

					Musica musica = new Musica(codigo, titulo, ano, categoria, duracao);

					if (midiateca.cadastraMidia(musica)) {
						linhasSaida.add(String.format(Locale.US, "2:%d,%s,%d,%s,%.2f", codigo, titulo, ano, categoria.getNome(), duracao));
					} else {
						linhasSaida.add(String.format("2:Erro-musica com codigo repetido: %d", codigo));
					}
				} catch (NumberFormatException e) {
					linhasSaida.add(String.format("2:Erro-dado inválido: %s", e.getMessage()));
				}
			}

			// Mostrar os dados de uma determinada mídia - PASSO 3
			if ((linha = br.readLine()) != null) {
				try {
					int codigo = Integer.parseInt(linha);
					Midia midia = midiateca.consultaPorCodigo(codigo);
					if (midia == null) {
						linhasSaida.add("3:Codigo inexistente.");
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
				} catch (NumberFormatException e) {
					linhasSaida.add(String.format("3:Erro-código inválido: %s", linha));
				}
			}

			// Mostrar os dados de mídia(s) de uma determinada categoria - PASSO 4
			if ((linha = br.readLine()) != null) {
				Categoria categoria = null;
				String categoriaStr = linha.toUpperCase();
				try {
					categoria = Categoria.valueOf(categoriaStr);
				} catch (IllegalArgumentException e) {
					linhasSaida.add("4:Nenhuma midia encontrada.");
				}
				if (categoria != null) {
					List<Midia> midias = midiateca.consultaPorCategoria(categoria);
					if (midias.isEmpty()) {
						linhasSaida.add("4:Nenhuma midia encontrada.");
					} else {
						for (Midia m : midias) {
							if (m instanceof Video) {
								Video video = (Video) m;
								linhasSaida.add(String.format(Locale.US, "4:%d,%s,%d,%s,%d,%.2f",
										video.getCodigo(), video.getTitulo(), video.getAno(),
										video.getCategoria().getNome(), video.getQualidade(), video.calculaLocacao()));
							} else if (m instanceof Musica) {
								Musica musica = (Musica) m;
								linhasSaida.add(String.format(Locale.US, "4:%d,%s,%d,%s,%.2f,%.2f",
										musica.getCodigo(), musica.getTitulo(), musica.getAno(),
										musica.getCategoria().getNome(), musica.getDuracao(), musica.calculaLocacao()));
							}
						}
					}
				}
			}

			// Mostrar os dados de vídeo(s) de uma determinada qualidade - PASSO 5
			if ((linha = br.readLine()) != null) {
				try {
					int qualidade = Integer.parseInt(linha);
					List<Midia> midias = midiateca.consultaPorQualidade(qualidade);
					if (midias.isEmpty()) {
						linhasSaida.add("5:Qualidade inexistente.");
					} else {
						for (Midia m : midias) {
							if (m instanceof Video) {
								Video video = (Video) m;
								linhasSaida.add(String.format(Locale.US, "5:%d,%s,%d,%s,%d,%.2f",
										video.getCodigo(), video.getTitulo(), video.getAno(),
										video.getCategoria().getNome(), video.getQualidade(), video.calculaLocacao()));
							}
						}
					}
				} catch (NumberFormatException e) {
					linhasSaida.add(String.format("5:Erro-qualidade inválida: %s", linha));
				}
			}

			// Mostrar os dados da música de maior duração - PASSO 6
			Musica musicaMaiorDuracao = midiateca.consultaMaiorDuracaoMusica();
			if (musicaMaiorDuracao == null) {
				linhasSaida.add("6:Nenhuma música encontrada.");
			} else {
				linhasSaida.add(String.format(Locale.US, "6:%s,%.2f", musicaMaiorDuracao.getTitulo(), musicaMaiorDuracao.getDuracao()));
			}

			// Remover uma mídia - PASSO 7
			if ((linha = br.readLine()) != null) {
				try {
					int codigo = Integer.parseInt(linha);
					Midia midia = midiateca.consultaPorCodigo(codigo);
					if (midia == null) {
						linhasSaida.add("7:Codigo inexistente.");
					} else {
						if (midiateca.removeMidia(codigo)) {
							if (midia instanceof Video) {
								Video video = (Video) midia;
								linhasSaida.add(String.format(Locale.US, "7:%d,%s,%d,%s,%d,%.2f",
										video.getCodigo(), video.getTitulo(), video.getAno(),
										video.getCategoria().getNome(), video.getQualidade(), video.calculaLocacao()));
							} else if (midia instanceof Musica) {
								Musica musica = (Musica) midia;
								linhasSaida.add(String.format(Locale.US, "7:%d,%s,%d,%s,%.2f,%.2f",
										musica.getCodigo(), musica.getTitulo(), musica.getAno(),
										musica.getCategoria().getNome(), musica.getDuracao(), musica.calculaLocacao()));
							}
						}
					}
				} catch (NumberFormatException e) {
					linhasSaida.add(String.format("7:Erro-código inválido: %s", linha));
				}
			}

			// Mostrar o somatório de locações de todas as mídias - PASSO 8
			double somatorio = midiateca.calculaSomatorioLocacoes();
			if (somatorio == 0) {
				linhasSaida.add("8:Nenhuma midia encontrada.");
			} else {
				linhasSaida.add(String.format(Locale.US, "8:%.2f", somatorio));
			}

			// Mostrar os dados da música com valor de locação mais próximo da média - PASSO 9
			double somaValores = 0;
			int quantidadeMusicas = 0;
			List<Musica> musicas = new ArrayList<>();

			for (Midia midia : midiateca.getMidias()) {
				if (midia instanceof Musica) {
					Musica musica = (Musica) midia;
					somaValores += musica.calculaLocacao();
					quantidadeMusicas++;
					musicas.add(musica);
				}
			}

			if (quantidadeMusicas == 0) {
				linhasSaida.add("9:Nenhuma musica encontrada.");
			} else {
				double media = somaValores / quantidadeMusicas;
				Musica musicaMaisProxima = null;
				double menorDiferenca = Double.MAX_VALUE;

				for (Musica musica : musicas) {
					double diferenca = Math.abs(musica.calculaLocacao() - media);
					if (diferenca < menorDiferenca) {
						menorDiferenca = diferenca;
						musicaMaisProxima = musica;
					}
				}

				if (musicaMaisProxima != null) {
					linhasSaida.add(String.format(Locale.US, "9:%.2f,%d,%s,%d,%s,%.2f,%.2f",
							media,
							musicaMaisProxima.getCodigo(),
							musicaMaisProxima.getTitulo(),
							musicaMaisProxima.getAno(),
							musicaMaisProxima.getCategoria().getNome(),
							musicaMaisProxima.getDuracao(),
							musicaMaisProxima.calculaLocacao()));
				}
			}

			// Mostrar os dados da mídia mais nova - PASSO 10
			Midia midiaMaisNova = null;
			int anoMaisNovo = Integer.MIN_VALUE;

			for (Midia midia : midiateca.getMidias()) {
				if (midia.getAno() > anoMaisNovo) {
					anoMaisNovo = midia.getAno();
					midiaMaisNova = midia;
				}
			}

			if (midiaMaisNova == null) {
				linhasSaida.add("10:Nenhuma midia encontrada.");
			} else {
				linhasSaida.add(String.format(Locale.US, "10:%d,%s,%d",
						midiaMaisNova.getCodigo(),
						midiaMaisNova.getTitulo(),
						midiaMaisNova.getAno()));
			}

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
