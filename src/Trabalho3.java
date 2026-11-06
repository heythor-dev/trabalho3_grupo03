import java.util.Random;

public class Trabalho3 {

    static int VAZIO = -1;
    static int REMOVIDO = -2;
    static int acessos = 0;
    static int reaproveitamentos = 0;
    static int duplicadasDescartadas = 0;

    static int funcaoHash(int chave, int tamanho) {
        return chave % tamanho;
    }

    static int rehash(int posicao, int tamanho) {
        return (posicao + 1) % tamanho;
    }

    static int[] criaTabela(int tamanho) {
        int[] tabela = new int[tamanho];
        for (int indice = 0; indice < tamanho; indice++) {
            tabela[indice] = VAZIO;
        }
        return tabela;
    }

    static int busca(int[] tabela, int chave) {
        int tamanho = tabela.length;
        int posicao = funcaoHash(chave, tamanho);
        int passos = 0;
        while (passos < tamanho) {
            acessos++;
            passos++;
            if (tabela[posicao] == VAZIO) {
                return -1;
            }
            if (tabela[posicao] == chave) {
                return posicao;
            }
            posicao = rehash(posicao, tamanho);
        }
        return -1;
    }

    static int insere(int[] tabela, int chave) {
        int tamanho = tabela.length;
        int posicao = funcaoHash(chave, tamanho);
        int passos = 0;
        int primeiraRemovida = -1;
        while (passos < tamanho) {
            acessos++;
            passos++;
            if (tabela[posicao] == VAZIO) {
                if (primeiraRemovida != -1) {
                    tabela[primeiraRemovida] = chave;
                    reaproveitamentos++;
                    return primeiraRemovida;
                } else {
                    tabela[posicao] = chave;
                    return posicao;
                }
            }
            if (tabela[posicao] == chave) {
                return -1;
            }
            if (tabela[posicao] == REMOVIDO && primeiraRemovida == -1) {
                primeiraRemovida = posicao;
            }
            posicao = rehash(posicao, tamanho);
        }
        if (primeiraRemovida != -1) {
            tabela[primeiraRemovida] = chave;
            reaproveitamentos++;
            return primeiraRemovida;
        } else {
            return -2;
        }
    }

    static boolean removeIngenua(int[] tabela, int chave) {
        int posicao = busca(tabela, chave);
        if (posicao == -1) {
            return false;
        }
        tabela[posicao] = VAZIO;
        return true;
    }

    static boolean remove(int[] tabela, int chave) {
        int posicao = busca(tabela, chave);
        if (posicao == -1) {
            return false;
        }
        tabela[posicao] = REMOVIDO;
        return true;
    }

    static void reorganiza(int[] tabela) {
        int tamanho = tabela.length;
        int[] nova = criaTabela(tamanho);
        for (int posicao = 0; posicao < tamanho; posicao++) {
            int valor = tabela[posicao];
            if (valor >= 0) {
                insere(nova, valor);
            }
        }
        for (int posicao = 0; posicao < tamanho; posicao++) {
            tabela[posicao] = nova[posicao];
        }
    }

    static int contaOcupadas(int[] tabela) {
        int quantidade = 0;
        int tamanho = tabela.length;
        for (int posicao = 0; posicao < tamanho; posicao++) {
            if (tabela[posicao] >= 0) {
                quantidade++;
            }
        }
        return quantidade;
    }

    static int contaRemovidas(int[] tabela) {
        int quantidade = 0;
        int tamanho = tabela.length;
        for (int posicao = 0; posicao < tamanho; posicao++) {
            if (tabela[posicao] == REMOVIDO) {
                quantidade++;
            }
        }
        return quantidade;
    }

    static int contaVazias(int[] tabela) {
        int quantidade = 0;
        int tamanho = tabela.length;
        for (int posicao = 0; posicao < tamanho; posicao++) {
            if (tabela[posicao] == VAZIO) {
                quantidade++;
            }
        }
        return quantidade;
    }

    static int[] geraChaves(Random sorteio, int quantidade) {
        int[] chaves = new int[quantidade];
        int gerados = 0;
        duplicadasDescartadas = 0;
        while (gerados < quantidade) {
            int candidata = 1000000 + sorteio.nextInt(9000000);
            boolean existe = false;
            for (int indice = 0; indice < gerados; indice++) {
                if (chaves[indice] == candidata) {
                    existe = true;
                }
            }
            if (existe) {
                duplicadasDescartadas++;
            } else {
                chaves[gerados] = candidata;
                gerados++;
            }
        }
        return chaves;
    }

