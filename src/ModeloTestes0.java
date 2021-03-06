
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
public class ModeloTestes0 {
    OrganizadorBrent orgSeq;
    public ModeloTestes0(String nome) {
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
                //System.out.println("adicioando aluno....");
                this.orgSeq.addAluno(aluno);
            }
        }catch(Exception e){
            System.err.println("Erro ao adicionar alunos");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean inserir(Aluno aluno){
        try{
            this.orgSeq.addAluno(aluno);
        }catch(Exception e){
            System.err.println("Erro ao adicionar aluno");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean recuperar(long[] matriculas){
        try {
            for(long matricula : matriculas){
                this.recuperar(matricula);
            }
        } catch (Exception e) {
            System.err.println("Erro ao recuperar.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean recuperar(long matricula){
        try {
            Aluno aluno = orgSeq.getAluno(matricula);
            if(aluno != null){
                System.out.println(aluno.toString());
            }else{
                System.err.println("Erro ao exibir aluno de matricula " +
                        matricula);
            }
        } catch (Exception e) {
            System.err.println("Erro ao recuperar.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean apagar(long matriculas){
        try{
            Aluno aluno = this.orgSeq.delAluno(matriculas);
            if(aluno != null){
                System.out.println("Aluno deletado!\n\n" + aluno);
            }else{
                System.out.println("Aluno de matricula " + matriculas +
                        " não deletado!");
                return false;
            }
        }catch(Exception e){
            System.err.println("Erro ao apagar alunos");
            return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
        Aluno[] rAlunos = {
            new Aluno((long)27, "Satã", "Inferno, Marmore", "sata@heavymetal.us", (short)69),
            new Aluno((long)18, "4Queijos", "Habbo", "4queijos@hotehabbo.com                            ", (short)24),
            new Aluno((long)29, "Naruto", "Konoha", "qqueroserhokage@ninja.com                          ", (short)69),
            new Aluno((long)28, "Dolores", "Westworld", "dolores@westworld.host", (short)20),
            new Aluno((long)39, "Deku", "UA", "deku@allmightfans.com", (short)11),
            new Aluno((long)13, "Jesus", "México", "jesusmexicano@americangods.com", (short)6),
            new Aluno((long)16, "Voldemort", "Hogwarts", "comensais@gogo.com", (short)25)
        };
        ModeloTestes0 testar = new ModeloTestes0("aluno.db");
        
        //testar.all();
        //testar.inserir(recAlunos[0]);
        //testar.apagar(recAlunos[0].getMatricula());
        //testar.inserir(rAlunos);
        //System.out.println("\n\n\n\n\n\n\n");
        //testar.all();
        //System.out.println("\n\n\n\n\n\n\n");
        long matDel = rAlunos[1].getMatricula();
        //System.out.println("\n\n\nRecuperar: " + matDel + "\n\n");
        testar.recuperar(matDel);
        //testar.apagar(matDel);

        
        
    }
    
}