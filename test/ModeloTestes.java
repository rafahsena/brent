
import ENEM.Aluno;
import Organizador.OrganizadorBrent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jocelino Neto
 */
public class ModeloTestes {
    OrganizadorBrent orgSeq;
    public ModeloTestes(String nome) {
        try {
            orgSeq = new OrganizadorBrent(nome);
            
        } catch (Exception e) {
            System.err.println("Erro ao gerar organizador.");
        }
    }
    public void all(){
        this.orgSeq.getAll();
    }
    
    public boolean inserir(Aluno[] alunos){
        try{
            for(Aluno aluno : alunos){
                System.out.println("adicioando aluno....");
                this.orgSeq.addAluno(aluno);
            }
        }catch(Exception e){
            System.err.println("Erro ao adicionar alunos");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean recuperar(long[] matriculas){
        try {
            for(long matricula : matriculas){
                Aluno aluno = orgSeq.getAluno(matricula);
                if(aluno != null){
                    System.out.println(aluno.toString());
                }else{
                    System.err.println("Erro ao exibir aluno de matricula " +
                            matricula);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao recuperar.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean apagar(long[] matriculas){
        try{
            for(long matricula : matriculas){
                Aluno aluno = this.orgSeq.delAluno(matricula);
                if(aluno != null){
                    System.out.println("Aluno deletado!\n\n" + aluno);
                }else{
                    System.out.println("Aluno de matricula " + matricula +
                            " não deletado!");
                    return false;
                }
            }
        }catch(Exception e){
            System.err.println("Erro ao apagar alunos");
            return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
        Aluno[] alunos = {
            new Aluno(2, "Saitama", "One Punch", "onepunch@man.com", (short)10),
            new Aluno(6, "Neto", "Rosa Elze", "jocelino.neto@dcomp.ufs.br", (short)50),
            new Aluno(8, "Deku", "Boku no Hero", "deku@allmight.com", (short)10),
            new Aluno(4, "José", "Trav Francisco", "jose@gloria.com", (short)50),
            new Aluno(5, "Miguel", "Trav Assis", "miguel@gloria.com", (short)50),
            new Aluno(1, "Wednesday", "Valhalla", "odin@trickgod.com", (short)10),
            new Aluno(7, "Jax", "Sons of Anarchy", "jax@samcro.com", (short)15),
            new Aluno(3, "Dolores", "Westworld", "dolores@westworld.host", (short)20)
        };
        Aluno[] recAlunos = {
            new Aluno(5, "Miguel", "Trav Assis", "miguel@gloria.com", (short)50),
            new Aluno(3, "Dolores", "Westworld", "dolores@westworld.host", (short)20),
            new Aluno(8, "Deku", "Boku no Hero", "deku@allmight.com", (short)10),
            
        };
        Aluno[] rAlunos = {
            new Aluno((long)27, "Satã", "Inferno, Marmore", "sata@heavymetal.us", (short)69),
            new Aluno((long)18, "4Queijos", "Habbo", "4queijos@hotehabbo.com                            ", (short)24),
            new Aluno((long)29, "Naruto", "Konoha", "qqueroserhokage@ninja.com                          ", (short)69),
            new Aluno((long)28, "Dolores", "Westworld", "dolores@westworld.host", (short)20),
            new Aluno((long)39, "Deku", "UA", "deku@allmightfans.com", (short)11),
            new Aluno((long)13, "Jesus", "México", "jesusmexicano@americangods.com", (short)6),
            new Aluno((long)16, "Voldemort", "Hogwarts", "comensais@gogo.com", (short)25)
        };
        ModeloTestes testar = new ModeloTestes("aluno.db");
        
        //testar.inserir(rAlunos);
        long q[] = {(long)3};//, 3, 8};
        //testar.recuperar(q);
        testar.all();
        
        
    }
    
}