    static int[] geraAusentes(Random sorteio, int[] chaves, int quantidade) {
        int[] ausentes = new int[quantidade];
        int gerados = 0;
        int tamanhoChaves = chaves.length;
        while (gerados < quantidade) {
            int candidata = 1000000 + sorteio.nextInt(9000000);
            boolean existe = false;
            for (int indice = 0; indice < tamanhoChaves; indice++) {
                if (chaves[indice] == candidata) {
                    existe = true;
                }
            }
            if (!existe) {
                ausentes[gerados] = candidata;
                gerados++;
            }
        }
        return ausentes;
    }

    static int verificaTeste(String codigo, boolean aprovado) {
        if (aprovado) {
            System.out.println("TESTE " + codigo + ": APROVADO");
            return 1;
        } else {
            System.out.println("TESTE " + codigo + ": REPROVADO");
            return 0;
        }
    }

    static void executaTestes() {
        int aprovados = 0;
        int total = 26;

        Random sorteioG1 = new Random(1990);
        int[] chavesG1 = geraChaves(sorteioG1, 5);
        aprovados += verificaTeste("G1", chavesG1[0] == 4429279 && chavesG1[1] == 3976163 && chavesG1[2] == 9187615 && chavesG1[3] == 1167825 && chavesG1[4] == 9651672 && duplicadasDescartadas == 0);

        int[] ausentesG2 = geraAusentes(sorteioG1, chavesG1, 3);
        aprovados += verificaTeste("G2", ausentesG2[0] == 8282916 && ausentesG2[1] == 7251645 && ausentesG2[2] == 6739522);

        Random sorteioG3 = new Random(1990);
        int[] chavesG3 = geraChaves(sorteioG3, 700);
        aprovados += verificaTeste("G3", chavesG3[699] == 9691227);

        int[] ausentesG4 = geraAusentes(sorteioG3, chavesG3, 350);
        aprovados += verificaTeste("G4", ausentesG4[0] == 7816661 && ausentesG4[1] == 1376564 && ausentesG4[2] == 1172019 && ausentesG4[349] == 6734863);

        int[] tabelaN = criaTabela(11);
        int retornoN1A = insere(tabelaN, 22);
        int retornoN1B = insere(tabelaN, 33);
        int retornoN1C = insere(tabelaN, 44);
        int retornoN1D = insere(tabelaN, 55);
        aprovados += verificaTeste("N1", retornoN1A == 0 && retornoN1B == 1 && retornoN1C == 2 && retornoN1D == 3 && tabelaN[0] == 22 && tabelaN[1] == 33 && tabelaN[2] == 44 && tabelaN[3] == 55 && contaVazias(tabelaN) == 7);

        boolean retornoN2 = removeIngenua(tabelaN, 33);
        aprovados += verificaTeste("N2", retornoN2 && tabelaN[1] == VAZIO);

        acessos = 0;
        int retornoN3 = busca(tabelaN, 44);
        aprovados += verificaTeste("N3", retornoN3 == -1 && acessos == 2);

        acessos = 0;
        int retornoN4 = busca(tabelaN, 55);
        aprovados += verificaTeste("N4", retornoN4 == -1 && acessos == 2);

        int[] tabelaC = criaTabela(11);
        insere(tabelaC, 22);
        insere(tabelaC, 33);
        insere(tabelaC, 44);
        insere(tabelaC, 55);
        reaproveitamentos = 0;
        boolean retornoC1 = remove(tabelaC, 33);
        aprovados += verificaTeste("C1", retornoC1 && tabelaC[1] == REMOVIDO);

        acessos = 0;
        int retornoC2 = busca(tabelaC, 44);
        aprovados += verificaTeste("C2", retornoC2 == 2 && acessos == 3);

        acessos = 0;
        int retornoC3 = busca(tabelaC, 55);
        aprovados += verificaTeste("C3", retornoC3 == 3 && acessos == 4);

        acessos = 0;
        int retornoC4 = busca(tabelaC, 77);
        aprovados += verificaTeste("C4", retornoC4 == -1 && acessos == 5);

        acessos = 0;
        int retornoC5 = busca(tabelaC, 33);
        aprovados += verificaTeste("C5", retornoC5 == -1 && acessos == 5);

        acessos = 0;
        boolean retornoC6 = remove(tabelaC, 99);
        aprovados += verificaTeste("C6", !retornoC6 && acessos == 5 && contaOcupadas(tabelaC) == 3 && contaRemovidas(tabelaC) == 1);

        acessos = 0;
        reaproveitamentos = 0;
        int retornoC7 = insere(tabelaC, 44);
        aprovados += verificaTeste("C7", retornoC7 == -1 && acessos == 3 && reaproveitamentos == 0);

        acessos = 0;
        reaproveitamentos = 0;
        int retornoC8 = insere(tabelaC, 66);
        aprovados += verificaTeste("C8", retornoC8 == 1 && acessos == 5 && reaproveitamentos == 1 && tabelaC[1] == 66 && contaRemovidas(tabelaC) == 0);

        int[] tabelaF = criaTabela(3);
        insere(tabelaF, 3);
        insere(tabelaF, 6);
        insere(tabelaF, 9);
        acessos = 0;
        int retornoF1 = busca(tabelaF, 12);
        aprovados += verificaTeste("F1", tabelaF[0] == 3 && tabelaF[1] == 6 && tabelaF[2] == 9 && retornoF1 == -1 && acessos == 3);

        acessos = 0;
        int retornoF2 = insere(tabelaF, 12);
        aprovados += verificaTeste("F2", retornoF2 == -2 && acessos == 3);

        remove(tabelaF, 6);
        acessos = 0;
        int retornoF3 = busca(tabelaF, 12);
        aprovados += verificaTeste("F3", tabelaF[1] == REMOVIDO && retornoF3 == -1 && acessos == 3);

        acessos = 0;
        reaproveitamentos = 0;
        int retornoF4 = insere(tabelaF, 12);
        aprovados += verificaTeste("F4", retornoF4 == 1 && acessos == 3 && reaproveitamentos == 1 && tabelaF[1] == 12);

        acessos = 0;
        int retornoF5 = busca(tabelaF, 15);
        aprovados += verificaTeste("F5", retornoF5 == -1 && acessos == 3);

        int[] tabelaO1 = criaTabela(11);
        insere(tabelaO1, 22);
        insere(tabelaO1, 33);
        insere(tabelaO1, 44);
        insere(tabelaO1, 55);
        remove(tabelaO1, 33);
        reorganiza(tabelaO1);
        aprovados += verificaTeste("O1", tabelaO1[0] == 22 && tabelaO1[1] == 44 && tabelaO1[2] == 55 && tabelaO1[3] == VAZIO && contaRemovidas(tabelaO1) == 0 && contaOcupadas(tabelaO1) == 3);

        acessos = 0;
        int retornoO2 = busca(tabelaO1, 55);
        aprovados += verificaTeste("O2", retornoO2 == 2 && acessos == 3);

        int[] tabelaO3 = criaTabela(11);
        insere(tabelaO3, 12);
        insere(tabelaO3, 22);
        insere(tabelaO3, 33);
        remove(tabelaO3, 22);
        aprovados += verificaTeste("O3", tabelaO3[0] == REMOVIDO && tabelaO3[1] == 12 && tabelaO3[2] == 33);

        reorganiza(tabelaO3);
        aprovados += verificaTeste("O4", tabelaO3[0] == 33 && tabelaO3[1] == 12 && tabelaO3[2] == VAZIO && contaRemovidas(tabelaO3) == 0);

        acessos = 0;
        int retornoO5 = busca(tabelaO3, 33);
        aprovados += verificaTeste("O5", retornoO5 == 0 && acessos == 1);

        System.out.println("TESTES: " + aprovados + " de " + total + " aprovados");
    }

