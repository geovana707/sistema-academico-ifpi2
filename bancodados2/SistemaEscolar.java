import java.io.*;
import java.util.Scanner;

public class SistemaEscolar {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int op = -1;
        while (op != 0) {
            System.out.println("\n========================================");
            System.out.println("       SISTEMA ACADÊMICO - IFPI         ");
            System.out.println("========================================");
            System.out.println(" [1] Gerenciar PROFESSORES");
            System.out.println(" [2] Gerenciar ALUNOS");
            System.out.println(" [3] Gerenciar DISCIPLINAS");
            System.out.println(" [0] Sair do Sistema");
            System.out.println("----------------------------------------");
            System.out.print(" Escolha uma opção: ");
            
            try {
                String entrada = sc.nextLine();
                op = Integer.parseInt(entrada);
                
                if (op == 1) menu("professores.txt", "Professor");
                else if (op == 2) menu("alunos.txt", "Aluno");
                else if (op == 3) menu("disciplinas.txt", "Disciplina");
                else if (op != 0) System.out.println("\n[!] Opção inexistente.");
                
            } catch (Exception e) { 
                System.out.println("\n[!] Erro: Digite apenas números."); 
                op = -1;
            }
        }
        System.out.println("\nSistema encerrado. Sucesso nos estudos, GG!");
    }

    public static void menu(String arq, String tipo) {
        int op = -1;
        while (op != 0) {
            System.out.println("\n>>> MÓDULO: " + tipo.toUpperCase());
            System.out.println(" [1] Cadastrar Novo");
            System.out.println(" [2] Visualizar Banco (Tabela)");
            System.out.println(" [3] Pesquisar por ID");
            System.out.println(" [0] Voltar");
            System.out.print(" Selecione: ");
            
            try {
                op = Integer.parseInt(sc.nextLine());
                if (op == 1) cadastrar(arq, tipo);
                else if (op == 2) listar(arq, tipo);
                else if (op == 3) buscar(arq, tipo);
            } catch (Exception e) { 
                System.out.println("\n[!] Opção inválida."); 
                op = -1;
            }
        }
    }

    public static void cadastrar(String arq, String tipo) {
        System.out.println("\n--- NOVO CADASTRO DE " + tipo.toUpperCase() + " ---");
        
        try {
            int id = pegarNovoId(arq);
            System.out.print(" Nome: "); String n = sc.nextLine();
            String linhaParaSalvar = "";

            if (tipo.equalsIgnoreCase("Professor")) {
                System.out.print(" Turma: "); String t = sc.nextLine();
                System.out.print(" Disciplina: "); String d = sc.nextLine();
                linhaParaSalvar = id + ";" + n + ";" + t + ";" + d;
            } else if (tipo.equalsIgnoreCase("Aluno")) {
                System.out.print(" Turma: "); String t = sc.nextLine();
                System.out.print(" Curso: "); String c = sc.nextLine();
                linhaParaSalvar = id + ";" + n + ";" + t + ";" + c;
            } else {
                System.out.print(" Carga Horária: "); String ch = sc.nextLine();
                linhaParaSalvar = id + ";" + n + ";" + ch;
            }

            // Gravação com fechamento forçado para garantir o salvamento
            FileWriter fw = new FileWriter(arq, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(linhaParaSalvar);
            pw.flush();
            pw.close();
            fw.close();

            System.out.println("\n[OK] " + tipo + " salvo com sucesso!");
            System.out.println("[OK] ID Gerado: " + id);

        } catch (IOException e) {
            System.out.println("[!] Erro ao gravar no arquivo: " + e.getMessage());
        }
    }

    public static void listar(String arq, String tipo) {
        System.out.println("\n==================== BANCO DE DADOS: " + tipo.toUpperCase() + " ====================");
        
        File f = new File(arq);
        if (!f.exists() || f.length() == 0) {
            System.out.println(" [!] O banco de " + tipo + " está vazio.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arq))) {
            // Cabeçalhos da Tabela
            if (tipo.equalsIgnoreCase("Professor")) {
                System.out.printf("%-5s | %-20s | %-10s | %-15s\n", "ID", "NOME", "TURMA", "DISCIPLINA");
            } else if (tipo.equalsIgnoreCase("Aluno")) {
                System.out.printf("%-5s | %-20s | %-10s | %-15s\n", "ID", "NOME", "TURMA", "CURSO");
            } else {
                System.out.printf("%-5s | %-20s | %-15s\n", "ID", "NOME", "CARGA H.");
            }
            System.out.println("----------------------------------------------------------------------");

            String l;
            while ((l = br.readLine()) != null) {
                if (l.trim().isEmpty()) continue;
                String[] d = l.split(";");
                
                // Preenchimento preventivo caso falte algum dado na linha
                String c1 = (d.length > 0) ? d[0] : "?";
                String c2 = (d.length > 1) ? d[1] : "---";
                String c3 = (d.length > 2) ? d[2] : "---";
                String c4 = (d.length > 3) ? d[3] : "---";

                if (tipo.equalsIgnoreCase("Disciplina")) {
                    System.out.printf("%-5s | %-20s | %-15s\n", c1, c2, c3);
                } else {
                    System.out.printf("%-5s | %-20s | %-10s | %-15s\n", c1, c2, c3, c4);
                }
            }
        } catch (Exception e) { 
            System.out.println(" [!] Erro ao ler o banco."); 
        }
        System.out.println("======================================================================");
    }

    public static void buscar(String arq, String tipo) {
        System.out.print("\n Digite o ID para busca: ");
        String idBusca = sc.nextLine().trim();
        boolean achou = false;

        File f = new File(arq);
        if (!f.exists()) {
            System.out.println(" [!] O banco ainda não existe.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arq))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] d = l.split(";");
                if (d[0].trim().equals(idBusca)) {
                    System.out.println("\n>>>> REGISTRO ENCONTRADO <<<<");
                    System.out.println(" ID        : " + d[0]);
                    System.out.println(" NOME      : " + d[1]);
                    
                    if (tipo.equalsIgnoreCase("Professor")) {
                        System.out.println(" TURMA     : " + (d.length > 2 ? d[2] : "---"));
                        System.out.println(" DISCIPLINA: " + (d.length > 3 ? d[3] : "---"));
                    } else if (tipo.equalsIgnoreCase("Aluno")) {
                        System.out.println(" TURMA     : " + (d.length > 2 ? d[2] : "---"));
                        System.out.println(" CURSO     : " + (d.length > 3 ? d[3] : "---"));
                    } else {
                        System.out.println(" CARGA H.  : " + (d.length > 2 ? d[2] : "---"));
                    }
                    System.out.println("----------------------------");
                    achou = true;
                    break;
                }
            }
            if (!achou) System.out.println("\n[!] Ninguém encontrado com o ID " + idBusca);
        } catch (Exception e) { 
            System.out.println("[!] Erro na pesquisa."); 
        }
    }

    public static int pegarNovoId(String arq) {
        int id = 1;
        File f = new File(arq);
        if (!f.exists()) return 1;

        try (BufferedReader br = new BufferedReader(new FileReader(arq))) {
            String l, ult = "";
            while ((l = br.readLine()) != null) {
                if (!l.trim().isEmpty()) ult = l;
            }
            if (!ult.isEmpty()) {
                String[] partes = ult.split(";");
                id = Integer.parseInt(partes[0].trim()) + 1;
            }
        } catch (Exception e) { 
            id = 1; 
        }
        return id;
    }
}