    static void executaExperimentos() {
        Random sorteio = new Random(1990);
        int[] chaves = geraChaves(sorteio, 700);
        int descartadas = duplicadasDescartadas;
        int[] ausentes = geraAusentes(sorteio, chaves, 350);

        int[] tabela0 = criaTabela(1000);
        for (int indice = 0; indice < 700; indice++) {
            insere(tabela0, chaves[indice]);
        }
        int ocupadas0 = contaOcupadas(tabela0);
        int removidas0 = contaRemovidas(tabela0);
        int vazias0 = contaVazias(tabela0);
        acessos = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            busca(tabela0, chaves[indice]);
        }
        float acessosSucessoMedia0 = (float) acessos / 350;
        acessos = 0;
        for (int indice = 0; indice < 350; indice++) {
            busca(tabela0, ausentes[indice]);
        }
        float acessosFalhaMedia0 = (float) acessos / 350;
        System.out.printf("EXPERIMENTO 0 (antes das remocoes): ocupadas=%d removidas=%d vazias=%d acessosSucessoMedia=%.2f acessosFalhaMedia=%.2f%n", ocupadas0, removidas0, vazias0, acessosSucessoMedia0, acessosFalhaMedia0);

        int[] tabela1 = criaTabela(1000);
        for (int indice = 0; indice < 700; indice++) {
            insere(tabela1, chaves[indice]);
        }
        int removidasComSucesso1 = 0;
        for (int indice = 0; indice < 700; indice += 2) {
            if (removeIngenua(tabela1, chaves[indice])) {
                removidasComSucesso1++;
            }
        }
        int falsosNegativos1 = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            if (busca(tabela1, chaves[indice]) == -1) {
                falsosNegativos1++;
            }
        }
        int ocupadas1 = contaOcupadas(tabela1);
        int vazias1 = contaVazias(tabela1);
        System.out.printf("EXPERIMENTO 1 (remocao ingenua): removidasComSucesso=%d falsosNegativos=%d ocupadas=%d vazias=%d%n", removidasComSucesso1, falsosNegativos1, ocupadas1, vazias1);

        int[] tabela2 = criaTabela(1000);
        for (int indice = 0; indice < 700; indice++) {
            insere(tabela2, chaves[indice]);
        }
        int removidasComSucesso2 = 0;
        for (int indice = 0; indice < 700; indice += 2) {
            if (remove(tabela2, chaves[indice])) {
                removidasComSucesso2++;
            }
        }
        int falsosNegativos2 = 0;
        acessos = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            if (busca(tabela2, chaves[indice]) == -1) {
                falsosNegativos2++;
            }
        }
        float acessosSucessoMedia2 = (float) acessos / 350;
        acessos = 0;
        for (int indice = 0; indice < 350; indice++) {
            busca(tabela2, ausentes[indice]);
        }
        float acessosFalhaMedia2 = (float) acessos / 350;
        int ocupadas2 = contaOcupadas(tabela2);
        int removidas2 = contaRemovidas(tabela2);
        int vazias2 = contaVazias(tabela2);
        System.out.printf("EXPERIMENTO 2 (remocao com REMOVIDO): removidasComSucesso=%d falsosNegativos=%d ocupadas=%d removidas=%d vazias=%d acessosSucessoMedia=%.2f acessosFalhaMedia=%.2f%n", removidasComSucesso2, falsosNegativos2, ocupadas2, removidas2, vazias2, acessosSucessoMedia2, acessosFalhaMedia2);

        reorganiza(tabela2);
        int falsosNegativos3 = 0;
        acessos = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            if (busca(tabela2, chaves[indice]) == -1) {
                falsosNegativos3++;
            }
        }
        float acessosSucessoMedia3 = (float) acessos / 350;
        acessos = 0;
        for (int indice = 0; indice < 350; indice++) {
            busca(tabela2, ausentes[indice]);
        }
        float acessosFalhaMedia3 = (float) acessos / 350;
        int ocupadas3 = contaOcupadas(tabela2);
        int removidas3 = contaRemovidas(tabela2);
        int vazias3 = contaVazias(tabela2);
        System.out.printf("EXPERIMENTO 3 (apos reorganiza): falsosNegativos=%d ocupadas=%d removidas=%d vazias=%d acessosSucessoMedia=%.2f acessosFalhaMedia=%.2f%n", falsosNegativos3, ocupadas3, removidas3, vazias3, acessosSucessoMedia3, acessosFalhaMedia3);

        int[] tabela4 = criaTabela(1000);
        for (int indice = 0; indice < 700; indice++) {
            insere(tabela4, chaves[indice]);
        }
        for (int indice = 0; indice < 700; indice += 2) {
            remove(tabela4, chaves[indice]);
        }
        reaproveitamentos = 0;
        int inseridas4 = 0;
        for (int indice = 0; indice < 350; indice++) {
            if (insere(tabela4, ausentes[indice]) >= 0) {
                inseridas4++;
            }
        }
        int reaproveitamentos4 = reaproveitamentos;
        int falsosNegativos4 = 0;
        acessos = 0;
        for (int indice = 1; indice < 700; indice += 2) {
            if (busca(tabela4, chaves[indice]) == -1) {
                falsosNegativos4++;
            }
        }
        for (int indice = 0; indice < 350; indice++) {
            if (busca(tabela4, ausentes[indice]) == -1) {
                falsosNegativos4++;
            }
        }
        float acessosSucessoMedia4 = (float) acessos / 700;
        int ocupadas4 = contaOcupadas(tabela4);
        int removidas4 = contaRemovidas(tabela4);
        int vazias4 = contaVazias(tabela4);
        System.out.printf("EXPERIMENTO 4 (reinsercao): inseridas=%d reaproveitamentos=%d falsosNegativos=%d ocupadas=%d removidas=%d vazias=%d acessosSucessoMedia=%.2f%n", inseridas4, reaproveitamentos4, falsosNegativos4, ocupadas4, removidas4, vazias4, acessosSucessoMedia4);
    }

    public static void main(String[] args) {
        executaTestes();
        executaExperimentos();
    }
}